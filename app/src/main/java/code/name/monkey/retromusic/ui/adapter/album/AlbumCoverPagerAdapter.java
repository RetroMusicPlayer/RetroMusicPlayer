package code.name.monkey.retromusic.ui.adapter.album;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.glide.RetroMusicColoredTarget;
import code.name.monkey.retromusic.glide.SongGlideRequest;
import code.name.monkey.retromusic.misc.CustomFragmentStatePagerAdapter;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.ui.activities.LyricsActivity;
import code.name.monkey.retromusic.ui.fragments.NowPlayingScreen;
import code.name.monkey.retromusic.util.PreferenceUtil;


public class AlbumCoverPagerAdapter extends CustomFragmentStatePagerAdapter {

    public static final String TAG = AlbumCoverPagerAdapter.class.getSimpleName();

    private ArrayList<Song> dataSet;

    private AlbumCoverFragment.ColorReceiver currentColorReceiver;
    private int currentColorReceiverPosition = -1;

    public AlbumCoverPagerAdapter(FragmentManager fm, ArrayList<Song> dataSet) {
        super(fm);
        this.dataSet = dataSet;
    }

    @Override
    public Fragment getItem(final int position) {
        return AlbumCoverFragment.newInstance(dataSet.get(position));
    }

    @Override
    public int getCount() {
        return dataSet.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object o = super.instantiateItem(container, position);
        if (currentColorReceiver != null && currentColorReceiverPosition == position) {
            receiveColor(currentColorReceiver, currentColorReceiverPosition);
        }
        return o;
    }

    /**
     * Only the latest passed {@link AlbumCoverFragment.ColorReceiver} is guaranteed to receive a
     * response
     */
    public void receiveColor(AlbumCoverFragment.ColorReceiver colorReceiver, int position) {
        AlbumCoverFragment fragment = (AlbumCoverFragment) getFragment(position);
        if (fragment != null) {
            currentColorReceiver = null;
            currentColorReceiverPosition = -1;
            fragment.receiveColor(colorReceiver, position);
        } else {
            currentColorReceiver = colorReceiver;
            currentColorReceiverPosition = position;
        }
    }

    public static class AlbumCoverFragment extends Fragment {

        private static final String SONG_ARG = "song";
        @BindView(R.id.player_image)
        ImageView albumCover;
        private Unbinder unbinder;
        private boolean isColorReady;
        private int color;
        private Song song;
        private ColorReceiver colorReceiver;
        private int request;

        public static AlbumCoverFragment newInstance(final Song song) {
            AlbumCoverFragment frag = new AlbumCoverFragment();
            final Bundle args = new Bundle();
            args.putParcelable(SONG_ARG, song);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                song = getArguments().getParcelable(SONG_ARG);
            }
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            int layout = getLayout();
            View view = inflater.inflate(layout, container, false);
            unbinder = ButterKnife.bind(this, view);
            return view;
        }

        private int getLayout() {
            int layout;
            //noinspection ConstantConditions
            switch (PreferenceUtil.getInstance(getContext()).getNowPlayingScreen()) {
                case BLUR_CARD:
                    layout = R.layout.fragment_album_card_cover;
                    break;
                case MATERIAL:
                    layout = R.layout.fragment_album_material_cover;
                    break;
                case PLAIN:
                case FLAT:
                    layout = R.layout.fragment_album_flat_cover;
                    break;
                case CARD:
                case FULL:
                case ADAPTIVE:
                    layout = R.layout.fragment_album_full_cover;
                    break;
                default:
                case NORMAL:
                    layout = R.layout.fragment_album_cover;
                    break;

            }
            if (PreferenceUtil.getInstance(getContext()).carouselEffect() &&
                    !(PreferenceUtil.getInstance(getContext()).getNowPlayingScreen()
                            == NowPlayingScreen.FULL)) {
                layout = R.layout.fragment_carousal_album_cover;
            }
            if (PreferenceUtil.getInstance(getContext()).circularAlbumArt()) {
                layout = R.layout.fragment_album_circle_cover;
            }
            return layout;
        }

        @Override
        public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            loadAlbumCover();
        }

        @OnClick(R.id.player_image)
        void showLyrics() {
            startActivity(new Intent(getContext(), LyricsActivity.class));
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            unbinder.unbind();
            colorReceiver = null;
        }

        private void loadAlbumCover() {
            SongGlideRequest.Builder.from(Glide.with(this), song)
                    .checkIgnoreMediaStore(getActivity())
                    .generatePalette(getActivity()).build()
                    .into(new RetroMusicColoredTarget(albumCover) {
                        @Override
                        public void onColorReady(int color) {
                            setColor(color);
                        }
                    });
        }

        private void setColor(int color) {
            this.color = color;
            isColorReady = true;
            if (colorReceiver != null) {
                colorReceiver.onColorReady(color, request);
                colorReceiver = null;
            }
        }

        public void receiveColor(ColorReceiver colorReceiver, int request) {
            if (isColorReady) {
                colorReceiver.onColorReady(color, request);
            } else {
                this.colorReceiver = colorReceiver;
                this.request = request;
            }
        }

        public interface ColorReceiver {

            void onColorReady(int color, int request);
        }
    }
}

