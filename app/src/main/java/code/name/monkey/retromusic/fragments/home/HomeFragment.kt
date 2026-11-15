/*
 * Copyright (c) 2020 Hemanth Savarla.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 */
package code.name.monkey.retromusic.fragments.home

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import android.view.MenuItem.SHOW_AS_ACTION_IF_ROOM
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.os.bundleOf
import androidx.core.text.parseAsHtml
import androidx.core.view.doOnLayout
import androidx.core.view.doOnPreDraw
import androidx.core.view.WindowCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.core.view.updateLayoutParams
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import code.name.monkey.appthemehelper.common.ATHToolbarActivity
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper
import code.name.monkey.retromusic.*
import code.name.monkey.retromusic.adapter.HomeAdapter
import code.name.monkey.retromusic.databinding.FragmentHomeBinding
import code.name.monkey.retromusic.dialogs.CreatePlaylistDialog
import code.name.monkey.retromusic.dialogs.ImportPlaylistDialog
import code.name.monkey.retromusic.extensions.accentColor
import code.name.monkey.retromusic.extensions.dip
import code.name.monkey.retromusic.extensions.elevatedAccentColor
import code.name.monkey.retromusic.extensions.materialDialog
import code.name.monkey.retromusic.extensions.setUpMediaRouteButton
import code.name.monkey.retromusic.extensions.surfaceColor
import code.name.monkey.retromusic.fragments.ReloadType
import code.name.monkey.retromusic.fragments.base.AbsMainActivityFragment
import code.name.monkey.retromusic.glide.RetroGlideExtension
import code.name.monkey.retromusic.glide.RetroGlideExtension.profileBannerOptions
import code.name.monkey.retromusic.glide.RetroGlideExtension.songCoverOptions
import code.name.monkey.retromusic.glide.RetroGlideExtension.userProfileOptions
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.interfaces.IScrollHelper
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.util.PreferenceUtil.userName
import com.bumptech.glide.Glide
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis

class HomeFragment :
    AbsMainActivityFragment(R.layout.fragment_home), IScrollHelper {

    private var _binding: HomeBinding? = null
    private val binding get() = _binding!!

    // Toolbar over the cover wall: transparent at the top, fades to the normal toolbar while the
    // lists scroll up. null = not applied yet (or the wall is off).
    private var defaultAppBarBackground: Drawable? = null
    private var wallToolbarLight: Boolean? = null
    private var savedLightStatusBars: Boolean? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val homeBinding = FragmentHomeBinding.bind(view)
        _binding = HomeBinding(homeBinding)
        mainActivity.setSupportActionBar(binding.toolbar)
        mainActivity.supportActionBar?.title = null
        defaultAppBarBackground = binding.appBarLayout.background?.constantState?.newDrawable()?.mutate()
        setupListeners()
        val name = userName?.trim().orEmpty()
        val hasCustomName = name.isNotEmpty() && name != getString(R.string.user_name)
        binding.titleWelcome.isVisible = hasCustomName
        binding.titleWelcome.text = name

        enterTransition = MaterialFadeThrough().addTarget(binding.contentContainer)
        reenterTransition = MaterialFadeThrough().addTarget(binding.contentContainer)

        checkForMargins()

        val homeAdapter = HomeAdapter(mainActivity)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(mainActivity)
            adapter = homeAdapter
        }
        libraryViewModel.getSuggestions().observe(viewLifecycleOwner) {
            loadSuggestions(it)
        }
        libraryViewModel.getHome().observe(viewLifecycleOwner) {
            homeAdapter.swapData(it)
        }
        libraryViewModel.getHomeCovers().observe(viewLifecycleOwner) {
            binding.coverGrid.setCovers(it)
        }
        // The wall fills the whole visible area; the lists start below it.
        binding.container.doOnLayout {
            sizeCoverHeader()
            updateToolbarForScroll()
        }
        binding.container.setOnScrollChangeListener(
            androidx.core.widget.NestedScrollView.OnScrollChangeListener { _, _, _, _, _ ->
                updateToolbarForScroll()
            }
        )
        // A tap only plays the song (the mini player shows it); it does not open Now Playing.
        binding.coverGrid.onSongClick = { songs, index ->
            MusicPlayerRemote.openQueue(songs, index, true)
        }
        binding.coverGrid.onSongLongClick = { songs, index -> showWallMenu(songs, index) }

        loadProfile()
        setupTitle()
        colorButtons()
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
        view.doOnLayout {
            adjustPlaylistButtons()
        }
    }

    private fun adjustPlaylistButtons() {
        val buttons =
            listOf(binding.history, binding.lastAdded, binding.topPlayed, binding.actionShuffle)
        buttons.maxOf { it.lineCount }.let { maxLineCount ->
            buttons.forEach { button ->
                // Set the highest line count to every button for consistency
                button.setLines(maxLineCount)
            }
        }
    }

    private fun setupListeners() {
        binding.bannerImage?.setOnClickListener {
            findNavController().navigate(
                R.id.user_info_fragment, null, null, FragmentNavigatorExtras(
                    binding.userImage to "user_image"
                )
            )
            reenterTransition = null
        }

        // Tapping the logo on the cover wall opens the listening stats.
        binding.coverLogo.setOnClickListener {
            findNavController().navigate(R.id.listening_stats_fragment, null, navOptions)
        }

        binding.lastAdded.setOnClickListener {
            findNavController().navigate(
                R.id.detailListFragment,
                bundleOf(EXTRA_PLAYLIST_TYPE to LAST_ADDED_PLAYLIST)
            )
            setSharedAxisYTransitions()
        }

        binding.topPlayed.setOnClickListener {
            findNavController().navigate(
                R.id.detailListFragment,
                bundleOf(EXTRA_PLAYLIST_TYPE to TOP_PLAYED_PLAYLIST)
            )
            setSharedAxisYTransitions()
        }

        binding.actionShuffle.setOnClickListener {
            libraryViewModel.shuffleSongs()
        }

        binding.history.setOnClickListener {
            findNavController().navigate(
                R.id.detailListFragment,
                bundleOf(EXTRA_PLAYLIST_TYPE to HISTORY_PLAYLIST)
            )
            setSharedAxisYTransitions()
        }

        binding.userImage.setOnClickListener {
            findNavController().navigate(
                R.id.user_info_fragment, null, null, FragmentNavigatorExtras(
                    binding.userImage to "user_image"
                )
            )
        }
        // Reload suggestions
        binding.suggestions.refreshButton.setOnClickListener {
            libraryViewModel.forceReload(
                ReloadType.Suggestions
            )
        }
    }

    private fun setupTitle() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_search, null, navOptions)
        }
        val hexColor = String.format("#%06X", 0xFFFFFF and accentColor())
        val appName = "R <font color=$hexColor>Music</font>".parseAsHtml()
        binding.appBarLayout.title = appName
    }

    private fun loadProfile() {
        binding.bannerImage?.let {
            Glide.with(requireContext())
                .load(RetroGlideExtension.getBannerModel())
                .profileBannerOptions(RetroGlideExtension.getBannerModel())
                .into(it)
        }
        Glide.with(requireActivity())
            .load(RetroGlideExtension.getUserModel())
            .userProfileOptions(RetroGlideExtension.getUserModel(), requireContext())
            .into(binding.userImage)
    }

    fun colorButtons() {
        binding.history.elevatedAccentColor()
        binding.lastAdded.elevatedAccentColor()
        binding.topPlayed.elevatedAccentColor()
        binding.actionShuffle.elevatedAccentColor()
    }

    private fun sizeCoverHeader() {
        // The scroll view fills the screen, also behind the status bar and the toolbar.
        val height = binding.container.height
        if (height > 0) {
            binding.coverHeader.updateLayoutParams<ViewGroup.LayoutParams> {
                this.height = height
            }
        }
        // Keep the logo and the buttons clear of the bottom navigation and the mini player.
        val bottomInset =
            (if (mainActivity.isBottomNavVisible) dip(R.dimen.bottom_nav_height) else 0) +
                    dip(R.dimen.mini_player_height)
        binding.coverOverlay.updatePadding(bottom = bottomInset)
    }

    /** The four main buttons sit on the wall when it is on, otherwise in the normal flow. */
    private fun placePlaylistButtons(onWall: Boolean) {
        val view = binding.absPlaylistsView
        val target: ViewGroup = if (onWall) binding.coverOverlay else binding.playlistsHolder
        if (view.parent === target) return
        (view.parent as? ViewGroup)?.removeView(view)
        target.addView(
            view,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )
    }

    /**
     * With the wall on, the toolbar is transparent over it (white icons) and turns into the normal
     * toolbar when the lists reach it.
     */
    private fun updateToolbarForScroll() {
        val b = _binding ?: return
        if (!PreferenceUtil.homeCoversEnabled) {
            restoreToolbar()
            return
        }
        val appBarHeight = b.appBarLayout.height
        val hero = b.coverHeader.height
        if (appBarHeight <= 0 || hero <= 0) return
        val end = (hero - appBarHeight).toFloat()
        val start = end - appBarHeight * 2f
        val progress = ((b.container.scrollY - start) / (end - start)).coerceIn(0f, 1f)
        b.appBarLayout.setBackgroundColor(ColorUtil.withAlpha(surfaceColor(), progress))
        setToolbarContentLight(progress < 0.5f)
    }

    private fun setToolbarContentLight(light: Boolean) {
        val b = _binding ?: return
        if (wallToolbarLight != light) {
            wallToolbarLight = light
            ToolbarContentTintHelper.setToolbarContentColorBasedOnToolbarColor(
                requireContext(), b.toolbar, b.toolbar.menu, if (light) Color.BLACK else surfaceColor()
            )
        }
        // Not guarded by the check above: onPause gives the icons back and they must return on resume.
        setStatusBarIconsLight(light)
    }

    /** White status bar icons while the toolbar sits on the dark wall. */
    private fun setStatusBarIconsLight(light: Boolean) {
        val window = activity?.window ?: return
        val controller = WindowCompat.getInsetsController(window, window.decorView)
        if (savedLightStatusBars == null) savedLightStatusBars = controller.isAppearanceLightStatusBars
        val target = if (light) false else savedLightStatusBars!!
        if (controller.isAppearanceLightStatusBars != target) {
            controller.isAppearanceLightStatusBars = target
        }
    }

    private fun restoreStatusBarIcons() {
        val saved = savedLightStatusBars ?: return
        val window = activity?.window ?: return
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = saved
    }

    private fun restoreToolbar() {
        val b = _binding ?: return
        if (wallToolbarLight == null) return
        wallToolbarLight = null
        defaultAppBarBackground?.let { b.appBarLayout.background = it }
        ToolbarContentTintHelper.setToolbarContentColorBasedOnToolbarColor(
            requireContext(), b.toolbar, b.toolbar.menu, surfaceColor()
        )
        restoreStatusBarIcons()
    }

    private fun setupCovers() {
        val enabled = PreferenceUtil.homeCoversEnabled
        binding.coverHeader.isVisible = enabled
        placePlaylistButtons(enabled)
        if (!enabled) {
            restoreToolbar()
            return
        }
        binding.coverGrid.configure(
            PreferenceUtil.homeCoversAngle,
            PreferenceUtil.homeCoversDarkness,
            PreferenceUtil.homeCoversSpeed
        )
        reloadWallCovers()
    }

    private fun reloadWallCovers() {
        libraryViewModel.loadHomeCovers(
            PreferenceUtil.homeCoversSource,
            PreferenceUtil.homeCoversCount
        )
    }

    /** Long press on a cover: pin / unpin it on the wall, or open it in Now Playing. */
    private fun showWallMenu(songs: List<Song>, index: Int) {
        val song = songs.getOrNull(index) ?: return
        val pinned = PreferenceUtil.homeCoversPinned
        val isPinned = song.id in pinned
        materialDialog(R.string.wall_cover_menu_title)
            .setTitle(song.title)
            .setItems(
                arrayOf(
                    getString(if (isPinned) R.string.wall_unpin else R.string.wall_pin),
                    getString(R.string.wall_go_to_song)
                )
            ) { _, which ->
                when (which) {
                    0 -> {
                        PreferenceUtil.homeCoversPinned =
                            if (isPinned) pinned - song.id else pinned + song.id
                        reloadWallCovers()
                    }

                    1 -> {
                        MusicPlayerRemote.openQueue(songs, index, true)
                        mainActivity.expandPanel()
                    }
                }
            }
            .show()
    }

    private fun checkForMargins() {
        if (mainActivity.isBottomNavVisible) {
            binding.recyclerView.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                bottomMargin = dip(R.dimen.bottom_nav_height)
            }
        }
    }

    override fun onCreateMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        menu.removeItem(R.id.action_grid_size)
        menu.removeItem(R.id.action_layout_type)
        menu.removeItem(R.id.action_sort_order)
        menu.findItem(R.id.action_settings).setShowAsAction(SHOW_AS_ACTION_IF_ROOM)
        // The nav destination id doubles as the menu item id. With the cover wall on, the logo
        // opens the stats instead, so the menu entry is only needed when the wall is off.
        if (!PreferenceUtil.homeCoversEnabled) {
            menu.add(Menu.NONE, R.id.listening_stats_fragment, 9, R.string.listening_stats)
        }
        ToolbarContentTintHelper.handleOnCreateOptionsMenu(
            requireContext(),
            binding.toolbar,
            menu,
            if (wallToolbarLight == true) Color.BLACK
            else ATHToolbarActivity.getToolbarBackgroundColor(binding.toolbar)
        )
        //Setting up cast button
        requireContext().setUpMediaRouteButton(menu)
    }

    override fun scrollToTop() {
        binding.container.scrollTo(0, 0)
        binding.appBarLayout.setExpanded(true)
    }

    fun setSharedAxisXTransitions() {
        exitTransition =
            MaterialSharedAxis(MaterialSharedAxis.X, true).addTarget(CoordinatorLayout::class.java)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
    }

    private fun setSharedAxisYTransitions() {
        exitTransition =
            MaterialSharedAxis(MaterialSharedAxis.Y, true).addTarget(CoordinatorLayout::class.java)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Y, false)
    }

    private fun loadSuggestions(songs: List<Song>) {
        if (!PreferenceUtil.homeSuggestions || songs.isEmpty()) {
            binding.suggestions.root.isVisible = false
            return
        }
        val images = listOf(
            binding.suggestions.image1,
            binding.suggestions.image2,
            binding.suggestions.image3,
            binding.suggestions.image4,
            binding.suggestions.image5,
            binding.suggestions.image6,
            binding.suggestions.image7,
            binding.suggestions.image8
        )
        val color = accentColor()
        binding.suggestions.message.apply {
            setTextColor(color)
            setOnClickListener {
                it.isClickable = false
                it.postDelayed({ it.isClickable = true }, 500)
                MusicPlayerRemote.playNext(songs.subList(0, 8))
                if (!MusicPlayerRemote.isPlaying) {
                    MusicPlayerRemote.playNextSong()
                }
            }
        }
        binding.suggestions.card6.setCardBackgroundColor(ColorUtil.withAlpha(color, 0.12f))
        images.forEachIndexed { index, imageView ->
            imageView.setOnClickListener {
                it.isClickable = false
                it.postDelayed({ it.isClickable = true }, 500)
                MusicPlayerRemote.playNext(songs[index])
                if (!MusicPlayerRemote.isPlaying) {
                    MusicPlayerRemote.playNextSong()
                }
            }
            Glide.with(this)
                .load(RetroGlideExtension.getSongModel(songs[index]))
                .songCoverOptions(songs[index])
                .into(imageView)
        }
    }

    companion object {

        const val TAG: String = "BannerHomeFragment"

        @JvmStatic
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun onMenuItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> findNavController().navigate(
                R.id.settings_fragment,
                null,
                navOptions
            )

            R.id.listening_stats_fragment -> findNavController().navigate(
                R.id.listening_stats_fragment,
                null,
                navOptions
            )

            R.id.action_import_playlist -> ImportPlaylistDialog().show(
                childFragmentManager,
                "ImportPlaylist"
            )

            R.id.action_add_to_playlist -> CreatePlaylistDialog.create(emptyList()).show(
                childFragmentManager,
                "ShowCreatePlaylistDialog"
            )
        }
        return false
    }

    override fun onPrepareMenu(menu: Menu) {
        super.onPrepareMenu(menu)
        ToolbarContentTintHelper.handleOnPrepareOptionsMenu(requireActivity(), binding.toolbar)
    }

    override fun onResume() {
        super.onResume()
        checkForMargins()
        libraryViewModel.forceReload(ReloadType.HomeSections)
        setupCovers()
        view?.post { updateToolbarForScroll() }
        // The stats menu entry depends on whether the cover wall is on.
        requireActivity().invalidateMenu()
        exitTransition = null
    }

    override fun onPause() {
        super.onPause()
        // Other screens have a normal toolbar, so give the status bar icons back.
        restoreStatusBarIcons()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        restoreStatusBarIcons()
        _binding = null
    }
}
