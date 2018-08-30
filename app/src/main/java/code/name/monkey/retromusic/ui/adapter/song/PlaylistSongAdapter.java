package code.name.monkey.retromusic.ui.adapter.song;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.interfaces.CabHolder;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.util.MusicUtil;
import code.name.monkey.retromusic.util.NavigationUtil;


public class PlaylistSongAdapter extends AbsOffsetSongAdapter {

    public static final String TAG = PlaylistSongAdapter.class.getSimpleName();

    public PlaylistSongAdapter(AppCompatActivity activity, @NonNull ArrayList<Song> dataSet, @LayoutRes int itemLayoutRes, boolean usePalette, @Nullable CabHolder cabHolder) {
        super(activity, dataSet, itemLayoutRes, usePalette, cabHolder, false);
        setMultiSelectMenuRes(R.menu.menu_cannot_delete_single_songs_playlist_songs_selection);
    }

    @Override
    protected SongAdapter.ViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SongAdapter.ViewHolder holder, int position) {
        if (holder.getItemViewType() == OFFSET_ITEM) {
            int textColor = ThemeStore.textColorSecondary(activity);
            if (holder.title != null) {
                holder.title.setText(MusicUtil.getPlaylistInfoString(activity, dataSet));
                holder.title.setTextColor(textColor);
            }


            if (holder.text != null) {
                holder.text.setVisibility(View.GONE);
            }
            if (holder.menu != null) {
                holder.menu.setVisibility(View.GONE);
            }
            if (holder.image != null) {
                final int padding = activity.getResources().getDimensionPixelSize(R.dimen.default_item_margin) / 2;
                holder.image.setPadding(padding, padding, padding, padding);
                holder.image.setColorFilter(textColor);
                holder.image.setImageResource(R.drawable.ic_timer_white_24dp);
            }
            if (holder.dragView != null) {
                holder.dragView.setVisibility(View.GONE);
            }
            if (holder.separator != null) {
                holder.separator.setVisibility(View.VISIBLE);
            }
            if (holder.shortSeparator != null) {
                holder.shortSeparator.setVisibility(View.GONE);
            }
        } else {
            super.onBindViewHolder(holder, position - 1);
        }
    }

    public class ViewHolder extends AbsOffsetSongAdapter.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

        }

        @Override
        protected int getSongMenuRes() {
            return R.menu.menu_item_cannot_delete_single_songs_playlist_song;
        }

        @Override
        protected boolean onSongMenuItemClick(MenuItem item) {
            if (item.getItemId() == R.id.action_go_to_album) {
                Pair[] albumPairs = new Pair[]{
                        Pair.create(image, activity.getString(R.string.transition_album_art))};
                NavigationUtil.goToAlbum(activity, dataSet.get(getAdapterPosition() - 1).albumId, albumPairs);
                return true;
            }
            return super.onSongMenuItemClick(item);
        }
    }
}