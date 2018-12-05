package code.name.monkey.retromusic.service.daydream

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.service.dreams.DreamService
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.glide.RetroMusicColoredTarget
import code.name.monkey.retromusic.glide.SongGlideRequest
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.loaders.SongLoader
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.ui.adapter.base.MediaEntryViewHolder
import com.bumptech.glide.Glide
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * Created by hemanths on 25/09/17.
 */

class RetroMusicAlbums : DreamService() {
    @BindView(R.id.recycler_view)
    internal var mRecyclerView: RecyclerView? = null
    @BindView(R.id.title)
    internal var mTitle: TextView? = null
    @BindView(R.id.text)
    internal var mText: TextView? = null
    @BindView(R.id.title_container)
    internal var mViewGroup: ViewGroup? = null

    private val mBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            if (intent == null) {
                return
            }
            val artist = intent.getStringExtra("artist")
            val track = intent.getStringExtra("track")
            if (mViewGroup != null) {
                mViewGroup!!.visibility = View.VISIBLE
                TransitionManager.beginDelayedTransition(mViewGroup!!)
                if (mTitle != null) {
                    mTitle!!.text = track
                }
                if (mText != null) {
                    mText!!.text = artist
                }
            }

        }
    }
    private var unbinder: Unbinder? = null
    private var mDisposable: CompositeDisposable? = null

    private val playingQueue: Observable<ArrayList<Song>>
        get() = Observable.just(MusicPlayerRemote.playingQueue)

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        val view = LayoutInflater.from(this).inflate(R.layout.dream_service, null)
        setContentView(view)
        unbinder = ButterKnife.bind(this, view)

        mRecyclerView!!.itemAnimator = DefaultItemAnimator()
        val layoutManager = GridLayoutManager(this, 4, RecyclerView.VERTICAL, false)
        mRecyclerView!!.layoutManager = layoutManager


        mDisposable!!.add(playingQueue
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap {
                    return@flatMap Observable.create<ArrayList<Song>> { e ->
                        if (it.isEmpty()) {
                            e.onNext(SongLoader.getAllSongs(this@RetroMusicAlbums).blockingFirst())
                        } else {
                            e.onNext(it)
                        }
                        e.onComplete()
                    }
                }
                .subscribe { songs ->
                    if (songs.size > 0) {
                        val imagesAdapter = ImagesAdapter(songs)
                        mRecyclerView!!.adapter = imagesAdapter
                    }
                })

    }

    override fun onCreate() {
        super.onCreate()
        isInteractive = true
        isFullscreen = true

        mDisposable = CompositeDisposable()

        val iF = IntentFilter()
        iF.addAction("com.android.music.musicservicecommand")
        iF.addAction("com.android.music.metachanged")
        registerReceiver(mBroadcastReceiver, iF)

    }

    override fun onDestroy() {
        super.onDestroy()
        unbinder!!.unbind()
        mDisposable!!.clear()
        unregisterReceiver(mBroadcastReceiver)
    }

    internal inner class ImagesAdapter(private val list: ArrayList<Song>) : RecyclerView.Adapter<ImagesAdapter.ViewHolder>() {

        override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(applicationContext)
                    .inflate(R.layout.image, viewGroup, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, i: Int) {
            val song = list[i]
            SongGlideRequest.Builder.from(Glide.with(applicationContext), song)
                    .checkIgnoreMediaStore(applicationContext)
                    .generatePalette(applicationContext).build()
                    .override(400, 400)
                    .into(object : RetroMusicColoredTarget(holder.image!!) {
                        override fun onColorReady(color: Int) {

                        }
                    })

        }

        override fun getItemCount(): Int {
            return list.size
        }

        internal inner class ViewHolder(itemView: View) : MediaEntryViewHolder(itemView) {

            override fun onClick(v: View?) {
                super.onClick(v)
                MusicPlayerRemote.openQueue(list, adapterPosition, true)
            }
        }
    }
}
