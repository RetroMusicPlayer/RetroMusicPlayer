package code.name.monkey.retromusic.ui.adapter.song;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemViewHolder;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;
import com.h6ah4i.android.widget.advrecyclerview.draggable.annotation.DraggableItemStateFlags;

import java.util.ArrayList;

import androidx.annotation.ColorInt;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.helper.MusicPlayerRemote;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.util.MusicUtil;
import code.name.monkey.retromusic.util.ViewUtil;


public class PlayingQueueAdapter extends SongAdapter implements DraggableItemAdapter<PlayingQueueAdapter.ViewHolder> {

    private static final int HISTORY = 0;
    private static final int CURRENT = 1;
    private static final int UP_NEXT = 2;

    private int current;
    private int color = -1;

    public PlayingQueueAdapter(AppCompatActivity activity, ArrayList<Song> dataSet, int current,
                               @LayoutRes int itemLayoutRes) {
        super(activity, dataSet, itemLayoutRes, false, null);
        this.current = current;
    }

    public PlayingQueueAdapter(AppCompatActivity activity, ArrayList<Song> dataSet, int current,
                               @LayoutRes int itemLayoutRes, @ColorInt int color) {
        super(activity, dataSet, itemLayoutRes, false, null);
        this.current = current;
        this.color = color;
    }

    @Override
    protected SongAdapter.ViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongAdapter.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (holder.imageText != null) {
            holder.imageText.setText(String.valueOf(position - current));
        }
        if (holder.time != null) {
            holder.time.setText(MusicUtil.getReadableDurationString(getDataSet().get(position).duration));
        }
        if (holder.getItemViewType() == HISTORY || holder.getItemViewType() == CURRENT) {
            setAlpha(holder, 0.5f);
        }
        if (usePalette) {
            setColor(holder, Color.WHITE);
        }
    }

    private void setColor(SongAdapter.ViewHolder holder, int white) {

        if (holder.title != null) {
            holder.title.setTextColor(white);
            if (color != -1) {
                holder.title.setTextColor(color);
            }
        }
        if (holder.text != null) {
            holder.text.setTextColor(white);
        }
        if (holder.time != null) {
            holder.time.setTextColor(white);
        }
        if (holder.imageText != null) {
            holder.imageText.setTextColor(white);
        }
        if (holder.menu != null) {
            ((ImageView) holder.menu).setColorFilter(white, PorterDuff.Mode.SRC_IN);
        }
    }

    @Override
    public void usePalette(boolean color) {
        super.usePalette(color);
        usePalette = color;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position < current) {
            return HISTORY;
        } else if (position > current) {
            return UP_NEXT;
        }
        return CURRENT;
    }

    @Override
    protected void loadAlbumCover(Song song, SongAdapter.ViewHolder holder) {
        // We don't want to load it in this adapter
    }

    public void swapDataSet(ArrayList<Song> dataSet, int position) {
        this.dataSet = dataSet;
        current = position;
        notifyDataSetChanged();
    }

    public void setCurrent(int current) {
        this.current = current;
        notifyDataSetChanged();
    }

    private void setAlpha(SongAdapter.ViewHolder holder, float alpha) {
        if (holder.image != null) {
            holder.image.setAlpha(alpha);
        }
        if (holder.title != null) {
            holder.title.setAlpha(alpha);
        }
        if (holder.text != null) {
            holder.text.setAlpha(alpha);
        }
        if (holder.imageText != null) {
            holder.imageText.setAlpha(alpha);
        }
        if (holder.paletteColorContainer != null) {
            holder.paletteColorContainer.setAlpha(alpha);
        }
    }

    @Override
    public boolean onCheckCanStartDrag(ViewHolder holder, int position, int x, int y) {
        return ViewUtil.hitTest(holder.imageText, x, y) || (ViewUtil.hitTest(holder.dragView, x, y));
    }

    @Override
    public ItemDraggableRange onGetItemDraggableRange(ViewHolder holder, int position) {
        return null;
    }

    @Override
    public void onMoveItem(int fromPosition, int toPosition) {
        MusicPlayerRemote.moveSong(fromPosition, toPosition);
    }

    @Override
    public boolean onCheckCanDrop(int draggingPosition, int dropPosition) {
        return true;
    }

    @Override
    public void onItemDragStarted(int position) {
        notifyDataSetChanged();
    }

    @Override
    public void onItemDragFinished(int fromPosition, int toPosition, boolean result) {
        notifyDataSetChanged();
    }

    public class ViewHolder extends SongAdapter.ViewHolder implements DraggableItemViewHolder {

        @DraggableItemStateFlags
        private int mDragStateFlags;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            if (imageText != null) {
                imageText.setVisibility(View.VISIBLE);
            }
            if (dragView != null) {
                dragView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected int getSongMenuRes() {
            return R.menu.menu_item_playing_queue_song;
        }

        @Override
        protected boolean onSongMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_remove_from_playing_queue:
                    MusicPlayerRemote.removeFromQueue(getAdapterPosition());
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
