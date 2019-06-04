package code.name.monkey.retromusic.fragments.mainactivity.home

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.LinearLayoutManager
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.common.ATHToolbarActivity
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper
import code.name.monkey.retromusic.Constants
import code.name.monkey.retromusic.Constants.USER_BANNER
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.adapter.HomeAdapter
import code.name.monkey.retromusic.fragments.base.AbsMainActivityFragment
import code.name.monkey.retromusic.glide.GlideApp
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.interfaces.MainActivityFragmentCallbacks
import code.name.monkey.retromusic.loaders.SongLoader
import code.name.monkey.retromusic.model.Home
import code.name.monkey.retromusic.model.smartplaylist.HistoryPlaylist
import code.name.monkey.retromusic.model.smartplaylist.LastAddedPlaylist
import code.name.monkey.retromusic.model.smartplaylist.MyTopTracksPlaylist
import code.name.monkey.retromusic.mvp.contract.HomeContract
import code.name.monkey.retromusic.mvp.presenter.HomePresenter
import code.name.monkey.retromusic.util.Compressor
import code.name.monkey.retromusic.util.NavigationUtil
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.util.RetroUtil
import com.bumptech.glide.load.engine.DiskCacheStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.abs_playlists.*
import kotlinx.android.synthetic.main.fragment_banner_home.*
import kotlinx.android.synthetic.main.fragment_home.recyclerView
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class BannerHomeFragment : AbsMainActivityFragment(), MainActivityFragmentCallbacks, HomeContract.HomeView {
    override fun showEmpty() {

    }

    private lateinit var disposable: CompositeDisposable
    private lateinit var homePresenter: HomePresenter
    private lateinit var contentContainerView: View
    private lateinit var lastAdded: View
    private lateinit var topPlayed: View
    private lateinit var actionShuffle: View
    private lateinit var history: View
    private lateinit var toolbar: Toolbar

    override fun onCreateView(inflater: LayoutInflater, viewGroup: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(if (PreferenceUtil.getInstance().isHomeBanner) R.layout.fragment_banner_home else R.layout.fragment_home, viewGroup, false)
    }

    private fun loadImageFromStorage() {
        disposable.add(Compressor(context!!)
                .setMaxHeight(300)
                .setMaxWidth(300)
                .setQuality(75)
                .setCompressFormat(Bitmap.CompressFormat.WEBP)
                .compressToBitmapAsFlowable(File(PreferenceUtil.getInstance().profileImage, Constants.USER_PROFILE))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it != null) {
                        userImage.setImageBitmap(it)
                    } else {
                        userImage.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_person_flat))
                    }
                }) {
                    userImage.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_person_flat))
                })
    }

    private val displayMetrics: DisplayMetrics
        get() {
            val display = mainActivity.windowManager.defaultDisplay
            val metrics = DisplayMetrics()
            display.getMetrics(metrics)
            return metrics
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homePresenter = HomePresenter(this)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar = view.findViewById(R.id.toolbar)
        bannerImage?.setOnClickListener {
            NavigationUtil.goToUserInfo(activity!!)
        }
        if (!PreferenceUtil.getInstance().isHomeBanner)
            setStatusbarColorAuto(view)

        lastAdded = view.findViewById(R.id.lastAdded)
        lastAdded.setOnClickListener {
            NavigationUtil.goToPlaylistNew(mainActivity, LastAddedPlaylist(mainActivity))
        }

        topPlayed = view.findViewById(R.id.topPlayed)
        topPlayed.setOnClickListener {
            NavigationUtil.goToPlaylistNew(mainActivity, MyTopTracksPlaylist(mainActivity))
        }

        actionShuffle = view.findViewById(R.id.actionShuffle)
        actionShuffle.setOnClickListener {
            MusicPlayerRemote.openAndShuffleQueue(SongLoader.getAllSongs(mainActivity).blockingFirst(), true)
        }

        history = view.findViewById(R.id.history)
        history.setOnClickListener {
            NavigationUtil.goToPlaylistNew(mainActivity, HistoryPlaylist(mainActivity))
        }

        homePresenter = HomePresenter(this)

        contentContainerView = view.findViewById(R.id.contentContainer)
        contentContainerView.setBackgroundColor(ThemeStore.primaryColor(context!!))

        setupToolbar()
        homeAdapter = HomeAdapter(mainActivity, ArrayList(), displayMetrics)

        homePresenter.subscribe()

        checkPadding()

        userInfoContainer.setOnClickListener {
            NavigationUtil.goToUserInfo(activity!!)
        }
        titleWelcome.setTextColor(ThemeStore.textColorPrimary(context!!))
        titleWelcome.text = String.format("%s", PreferenceUtil.getInstance().userName)
    }

    private fun checkPadding() {
        val marginSpan = when {
            MusicPlayerRemote.playingQueue.isEmpty() -> RetroUtil.convertDpToPixel(52f, context!!).toInt()
            else -> RetroUtil.convertDpToPixel(0f, context!!).toInt()
        }

        (recyclerView.layoutParams as ViewGroup.MarginLayoutParams).bottomMargin = (marginSpan * 2.3f).toInt()
    }

    private fun setupToolbar() {
        toolbarContainer.setBackgroundColor(Color.TRANSPARENT)
        toolbar.apply {
            setBackgroundColor(ColorUtil.withAlpha(RetroUtil.toolbarColor(mainActivity), 0.85f))
            setNavigationIcon(R.drawable.ic_menu_white_24dp)
            setOnClickListener {
                val pairImageView = Pair.create<View, String>(toolbarContainer, resources.getString(R.string.transition_toolbar))
                NavigationUtil.goToSearch(activity!!, pairImageView)
            }
        }
        mainActivity.setSupportActionBar(toolbar)
    }

    override fun handleBackPress(): Boolean {
        return false
    }

    override fun onResume() {
        super.onResume()
        disposable = CompositeDisposable()
        getTimeOfTheDay()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposable.dispose()
        homePresenter.unsubscribe()
    }

    override fun loading() {

    }

    override fun showEmptyView() {

    }

    override fun completed() {

    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        checkPadding()
    }

    override fun onQueueChanged() {
        super.onQueueChanged()
        checkPadding()
    }

    private lateinit var homeAdapter: HomeAdapter

    override fun showData(list: ArrayList<Home>) {
        val finalList = list.sortedWith(compareBy { it.priority })
        homeAdapter.swapData(finalList)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(mainActivity)
            adapter = homeAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_search, menu)

        val activity = activity ?: return
        ToolbarContentTintHelper.handleOnCreateOptionsMenu(activity, toolbar, menu, ATHToolbarActivity.getToolbarBackgroundColor(toolbar))
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val activity = activity ?: return
        ToolbarContentTintHelper.handleOnPrepareOptionsMenu(activity, toolbar)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_search) {
            val pairImageView = Pair.create<View, String>(toolbarContainer, resources.getString(R.string.transition_toolbar))
            NavigationUtil.goToSearch(mainActivity, true, pairImageView)
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
        if (bannerImage != null) {
            if (PreferenceUtil.getInstance().bannerImage.isEmpty()) {
                GlideApp.with(activity!!)
                        .load(day)
                        .placeholder(R.drawable.material_design_default)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(bannerImage!!)
            } else {
                disposable.add(Compressor(context!!)
                        .setQuality(100)
                        .setCompressFormat(Bitmap.CompressFormat.WEBP)
                        .compressToBitmapAsFlowable(File(PreferenceUtil.getInstance().bannerImage, USER_BANNER))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { bannerImage!!.setImageBitmap(it) })
            }
        }
        loadImageFromStorage()
    }

    companion object {

        const val TAG: String = "BannerHomeFragment"

        fun newInstance(): BannerHomeFragment {
            val args = Bundle()
            val fragment = BannerHomeFragment()
            fragment.arguments = args
            return fragment
        }
    }
}