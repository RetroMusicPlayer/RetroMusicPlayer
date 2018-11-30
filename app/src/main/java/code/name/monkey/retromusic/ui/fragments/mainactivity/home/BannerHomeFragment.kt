package code.name.monkey.retromusic.ui.fragments.mainactivity.home

import android.graphics.Bitmap
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.OnClick
import code.name.monkey.appthemehelper.ThemeStore
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
import code.name.monkey.retromusic.views.CircularImageView
import code.name.monkey.retromusic.views.MetalRecyclerViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.abs_playlists.*
import java.io.File
import java.util.*

class BannerHomeFragment : AbsMainActivityFragment(), MainActivityFragmentCallbacks, HomeContract.HomeView {


    override fun onCreateView(inflater: LayoutInflater, viewGroup: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(if (PreferenceUtil.getInstance().isHomeBanner) R.layout.fragment_banner_home else R.layout.fragment_home, viewGroup, false)

        if (!PreferenceUtil.getInstance().isHomeBanner)
            setStatusbarColorAuto(view)

        imageView = view.findViewById(R.id.image)
        userImage = view.findViewById(R.id.user_image)
        recentArtistRV = view.findViewById(R.id.recycler_view)
        recentAlbumRV = view.findViewById(R.id.recent_album)
        topArtistRV = view.findViewById(R.id.top_artist)
        topAlbumRV = view.findViewById(R.id.top_album)
        recentArtistContainer = view.findViewById(R.id.recent_artist_container)
        recentAlbumsContainer = view.findViewById(R.id.recent_albums_container)
        topArtistContainer = view.findViewById(R.id.top_artist_container)
        topAlbumContainer = view.findViewById(R.id.top_albums_container)
        genresRecyclerView = view.findViewById(R.id.genres)
        genreContainer = view.findViewById(R.id.genre_container)
        contentContainer = view.findViewById(R.id.content_container)
        container = view.findViewById(R.id.container)
        suggestionsSongs = view.findViewById(R.id.suggestion_songs)
        suggestionsContainer = view.findViewById(R.id.suggestion_container)

       /* lastAdded.setOnClickListener {
            NavigationUtil.goToPlaylistNew(activity!!, LastAddedPlaylist(activity!!))
        }
        topPlayed.setOnClickListener {
            NavigationUtil.goToPlaylistNew(activity!!, MyTopTracksPlaylist(activity!!))
        }
        actionShuffle.setOnClickListener {
            MusicPlayerRemote.openAndShuffleQueue(SongLoader.getAllSongs(activity!!).blockingFirst(), true)
        }
        history.setOnClickListener {
            NavigationUtil.goToPlaylistNew(activity!!, HistoryPlaylist(activity!!))
        }*/
        userImage.setOnClickListener {
            NavigationUtil.goToUserInfo(activity!!)
        }
        return view
    }

    private var imageView: ImageView? = null
    private lateinit var userImage: CircularImageView
    private lateinit var recentAlbumRV: MetalRecyclerViewPager
    private lateinit var topAlbumRV: MetalRecyclerViewPager
    private lateinit var topArtistRV: RecyclerView
    private lateinit var genresRecyclerView: RecyclerView
    private lateinit var suggestionsSongs: RecyclerView
    private lateinit var recentArtistRV: RecyclerView
    private lateinit var recentArtistContainer: View
    private lateinit var recentAlbumsContainer: View
    private lateinit var topArtistContainer: View
    private lateinit var topAlbumContainer: View
    private lateinit var genreContainer: View
    private lateinit var container: View
    private lateinit var contentContainer: View
    private lateinit var suggestionsContainer: View
    private lateinit var homePresenter: HomePresenter
    val disposable: CompositeDisposable = CompositeDisposable()

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
        if (timeOfDay in 0..5) {
            images = resources.getStringArray(R.array.night)
        } else if (timeOfDay in 6..11) {
            images = resources.getStringArray(R.array.morning)
        } else if (timeOfDay in 12..15) {
            images = resources.getStringArray(R.array.after_noon)
        } else if (timeOfDay in 16..19) {
            images = resources.getStringArray(R.array.evening)
        } else if (timeOfDay in 20..23) {
            images = resources.getStringArray(R.array.night)
        }

        val day = images[Random().nextInt(images.size)]
        loadTimeImage(day)
    }


    private fun loadTimeImage(day: String) {

        if (imageView != null) {
            if (PreferenceUtil.getInstance().bannerImage.isEmpty()) {
                Glide.with(activity).load(day)
                        .asBitmap()
                        .placeholder(R.drawable.material_design_default)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(imageView!!)
            } else {
                loadBannerFromStorage()
            }
        }
    }

    private fun loadBannerFromStorage() {

        disposable.add(Compressor(context!!)
                .setQuality(100)
                .setCompressFormat(Bitmap.CompressFormat.WEBP)
                .compressToBitmapAsFlowable(
                        File(PreferenceUtil.getInstance().bannerImage, USER_BANNER))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { imageView!!.setImageBitmap(it) })
    }

    private fun loadImageFromStorage(imageView: ImageView) {

        disposable.add(Compressor(context!!)
                .setMaxHeight(300)
                .setMaxWidth(300)
                .setQuality(75)
                .setCompressFormat(Bitmap.CompressFormat.WEBP)
                .compressToBitmapAsFlowable(
                        File(PreferenceUtil.getInstance().profileImage, USER_PROFILE))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    imageView.setImageBitmap(it)
                }) {
                    imageView.setImageDrawable(ContextCompat
                            .getDrawable(context!!, R.drawable.ic_person_flat))
                })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homePresenter = HomePresenter(this)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        loadImageFromStorage(userImage)
        homePresenter.subscribe()
        getTimeOfTheDay()
    }

    private fun setupToolbar() {
        userImage.setOnClickListener { showMainMenu() }
        contentContainer.setBackgroundColor(ThemeStore.primaryColor(mainActivity))
    }

    @OnClick(R.id.searchIcon)
    internal fun search() {
        NavigationUtil.goToSearch(mainActivity)
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
        recentArtistRV.layoutManager = GridLayoutManager(mainActivity, 1, GridLayoutManager.HORIZONTAL, false)
        val artistAdapter = ArtistAdapter(mainActivity, artists, PreferenceUtil.getInstance().getHomeGridStyle(context!!), false, null)
        recentArtistRV.adapter = artistAdapter
    }

    override fun recentAlbum(albums: ArrayList<Album>) {
        recentAlbumsContainer.visibility = View.VISIBLE
        val artistAdapter = AlbumFullWithAdapter(mainActivity,
                displayMetrics)
        artistAdapter.swapData(albums)
        recentAlbumRV.adapter = artistAdapter
    }

    override fun topArtists(artists: ArrayList<Artist>) {
        topArtistContainer.visibility = View.VISIBLE
        topArtistRV.layoutManager = GridLayoutManager(mainActivity, 1, GridLayoutManager.HORIZONTAL, false)
        val artistAdapter = ArtistAdapter(mainActivity, artists, PreferenceUtil.getInstance().getHomeGridStyle(context!!), false, null)
        topArtistRV.adapter = artistAdapter

    }

    override fun topAlbums(albums: ArrayList<Album>) {
        topAlbumContainer.visibility = View.VISIBLE
        val artistAdapter = AlbumFullWithAdapter(mainActivity,
                displayMetrics)
        artistAdapter.swapData(albums)
        topAlbumRV.adapter = artistAdapter
    }

    override fun suggestions(songs: ArrayList<Song>) {
        if (!songs.isEmpty()) {
            suggestionsContainer.visibility = View.VISIBLE
            val artistAdapter = CollageSongAdapter(mainActivity, songs)
            suggestionsSongs.layoutManager = if (RetroUtil.isTablet()) GridLayoutManager(mainActivity, 2) else LinearLayoutManager(mainActivity)
            suggestionsSongs.adapter = artistAdapter
        }
    }

    override fun playlists(playlists: ArrayList<Playlist>) {

    }

    override fun geners(genres: ArrayList<Genre>) {
        genreContainer.visibility = View.VISIBLE
        genresRecyclerView.layoutManager = LinearLayoutManager(context)

        val genreAdapter = GenreAdapter(activity!!, genres, R.layout.item_list)
        genresRecyclerView.adapter = genreAdapter
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