package code.name.monkey.retromusic.fragments.mainactivity.home

import android.app.ActivityOptions
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import code.name.monkey.appthemehelper.common.ATHToolbarActivity
import code.name.monkey.appthemehelper.util.ATHUtil
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper
import code.name.monkey.retromusic.App
import code.name.monkey.retromusic.Constants
import code.name.monkey.retromusic.Constants.USER_BANNER
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.adapter.HomeAdapter
import code.name.monkey.retromusic.dialogs.OptionsSheetDialogFragment
import code.name.monkey.retromusic.extensions.show
import code.name.monkey.retromusic.fragments.base.AbsMainActivityFragment
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.interfaces.MainActivityFragmentCallbacks
import code.name.monkey.retromusic.loaders.SongLoader
import code.name.monkey.retromusic.model.Home
import code.name.monkey.retromusic.model.smartplaylist.HistoryPlaylist
import code.name.monkey.retromusic.model.smartplaylist.LastAddedPlaylist
import code.name.monkey.retromusic.model.smartplaylist.MyTopTracksPlaylist
import code.name.monkey.retromusic.mvp.presenter.HomePresenter
import code.name.monkey.retromusic.mvp.presenter.HomeView
import code.name.monkey.retromusic.util.NavigationUtil
import code.name.monkey.retromusic.util.PreferenceUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.abs_playlists.actionShuffle
import kotlinx.android.synthetic.main.abs_playlists.history
import kotlinx.android.synthetic.main.abs_playlists.lastAdded
import kotlinx.android.synthetic.main.abs_playlists.topPlayed
import kotlinx.android.synthetic.main.fragment_banner_home.bannerImage
import kotlinx.android.synthetic.main.fragment_banner_home.titleWelcome
import kotlinx.android.synthetic.main.fragment_banner_home.toolbarContainer
import kotlinx.android.synthetic.main.fragment_banner_home.userImage
import kotlinx.android.synthetic.main.fragment_home.container
import kotlinx.android.synthetic.main.home_content.emptyContainer
import kotlinx.android.synthetic.main.home_content.recyclerView
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper
import java.io.File
import java.util.ArrayList
import java.util.Calendar
import java.util.Random
import javax.inject.Inject

class BannerHomeFragment : AbsMainActivityFragment(), MainActivityFragmentCallbacks, HomeView {
    @Inject
    lateinit var homePresenter: HomePresenter

    private lateinit var homeAdapter: HomeAdapter
    private lateinit var toolbar: Toolbar

    override fun sections(sections: ArrayList<Home>) {
        println(sections.size)
        homeAdapter.swapData(sections)
    }

    override fun onCreateView(inflater: LayoutInflater, viewGroup: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(
            if (PreferenceUtil.getInstance(requireContext()).isHomeBanner) R.layout.fragment_banner_home else R.layout.fragment_home,
            viewGroup,
            false
        )
    }

    private fun loadImageFromStorage() {
        Glide.with(requireContext())
            .load(File(PreferenceUtil.getInstance(requireContext()).profileImage, Constants.USER_PROFILE))
            .asBitmap()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.ic_person_flat)
            .error(R.drawable.ic_person_flat)
            .into(userImage)
    }

    private val displayMetrics: DisplayMetrics
        get() {
            val display = mainActivity.windowManager.defaultDisplay
            val metrics = DisplayMetrics()
            display.getMetrics(metrics)
            return metrics
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStatusBarColorAuto(view)
        toolbar = view.findViewById(R.id.toolbar)

        bannerImage?.setOnClickListener {
            val options = ActivityOptions.makeSceneTransitionAnimation(
                mainActivity,
                userImage,
                getString(R.string.transition_user_image)
            )
            NavigationUtil.goToUserInfo(requireActivity(), options)
        }

        lastAdded.setOnClickListener {
            NavigationUtil.goToPlaylistNew(requireActivity(), LastAddedPlaylist(requireActivity()))
        }

        topPlayed.setOnClickListener {
            NavigationUtil.goToPlaylistNew(requireActivity(), MyTopTracksPlaylist(requireActivity()))
        }

        actionShuffle.setOnClickListener {
            MusicPlayerRemote.openAndShuffleQueue(SongLoader.getAllSongs(requireActivity()), true)
        }

        history.setOnClickListener {
            NavigationUtil.goToPlaylistNew(requireActivity(), HistoryPlaylist(requireActivity()))
        }

        setupToolbar()

        userImage?.setOnClickListener {
            val options = ActivityOptions.makeSceneTransitionAnimation(
                mainActivity,
                userImage,
                getString(R.string.transition_user_image)
            )
            NavigationUtil.goToUserInfo(requireActivity(), options)
        }
        titleWelcome?.text = String.format("%s", PreferenceUtil.getInstance(requireContext()).userName)

        App.musicComponent.inject(this)
        homeAdapter = HomeAdapter(mainActivity, displayMetrics)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(mainActivity)
            adapter = homeAdapter
        }
        homePresenter.attachView(this)
        homePresenter.loadSections()

        OverScrollDecoratorHelper.setUpOverScroll(container)
    }

    private fun setupToolbar() {

        toolbar.apply {
            backgroundTintList =
                ColorStateList.valueOf(ATHUtil.resolveColor(requireContext(), R.attr.colorSurface))
            setNavigationIcon(R.drawable.ic_menu_white_24dp)
            setOnClickListener {
                val options = ActivityOptions.makeSceneTransitionAnimation(
                    mainActivity,
                    toolbarContainer,
                    getString(R.string.transition_toolbar)
                )
                NavigationUtil.goToSearch(requireActivity(), options)
            }

        }
        mainActivity.setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { showMainMenu(OptionsSheetDialogFragment.LIBRARY) }
    }

    override fun handleBackPress(): Boolean {
        return false
    }

    override fun onResume() {
        super.onResume()
        getTimeOfTheDay()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        homePresenter.detachView()
    }

    override fun showEmptyView() {
        emptyContainer.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_search, menu)

        ToolbarContentTintHelper.handleOnCreateOptionsMenu(
            requireActivity(),
            toolbar,
            menu,
            ATHToolbarActivity.getToolbarBackgroundColor(toolbar)
        )
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        ToolbarContentTintHelper.handleOnPrepareOptionsMenu(requireActivity(), toolbar)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_search) {
            val options = ActivityOptions.makeSceneTransitionAnimation(
                mainActivity,
                toolbarContainer,
                getString(R.string.transition_toolbar)
            )
            NavigationUtil.goToSearch(requireActivity(), true, options)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getTimeOfTheDay() {
        val c = Calendar.getInstance()
        val timeOfDay = c.get(Calendar.HOUR_OF_DAY)
        var images = arrayOf<String>()
        when (timeOfDay) {
            in 0..5 -> images = resources.getStringArray(R.array.night)
            in 6..11 -> images = resources.getStringArray(R.array.morning)
            in 12..15 -> images = resources.getStringArray(R.array.after_noon)
            in 16..19 -> images = resources.getStringArray(R.array.evening)
            in 20..23 -> images = resources.getStringArray(R.array.night)
        }
        val day = images[Random().nextInt(images.size)]
        loadTimeImage(day)
    }

    private fun loadTimeImage(day: String) {
        bannerImage?.let {
            val request = Glide.with(requireContext())
            if (PreferenceUtil.getInstance(requireContext()).bannerImage.isEmpty()) {
                request.load(day)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .placeholder(R.drawable.material_design_default)
                    .error(R.drawable.material_design_default)
                    .into(it)
            } else {
                request.load(File(PreferenceUtil.getInstance(requireContext()).bannerImage, USER_BANNER))
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .placeholder(R.drawable.material_design_default)
                    .error(R.drawable.material_design_default)
                    .into(it)
            }
        }
        loadImageFromStorage()
    }

    companion object {

        const val TAG: String = "BannerHomeFragment"

        fun newInstance(): BannerHomeFragment {
            return BannerHomeFragment()
        }
    }
}