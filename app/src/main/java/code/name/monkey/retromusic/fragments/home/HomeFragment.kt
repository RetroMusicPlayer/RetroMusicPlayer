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

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.MenuItem.SHOW_AS_ACTION_IF_ROOM
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.core.text.HtmlCompat
import androidx.core.view.doOnLayout
import androidx.core.view.doOnPreDraw
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import code.name.monkey.appthemehelper.common.ATHToolbarActivity
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper
import code.name.monkey.retromusic.*
import code.name.monkey.retromusic.adapter.HomeAdapter
import code.name.monkey.retromusic.databinding.FragmentBannerHomeBinding
import code.name.monkey.retromusic.databinding.FragmentHomeBinding
import code.name.monkey.retromusic.dialogs.CreatePlaylistDialog
import code.name.monkey.retromusic.dialogs.ImportPlaylistDialog
import code.name.monkey.retromusic.extensions.accentColor
import code.name.monkey.retromusic.extensions.drawNextToNavbar
import code.name.monkey.retromusic.extensions.elevatedAccentColor
import code.name.monkey.retromusic.fragments.base.AbsMainActivityFragment
import code.name.monkey.retromusic.glide.GlideApp
import code.name.monkey.retromusic.glide.RetroGlideExtension
import code.name.monkey.retromusic.interfaces.IScrollHelper
import code.name.monkey.retromusic.util.PreferenceUtil
import com.google.android.gms.cast.framework.CastButtonFactory
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis

class HomeFragment :
    AbsMainActivityFragment(if (PreferenceUtil.isHomeBanner) R.layout.fragment_banner_home else R.layout.fragment_home),
    IScrollHelper {

    private var _binding: HomeBindingAdapter? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = getBinding(PreferenceUtil.isHomeBanner, view)
        mainActivity.setSupportActionBar(binding.toolbar)
        mainActivity.supportActionBar?.title = null
        setupListeners()
        binding.titleWelcome.text = String.format("%s", PreferenceUtil.userName)

        enterTransition = MaterialFadeThrough().addTarget(binding.contentContainer)
        reenterTransition = MaterialFadeThrough().addTarget(binding.contentContainer)

        val homeAdapter = HomeAdapter(mainActivity)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(mainActivity)
            adapter = homeAdapter
        }
        libraryViewModel.getHome().observe(viewLifecycleOwner, {
            homeAdapter.swapData(it)
        })

        loadProfile()
        setupTitle()
        colorButtons()
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
        binding.appBarLayout.statusBarForeground =
            MaterialShapeDrawable.createWithElevationOverlay(requireContext())
        binding.toolbar.drawNextToNavbar()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            remove()
            mainActivity.finish()
        }
        view.doOnLayout {
            adjustPlaylistButtons()
        }
    }

    private fun adjustPlaylistButtons() {
        val buttons =
            listOf(binding.history, binding.lastAdded, binding.topPlayed, binding.actionShuffle)
        buttons.maxOf { it.lineCount }.let { maxLineCount->
            buttons.forEach { button ->
                // Set the highest line count to every button for consistency
                button.setLines(maxLineCount)
            }
        }

    }

    private fun setupListeners() {

        binding.downloadButton?.setOnClickListener {
            val toast = Toast.makeText(
                activity?.applicationContext,
                "Test",
                Toast.LENGTH_SHORT
            )
            toast.show()
            findNavController().navigate(
                R.id.download_fragment
            )
        }

        binding.bannerImage?.setOnClickListener {
            findNavController().navigate(
                R.id.user_info_fragment, null, null, FragmentNavigatorExtras(
                    binding.userImage to "user_image"
                )
            )
            reenterTransition = null
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
    }

    private fun getBinding(homeBanner: Boolean, view: View): HomeBindingAdapter {
        return if (homeBanner) {
            val homeBannerBinding = FragmentBannerHomeBinding.bind(view)
            HomeBindingAdapter(null, homeBannerBinding)
        } else {
            val homeBinding = FragmentHomeBinding.bind(view)
            HomeBindingAdapter(homeBinding, null)
        }
    }

    private fun setupTitle() {
        binding.toolbar.setNavigationOnClickListener {
            exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).addTarget(binding.root)
            reenterTransition =
                MaterialSharedAxis(MaterialSharedAxis.Z, false)
            findNavController().navigate(R.id.searchFragment, null, navOptions)
        }
        val hexColor = String.format("#%06X", 0xFFFFFF and accentColor())
        val appName = HtmlCompat.fromHtml(
            "Retro <span  style='color:$hexColor';>Music</span>",
            HtmlCompat.FROM_HTML_MODE_COMPACT
        )
        binding.appNameText.text = appName
    }

    private fun loadProfile() {
        binding.bannerImage?.let {
            GlideApp.with(requireContext())
                .asBitmap()
                .profileBannerOptions(RetroGlideExtension.getBannerModel())
                .load(RetroGlideExtension.getBannerModel())
                .into(it)
        }
        GlideApp.with(requireActivity()).asBitmap()
            .userProfileOptions(RetroGlideExtension.getUserModel())
            .load(RetroGlideExtension.getUserModel())
            .into(binding.userImage)
    }

    fun colorButtons() {
        binding.history.elevatedAccentColor()
        binding.lastAdded.elevatedAccentColor()
        binding.topPlayed.elevatedAccentColor()
        binding.actionShuffle.elevatedAccentColor()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main, menu)
        menu.removeItem(R.id.action_grid_size)
        menu.removeItem(R.id.action_layout_type)
        menu.removeItem(R.id.action_sort_order)
        menu.findItem(R.id.action_settings).setShowAsAction(SHOW_AS_ACTION_IF_ROOM)
        ToolbarContentTintHelper.handleOnCreateOptionsMenu(
            requireContext(),
            binding.toolbar,
            menu,
            ATHToolbarActivity.getToolbarBackgroundColor(binding.toolbar)
        )
        //Setting up cast button
        CastButtonFactory.setUpMediaRouteButton(requireContext(), menu, R.id.action_cast)
    }

    override fun scrollToTop() {
        binding.container.scrollTo(0, 0)
        binding.appBarLayout.setExpanded(true)
    }

    fun setSharedAxisXTransitions() {
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true).apply {
            addTarget(binding.root)
        }
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
    }

    private fun setSharedAxisYTransitions() {
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Y, true).apply {
            addTarget(binding.root)
        }
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Y, false)
    }

    companion object {

        const val TAG: String = "BannerHomeFragment"

        @JvmStatic
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> findNavController().navigate(
                R.id.settingsActivity,
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
        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        ToolbarContentTintHelper.handleOnPrepareOptionsMenu(requireActivity(), binding.toolbar)
    }

    override fun onResume() {
        super.onResume()
        libraryViewModel.fetchHomeSections()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
