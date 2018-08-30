package code.name.monkey.retromusic.ui.adapter;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.glide.SongGlideRequest;
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

        if (dataSet.size() > 9) {
            for (int i = 0; i < dataSet.subList(0, 9).size(); i++) {
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
        return new CollageSongViewHolder(
                LayoutInflater.from(activity).inflate(R.layout.item_collage, parent, false));
    }

    class CollageSongViewHolder extends MediaEntryViewHolder {

        @BindViews({R.id.image_1, R.id.image_2, R.id.image_3, R.id.image_4, R.id.image_5, R.id.image_6,
                R.id.image_7, R.id.image_8, R.id.image_9})
        @Nullable
        List<ImageView> imageViews;

        CollageSongViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindSongs() {
            if (imageViews != null) {
                for (int i = 0; i < imageViews.size(); i++) {
                    final int startPosition = i;
                    ImageView imageView = imageViews.get(i);
                    imageView.setOnClickListener(view -> {

                    });
                }
            }
        }
    }
}
