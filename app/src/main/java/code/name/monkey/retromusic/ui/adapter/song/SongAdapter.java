package code.name.monkey.retromusic.ui.adapter.song;

import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialcab.MaterialCab;
import com.bumptech.glide.Glide;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.ArrayList;

import code.name.monkey.appthemehelper.util.ColorUtil;
import code.name.monkey.appthemehelper.util.MaterialValueHelper;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.glide.RetroMusicColoredTarget;
import code.name.monkey.retromusic.glide.SongGlideRequest;
import code.name.monkey.retromusic.helper.MusicPlayerRemote;
import code.name.monkey.retromusic.helper.SortOrder;
import code.name.monkey.retromusic.helper.menu.SongMenuHelper;
import code.name.monkey.retromusic.helper.menu.SongsMenuHelper;
import code.name.monkey.retromusic.interfaces.CabHolder;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.ui.adapter.base.AbsMultiSelectAdapter;
import code.name.monkey.retromusic.ui.adapter.base.MediaEntryViewHolder;
import code.name.monkey.retromusic.util.MusicUtil;
import code.name.monkey.retromusic.util.NavigationUtil;
import code.name.monkey.retromusic.util.PreferenceUtil;

/**
 * Created by hemanths on 13/08/17.
 */

public class SongAdapter extends AbsMultiSelectAdapter<SongAdapter.ViewHolder, Song>
        implements MaterialCab.Callback, FastScrollRecyclerView.SectionedAdapter {

    public static final String TAG = SongAdapter.class.getSimpleName();

    protected final AppCompatActivity activity;
    protected ArrayList<Song> dataSet;

    protected int itemLayoutRes;

    protected boolean usePalette = false;
    private boolean showSectionName = true;


    public SongAdapter(AppCompatActivity activity, ArrayList<Song> dataSet,
                       @LayoutRes int itemLayoutRes, boolean usePalette, @Nullable CabHolder cabHolder) {
        this(activity, dataSet, itemLayoutRes, usePalette, cabHolder, true);
    }

    public SongAdapter(AppCompatActivity activity, ArrayList<Song> dataSet,
                       @LayoutRes int itemLayoutRes, boolean usePalette, @Nullable CabHolder cabHolder,
                       boolean showSectionName) {
        super(activity, cabHolder, R.menu.menu_media_selection);
        this.activity = activity;
        this.dataSet = dataSet;
        this.itemLayoutRes = itemLayoutRes;
        this.usePalette = usePalette;
        this.showSectionName = showSectionName;
        setHasStableIds(true);
    }

    public void swapDataSet(ArrayList<Song> dataSet) {
        this.dataSet = dataSet;
        notifyDataSetChanged();
    }

    public void usePalette(boolean usePalette) {
        this.usePalette = usePalette;
        notifyDataSetChanged();
    }

    public ArrayList<Song> getDataSet() {
        return dataSet;
    }

    @Override
    public long getItemId(int position) {
        return dataSet.get(position).id;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(itemLayoutRes, parent, false);
        return createViewHolder(view);
    }

    protected ViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Song song = dataSet.get(position);

        boolean isChecked = isChecked(song);
        holder.itemView.setActivated(isChecked);

        if (holder.getAdapterPosition() == getItemCount() - 1) {
            if (holder.shortSeparator != null) {
                holder.shortSeparator.setVisibility(View.GONE);
            }
        } else {
            if (holder.shortSeparator != null) {
                holder.shortSeparator.setVisibility(View.VISIBLE);
            }
        }

        if (holder.title != null) {
            holder.title.setText(getSongTitle(song));
        }
        if (holder.text != null) {
            holder.text.setText(getSongText(song));
        }

        loadAlbumCover(song, holder);

    }

    private void setColors(int color, ViewHolder holder) {
        if (holder.paletteColorContainer != null) {
            holder.paletteColorContainer.setBackgroundColor(color);
            if (holder.title != null) {
                holder.title.setTextColor(
                        MaterialValueHelper.getPrimaryTextColor(activity, ColorUtil.isColorLight(color)));
            }
            if (holder.text != null) {
                holder.text.setTextColor(
                        MaterialValueHelper.getSecondaryTextColor(activity, ColorUtil.isColorLight(color)));
            }
        }
    }

    protected void loadAlbumCover(Song song, final ViewHolder holder) {
        if (holder.image == null) {
            return;
        }
        SongGlideRequest.Builder.from(Glide.with(activity), song)
                .checkIgnoreMediaStore(activity)
                .generatePalette(activity).build()
                .into(new RetroMusicColoredTarget(holder.image) {
                    @Override
                    public void onLoadCleared(Drawable placeholder) {
                        super.onLoadCleared(placeholder);
                        setColors(getDefaultFooterColor(), holder);
                    }

                    @Override
                    public void onColorReady(int color) {
                        setColors(color, holder);
                    }
                });
    }

    private String getSongTitle(Song song) {
        return song.title;
    }

    private String getSongText(Song song) {
        return song.artistName;
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    protected Song getIdentifier(int position) {
        return dataSet.get(position);
    }

    @Override
    protected String getName(Song song) {
        return song.title;
    }

    @Override
    protected void onMultipleItemAction(@NonNull MenuItem menuItem,
                                        @NonNull ArrayList<Song> selection) {
        SongsMenuHelper.handleMenuClick(activity, selection, menuItem.getItemId());
    }

    @NonNull
    @Override
    public String getSectionName(int position) {
        if (!showSectionName) {
            return "";
        }
        @Nullable String sectionName = null;
        switch (PreferenceUtil.getInstance(activity).getSongSortOrder()) {
            case SortOrder.SongSortOrder.SONG_A_Z:
            case SortOrder.SongSortOrder.SONG_Z_A:
                sectionName = dataSet.get(position).title;
                break;
            case SortOrder.SongSortOrder.SONG_ALBUM:
                sectionName = dataSet.get(position).albumName;
                break;
            case SortOrder.SongSortOrder.SONG_ARTIST:
                sectionName = dataSet.get(position).artistName;
                break;
            case SortOrder.SongSortOrder.SONG_YEAR:
                return MusicUtil.getYearString(dataSet.get(position).year);
        }

        return MusicUtil.getSectionName(sectionName);
    }

    public class ViewHolder extends MediaEntryViewHolder {

        int DEFAULT_MENU_RES = SongMenuHelper.MENU_RES;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            setImageTransitionName(activity.getString(R.string.transition_album_art));

            /*if (mItemView != null) {
                mItemView.setOnMenuItemClickListener(new ListItemView.OnMenuItemClickListener() {
                    @Override
                    public void onActionMenuItemSelected(MenuItem item) {
                        SongMenuHelper.handleMenuClick(activity, dataSet.get(getAdapterPosition()), item.getItemId());
                    }
                });
            }*/

            if (menu == null) {
                return;
            }
            menu.setOnClickListener(new SongMenuHelper.OnClickSongMenu(activity) {
                @Override
                public Song getSong() {
                    return ViewHolder.this.getSong();
                }

                @Override
                public int getMenuRes() {
                    return getSongMenuRes();
                }

                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    return onSongMenuItemClick(item) || super.onMenuItemClick(item);
                }
            });
        }

        protected Song getSong() {
            return dataSet.get(getAdapterPosition());
        }

        protected int getSongMenuRes() {
            return DEFAULT_MENU_RES;
        }

        protected boolean onSongMenuItemClick(MenuItem item) {
            if (image != null && image.getVisibility() == View.VISIBLE) {
                switch (item.getItemId()) {
                    case R.id.action_go_to_album:
                        Pair[] albumPairs = new Pair[]{
                                Pair.create(imageContainer,
                                        activity.getResources().getString(R.string.transition_album_art))
                        };
                        NavigationUtil.goToAlbum(activity, getSong().albumId, albumPairs);
                        return true;
                }
            }
            return false;
        }

        @Override
        public void onClick(View v) {
            if (isInQuickSelectMode()) {
                toggleChecked(getAdapterPosition());
            } else {
                MusicPlayerRemote.openQueue(dataSet, getAdapterPosition(), true);
            }
        }

        @Override
        public boolean onLongClick(View view) {
            return toggleChecked(getAdapterPosition());
        }
    }
}
