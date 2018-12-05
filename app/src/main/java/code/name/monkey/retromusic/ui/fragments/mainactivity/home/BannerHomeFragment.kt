package code.name.monkey.retromusic.ui.fragments.mainactivity.home

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.TintHelper
import code.name.monkey.retromusic.Constants.USER_BANNER
import code.name.monkey.retromusic.Constants.USER_PROFILE
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.interfaces.MainActivityFragmentCallbacks
import code.name.monkey.retromusic.loaders.SongLoader
import code.name.monkey.retromusic.model.*
import code.name.monkey.retromusic.model.smartplaylist.HistoryPlaylist
import code.name.monkey.retromusic.model.smartplaylist.LastAddedPlaylist
import code.name.monkey.retromusic.model.smartplaylist.MyTopTracksPlaylist
import code.name.monkey.retromusic.mvp.contract.HomeContract
import code.name.monkey.retromusic.mvp.presenter.HomePresenter
import code.name.monkey.retromusic.ui.adapter.CollageSongAdapter
import code.name.monkey.retromusic.ui.adapter.GenreAdapter
import code.name.monkey.retromusic.ui.adapter.album.AlbumFullWithAdapter
import code.name.monkey.retromusic.ui.adapter.artist.ArtistAdapter
import code.name.monkey.retromusic.ui.fragments.base.AbsMainActivityFragment
import code.name.monkey.retromusic.util.Compressor
import code.name.monkey.retromusic.util.NavigationUtil
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.util.RetroUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_banner_home.*
import kotlinx.android.synthetic.main.home_section_content.*
import java.io.File
import java.util.*

class BannerHomeFragment : AbsMainActivityFragment(), MainActivityFragmentCallbacks, HomeContract.HomeView {

    val disposable: CompositeDisposable = CompositeDisposable()
    private lateinit var homePresenter: HomePresenter
    private lateinit var contentContainerView: View
    private lateinit var lastAdded: View
    private lateinit var topPlayed: View
    private lateinit var actionShuffle: View
    private lateinit var history: View
    private lateinit var userImage: ImageView
    private lateinit var toolbar: Toolbar

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainActivity.setBottomBarVisibility(View.GONE)
    }

    override fun onCreateView(inflater: LayoutInflater, viewGroup: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(if (PreferenceUtil.getInstance().isHomeBanner) R.layout.fragment_banner_home else R.layout.fragment_home, viewGroup, false)
    }


    private val displayMetrics: DisplayMetrics
        get() {
            val display = mainActivity.windowManager.defaultDisplay
            val metrics = DisplayMetrics()
            display.getMetrics(metrics)
            return metrics
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
                Glide.with(activity).load(day)
                        .asBitmap()
                        .placeholder(R.drawable.material_design_default)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
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
    }

    private fun loadImageFromStorage(imageView: ImageView) {
        disposable.add(Compressor(context!!)
                .setMaxHeight(300)
                .setMaxWidth(300)
                .setQuality(75)
                .setCompressFormat(Bitmap.CompressFormat.WEBP)
                .compressToBitmapAsFlowable(File(PreferenceUtil.getInstance().profileImage, USER_PROFILE))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    imageView.setImageBitmap(it)
                }) {
                    imageView.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_person_flat))
                })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homePresenter = HomePresenter(this)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity.setBottomBarVisibility(View.GONE)

        toolbar = view.findViewById(R.id.toolbar)

        if (!PreferenceUtil.getInstance().isHomeBanner)
            setStatusbarColorAuto(view)

        lastAdded = view.findViewById(R.id.lastAdded)
        lastAdded.setOnClickListener {
            NavigationUtil.goToPlaylistNew(activity!!, LastAddedPlaylist(activity!!))
        }

        topPlayed = view.findViewById(R.id.topPlayed)
        topPlayed.setOnClickListener {
            NavigationUtil.goToPlaylistNew(activity!!, MyTopTracksPlaylist(activity!!))
        }

        actionShuffle = view.findViewById(R.id.actionShuffle)
        actionShuffle.setOnClickListener {
            MusicPlayerRemote.openAndShuffleQueue(SongLoader.getAllSongs(activity!!).blockingFirst(), true)
        }

        history = view.findViewById(R.id.history)
        history.setOnClickListener {
            NavigationUtil.goToPlaylistNew(activity!!, HistoryPlaylist(activity!!))
        }

        userImage = view.findViewById(R.id.userImage)
        userImage.setOnClickListener { showMainMenu() }

        homePresenter = HomePresenter(this)

        contentContainerView = view.findViewById(R.id.contentContainer)
        contentContainerView.setBackgroundColor(ThemeStore.primaryColor(context!!))

        //bannerTitle.setTextColor(ThemeStore.textColorPrimary(context!!))

        setupToolbar()
        homePresenter.subscribe()

        loadImageFromStorage(userImage)
        getTimeOfTheDay()
    }

    private fun setupToolbar() {
        toolbar.navigationIcon = TintHelper.createTintedDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_menu_white_24dp), ThemeStore.textColorPrimary(context!!))
        mainActivity.title = null
        mainActivity.setSupportActionBar(toolbar)
        toolbar.setBackgroundColor(Color.TRANSPARENT)
    }

    override fun handleBackPress(): Boolean {
        return false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposable.clear()
        homePresenter.unsubscribe()
    }

    override fun loading() {

    }

    override fun showEmptyView() {

    }

    override fun completed() {

    }

    override fun showData(homes: ArrayList<Any>) {
        //homeAdapter.swapDataSet(homes);
    }

    override fun recentArtist(artists: ArrayList<Artist>) {
        recentArtistContainer.visibility = View.VISIBLE
        recentArtist.apply {
            val artistAdapter = ArtistAdapter(mainActivity, artists, PreferenceUtil.getInstance().getHomeGridStyle(context!!), false, null)
            layoutManager = GridLayoutManager(mainActivity, 1, GridLayoutManager.HORIZONTAL, false)
            adapter = artistAdapter
        }
    }

    override fun recentAlbum(albums: ArrayList<Album>) {
        recentAlbumsContainer.visibility = View.VISIBLE
        val artistAdapter = AlbumFullWithAdapter(mainActivity, displayMetrics)
        artistAdapter.swapData(albums)
        recentAlbum.adapter = artistAdapter
    }

    override fun topArtists(artists: ArrayList<Artist>) {
        topArtistContainer.visibility = View.VISIBLE
        topArtist.apply {
            layoutManager = GridLayoutManager(mainActivity, 1, GridLayoutManager.HORIZONTAL, false)
            val artistAdapter = ArtistAdapter(mainActivity, artists, PreferenceUtil.getInstance().getHomeGridStyle(context!!), false, null)
            adapter = artistAdapter
        }

    }

    override fun topAlbums(albums: ArrayList<Album>) {
        topAlbumsContainer.visibility = View.VISIBLE
        val artistAdapter = AlbumFullWithAdapter(mainActivity,
                displayMetrics)
        artistAdapter.swapData(albums)
        topAlbum.adapter = artistAdapter
    }

    override fun suggestions(songs: ArrayList<Song>) {
        if (!songs.isEmpty()) {
            suggestionContainer.visibility = View.VISIBLE
            val artistAdapter = CollageSongAdapter(mainActivity, songs)
            suggestionSongs.apply {
                layoutManager = if (RetroUtil.isTablet()) GridLayoutManager(mainActivity, 2) else LinearLayoutManager(mainActivity)
                adapter = artistAdapter
            }
        }
    }

    override fun playlists(playlists: ArrayList<Playlist>) {

    }

    override fun geners(genres: ArrayList<Genre>) {
        genreContainer.visibility = View.VISIBLE
        genresRecyclerView.apply {
            val genreAdapter = GenreAdapter(activity!!, genres, R.layout.item_list)
            layoutManager = LinearLayoutManager(context)
            adapter = genreAdapter
        }
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