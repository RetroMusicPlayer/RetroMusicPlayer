package code.name.monkey.retromusic.ui.adapter.home;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.helper.MusicPlayerRemote;
import code.name.monkey.retromusic.loaders.SongLoader;
import code.name.monkey.retromusic.model.Album;
import code.name.monkey.retromusic.model.Artist;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.model.smartplaylist.HistoryPlaylist;
import code.name.monkey.retromusic.model.smartplaylist.LastAddedPlaylist;
import code.name.monkey.retromusic.model.smartplaylist.MyTopTracksPlaylist;
import code.name.monkey.retromusic.ui.adapter.album.AlbumAdapter;
import code.name.monkey.retromusic.ui.adapter.artist.ArtistAdapter;
import code.name.monkey.retromusic.ui.adapter.base.MediaEntryViewHolder;
import code.name.monkey.retromusic.ui.adapter.song.SongAdapter;
import code.name.monkey.retromusic.util.NavigationUtil;
import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private static final int SUB_HEADER = 0;
  private static final int ABS_PLAYLITS = 1;
  private static final int DATA = 2;
  private ArrayList<Object> dataSet = new ArrayList<>();
  private AppCompatActivity activity;

  public HomeAdapter(@NonNull AppCompatActivity activity) {
    this.activity = activity;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
    switch (i) {

      case ABS_PLAYLITS:
        return new ViewHolder(LayoutInflater.from(activity)
            .inflate(R.layout.abs_playlists, viewGroup, false));
      default:
      case DATA:
        return new ViewHolder(LayoutInflater.from(activity)
            .inflate(R.layout.recycler_view_sec, viewGroup, false));
      case SUB_HEADER:
        return new ViewHolder(LayoutInflater.from(activity)
            .inflate(R.layout.sub_header, viewGroup, false));
    }
  }

  @Override
  public int getItemViewType(int position) {
    if (dataSet.get(position) instanceof String) {
      return SUB_HEADER;
    } else if (dataSet.get(position) instanceof Integer) {
      return ABS_PLAYLITS;
    } else if (dataSet.get(position) instanceof ArrayList) {
      return DATA;
    }
    return super.getItemViewType(position);
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {
    ViewHolder viewholder = (ViewHolder) holder;
    switch (getItemViewType(i)) {
      case ABS_PLAYLITS:
        bindAbsActions(viewholder);
        break;
      case SUB_HEADER:
        String title = (String) dataSet.get(i);
        if (viewholder.title != null) {
          viewholder.title.setText(title);
        }
        break;
      case DATA:
        parseAllSections(i, viewholder);
        break;
    }
  }

  private void bindAbsActions(ViewHolder viewholder) {


    if (viewholder.shuffle != null) {
      viewholder.shuffle.setOnClickListener(view -> MusicPlayerRemote
          .openAndShuffleQueue(SongLoader.getAllSongs(activity).blockingFirst(), true));
    }
        /*if (viewholder.search != null) {
            viewholder.search.setBackgroundTintList(ColorStateList.valueOf(ColorUtil.withAlpha(ThemeStore.textColorPrimary(activity), 0.2f)));
            viewholder.search.setOnClickListener(view -> {
                activity.startActivity(new Intent(activity, SearchActivity.class));
            });
        }*/
  }

  @SuppressWarnings("unchecked")
  private void parseAllSections(int i, ViewHolder viewholder) {
    if (viewholder.recyclerView != null) {
      ArrayList arrayList = (ArrayList) dataSet.get(i);
      if (arrayList.isEmpty()) {
        return;
      }
      Object something = arrayList.get(0);
      if (something instanceof Artist) {
        layoutManager(viewholder);
        viewholder.recyclerView.setAdapter(
            new ArtistAdapter(activity, (ArrayList<Artist>) arrayList, R.layout.item_artist, false,
                null));
      } else if (something instanceof Album) {
        layoutManager(viewholder);
        viewholder.recyclerView.setItemAnimator(new DefaultItemAnimator());
        viewholder.recyclerView.setAdapter(
            new AlbumAdapter(activity, (ArrayList<Album>) arrayList, R.layout.item_image, false,
                null));
      } else if (something instanceof Song) {
        GridLayoutManager layoutManager = new GridLayoutManager(activity, 1,
            LinearLayoutManager.HORIZONTAL, false);
        viewholder.recyclerView.setLayoutManager(layoutManager);
        viewholder.recyclerView.setAdapter(
            new SongAdapter(activity, (ArrayList<Song>) arrayList, R.layout.item_image, false,
                null));
      }
    }
  }

  private void layoutManager(ViewHolder viewholder) {
    if (viewholder.recyclerView != null) {
      viewholder.recyclerView.setLayoutManager(
          new GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false));
      viewholder.recyclerView.setItemAnimator(new DefaultItemAnimator());
    }
  }


  @Override
  public int getItemCount() {
    return dataSet.size();
  }

  public void swapDataSet(@NonNull ArrayList<Object> data) {
    dataSet = data;
    notifyDataSetChanged();
  }

  public ArrayList<Object> getDataset() {
    return dataSet;
  }

  public class ViewHolder extends MediaEntryViewHolder {


    @BindView(R.id.action_shuffle)
    @Nullable
    View shuffle;

    public ViewHolder(View itemView) {
      super(itemView);
    }
  }
}
