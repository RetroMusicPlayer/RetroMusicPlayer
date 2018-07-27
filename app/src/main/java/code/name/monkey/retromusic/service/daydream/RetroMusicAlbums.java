package code.name.monkey.retromusic.service.daydream;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.service.dreams.DreamService;
import android.support.transition.TransitionManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import code.name.monkey.retromusic.loaders.SongLoader;
import code.name.monkey.retromusic.model.Song;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.glide.RetroMusicColoredTarget;
import code.name.monkey.retromusic.glide.SongGlideRequest;
import code.name.monkey.retromusic.helper.MusicPlayerRemote;
import code.name.monkey.retromusic.ui.adapter.base.MediaEntryViewHolder;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hemanths on 25/09/17.
 */

public class RetroMusicAlbums extends DreamService {
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.text)
    TextView mText;
    @BindView(R.id.title_container)
    ViewGroup mViewGroup;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                return;
            }
            String artist = intent.getStringExtra("artist");
            String track = intent.getStringExtra("track");
            if (mViewGroup != null) {
                mViewGroup.setVisibility(View.VISIBLE);
                TransitionManager.beginDelayedTransition(mViewGroup);
                if (mTitle != null) {
                    mTitle.setText(track);
                }
                if (mText != null) {
                    mText.setText(artist);
                }
            }

        }
    };
    private Unbinder unbinder;
    private CompositeDisposable mDisposable;

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        View view = LayoutInflater.from(this).inflate(R.layout.dream_service, null);
        setContentView(view);
        unbinder = ButterKnife.bind(this, view);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);


        mDisposable.add(getPlayingQueue()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap((Function<ArrayList<Song>, ObservableSource<ArrayList<Song>>>) songs -> Observable.create(e -> {
                    if (songs.isEmpty()) {
                        e.onNext(SongLoader.getAllSongs(RetroMusicAlbums.this).blockingFirst());
                    } else {
                        e.onNext(songs);
                    }
                    e.onComplete();
                }))
                .subscribe(songs -> {
                    if (songs.size() > 0) {
                        ImagesAdapter imagesAdapter = new ImagesAdapter(songs);
                        mRecyclerView.setAdapter(imagesAdapter);
                    }
                }));

    }

    @Override
    public void onCreate() {
        super.onCreate();
        setInteractive(true);
        setFullscreen(true);

        mDisposable = new CompositeDisposable();

        IntentFilter iF = new IntentFilter();
        iF.addAction("com.android.music.musicservicecommand");
        iF.addAction("com.android.music.metachanged");
        registerReceiver(mBroadcastReceiver, iF);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        mDisposable.clear();
        unregisterReceiver(mBroadcastReceiver);
    }

    private Observable<ArrayList<Song>> getPlayingQueue() {
        return Observable.just(MusicPlayerRemote.getPlayingQueue());
    }

    class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ViewHolder> {

        private final ArrayList<Song> list;

        public ImagesAdapter(ArrayList<Song> songs) {
            this.list = songs;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.image, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int i) {
            Song song = list.get(i);
            SongGlideRequest.Builder.from(Glide.with(getApplicationContext()), song)
                    .checkIgnoreMediaStore(getApplicationContext())
                    .generatePalette(getApplicationContext()).build()
                    .override(400, 400)
                    .into(new RetroMusicColoredTarget(holder.image) {

                        @Override
                        public void onColorReady(int color) {

                        }
                    });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends MediaEntryViewHolder {

            public ViewHolder(View itemView) {
                super(itemView);
            }

            @Override
            public void onClick(View v) {
                super.onClick(v);
                MusicPlayerRemote.openQueue(list, getAdapterPosition(), true);
            }
        }
    }
}
