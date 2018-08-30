package code.name.monkey.retromusic.ui.adapter.song;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemViewHolder;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;
import com.h6ah4i.android.widget.advrecyclerview.draggable.annotation.DraggableItemStateFlags;

import java.util.ArrayList;
import java.util.List;

import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.dialogs.RemoveFromPlaylistDialog;
import code.name.monkey.retromusic.interfaces.CabHolder;
import code.name.monkey.retromusic.model.PlaylistSong;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.util.ViewUtil;


@SuppressWarnings("unchecked")
public class OrderablePlaylistSongAdapter extends PlaylistSongAdapter
        implements DraggableItemAdapter<OrderablePlaylistSongAdapter.ViewHolder> {

    public static final String TAG = OrderablePlaylistSongAdapter.class.getSimpleName();

    private OnMoveItemListener onMoveItemListener;

    public OrderablePlaylistSongAdapter(@NonNull AppCompatActivity activity,
                                        @NonNull ArrayList<PlaylistSong> dataSet,
                                        @LayoutRes int itemLayoutRes,
                                        boolean usePalette,
                                        @Nullable CabHolder cabHolder,
                                        @Nullable OnMoveItemListener onMoveItemListener) {
        super(activity, (ArrayList<Song>) (List) dataSet, itemLayoutRes, usePalette, cabHolder);
        setMultiSelectMenuRes(R.menu.menu_playlists_songs_selection);
        this.onMoveItemListener = onMoveItemListener;
    }

    @Override
    protected SongAdapter.ViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public long getItemId(int position) {
        position--;
        if (position < 0) return -2;
        return ((ArrayList<PlaylistSong>) (List) dataSet).get(position).idInPlayList; // important!
    }

    @Override
    protected void onMultipleItemAction(@NonNull MenuItem menuItem, @NonNull ArrayList<Song> selection) {
        switch (menuItem.getItemId()) {
            case R.id.action_remove_from_playlist:
                RemoveFromPlaylistDialog.create((ArrayList<PlaylistSong>) (List) selection).show(activity.getSupportFragmentManager(), "ADD_PLAYLIST");
                return;
        }
        super.onMultipleItemAction(menuItem, selection);
    }

    @Override
    public boolean onCheckCanStartDrag(ViewHolder holder, int position, int x, int y) {
        return onMoveItemListener != null && position > 0 &&
                (ViewUtil.hitTest(holder.dragView, x, y) || ViewUtil.hitTest(holder.image, x, y));
    }

    @Override
    public ItemDraggableRange onGetItemDraggableRange(ViewHolder holder, int position) {
        return new ItemDraggableRange(1, dataSet.size());
    }

    @Override
    public void onMoveItem(int fromPosition, int toPosition) {
        if (onMoveItemListener != null && fromPosition != toPosition) {
            onMoveItemListener.onMoveItem(fromPosition - 1, toPosition - 1);
        }
    }

    @Override
    public boolean onCheckCanDrop(int draggingPosition, int dropPosition) {
        return dropPosition > 0;
    }

    @Override
    public void onItemDragStarted(int position) {
        notifyDataSetChanged();
    }

    @Override
    public void onItemDragFinished(int fromPosition, int toPosition, boolean result) {
        notifyDataSetChanged();
    }

    public interface OnMoveItemListener {
        void onMoveItem(int fromPosition, int toPosition);
    }

    public class ViewHolder extends PlaylistSongAdapter.ViewHolder implements DraggableItemViewHolder {
        @DraggableItemStateFlags
        private int mDragStateFlags;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            if (dragView != null) {
                if (onMoveItemListener != null) {
                    dragView.setVisibility(View.VISIBLE);
                } else {
                    dragView.setVisibility(View.GONE);
                }
            }
        }

        @Override
        protected int getSongMenuRes() {
            return R.menu.menu_item_playlist_song;
        }

        @Override
        protected boolean onSongMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_remove_from_playlist:
                    RemoveFromPlaylistDialog.create((PlaylistSong) getSong()).show(activity.getSupportFragmentManager(), "REMOVE_FROM_PLAYLIST");
                    return true;
            }
            return super.onSongMenuItemClick(item);
        }

        @Override
        @DraggableItemStateFlags
        public int getDragStateFlags() {
            return mDragStateFlags;
        }

        @Override
        public void setDragStateFlags(@DraggableItemStateFlags int flags) {
            mDragStateFlags = flags;
        }
    }
}
