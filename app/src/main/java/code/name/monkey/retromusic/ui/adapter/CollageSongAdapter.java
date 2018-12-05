package code.name.monkey.retromusic.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.appthemehelper.util.ColorUtil;
import code.name.monkey.appthemehelper.util.MaterialValueHelper;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.glide.SongGlideRequest;
import code.name.monkey.retromusic.helper.MusicPlayerRemote;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.ui.adapter.CollageSongAdapter.CollageSongViewHolder;
import code.name.monkey.retromusic.ui.adapter.base.MediaEntryViewHolder;

/**
 * @author Hemanth S (h4h13).
 */
public class CollageSongAdapter extends RecyclerView.Adapter<CollageSongViewHolder> {

    private Activity activity;
    private ArrayList<Song> dataSet;

    public CollageSongAdapter(Activity activity, ArrayList<Song> dataSet) {
        this.activity = activity;
        this.dataSet = dataSet;
    }

    @Override
    public void onBindViewHolder(@NonNull CollageSongViewHolder holder, int position) {
        holder.bindSongs();
        if (dataSet.size() > 8) {
            for (int i = 0; i < dataSet.subList(0, 8).size(); i++) {
                if (holder.imageViews != null) {
                    SongGlideRequest.Builder.from(Glide.with(activity), dataSet.get(i))
                            .checkIgnoreMediaStore(activity)
                            .build()
                            .into(holder.imageViews.get(i));
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    @NonNull
    @Override
    public CollageSongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CollageSongViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_collage, parent, false));
    }

    class CollageSongViewHolder extends MediaEntryViewHolder {

        @BindViews({R.id.image_2, R.id.image_3, R.id.image_4, R.id.image_5, R.id.image_6, R.id.image_7, R.id.image_8, R.id.image_9})
        @Nullable
        List<ImageView> imageViews;

        @BindView(R.id.image_1)
        TextView view;

        CollageSongViewHolder(View itemView) {
            super(itemView);
            //ButterKnife.bind(this, itemView);
            //Context context = itemView.getContext();
            //int color = ThemeStore.accentColor(context);
            //view.setOnClickListener(v -> MusicPlayerRemote.INSTANCE.openQueue(dataSet, 0, true));
            //view.setBackgroundColor(color);
            //view.setTextColor(MaterialValueHelper.getPrimaryTextColor(context, ColorUtil.isColorLight(color)));
        }

        void bindSongs() {
            if (imageViews != null) {
                for (int i = 0; i < imageViews.size(); i++) {
                    final int startPosition = i;
                    ImageView imageView = imageViews.get(i);
                    imageView.setOnClickListener(view -> {
                        MusicPlayerRemote.INSTANCE.playNext(dataSet.get(startPosition));
                    });
                }
            }
        }
    }
}
