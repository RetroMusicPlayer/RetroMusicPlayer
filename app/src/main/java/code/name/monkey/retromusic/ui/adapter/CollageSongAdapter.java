package code.name.monkey.retromusic.ui.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.glide.SongGlideRequest;
import code.name.monkey.retromusic.model.Album;
import code.name.monkey.retromusic.ui.adapter.CollageSongAdapter.CollageSongViewHolder;
import code.name.monkey.retromusic.ui.adapter.base.MediaEntryViewHolder;
import code.name.monkey.retromusic.util.NavigationUtil;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Hemanth S (h4h13).
 */
public class CollageSongAdapter extends RecyclerView.Adapter<CollageSongViewHolder> {

  private AppCompatActivity activity;
  private ArrayList<Album> dataSet;
  private int itemLayoutRes;

  public CollageSongAdapter(AppCompatActivity activity, ArrayList<Album> dataSet,
      int itemLayoutRes) {
    this.activity = activity;
    this.dataSet = dataSet;
    this.itemLayoutRes = itemLayoutRes;
  }

  @Override
  public void onBindViewHolder(@NonNull CollageSongViewHolder holder, int position) {
    ArrayList<Album> albums = dataSet;

    holder.bindSongs(albums);

    if (albums.size() > 9) {
      for (int i = 0; i < albums.subList(0, 9).size(); i++) {
        if (holder.imageViews != null) {
          SongGlideRequest.Builder.from(Glide.with(activity), albums.get(i).safeGetFirstSong())
              .checkIgnoreMediaStore(activity).build().into(holder.imageViews.get(i));
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
  public CollageSongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new CollageSongViewHolder(
        LayoutInflater.from(activity).inflate(itemLayoutRes, parent, false));
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

    @Override
    public void onClick(View v) {
      super.onClick(v);
      NavigationUtil.goToAlbum(activity, dataSet.get(getAdapterPosition() + 1).getId());
    }

    void bindSongs(ArrayList<Album> albums) {
      if (imageViews != null) {
        for (int i = 0; i < imageViews.size(); i++) {
          final int startPosition = i;
          ImageView imageView = imageViews.get(i);
          imageView.setOnClickListener(
              v -> NavigationUtil.goToAlbum(activity, albums.get(startPosition).getId()));
        }
      }
    }
  }
}
