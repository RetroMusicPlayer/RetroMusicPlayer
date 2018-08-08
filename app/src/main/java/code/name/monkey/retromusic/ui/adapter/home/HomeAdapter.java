package code.name.monkey.retromusic.ui.adapter.home;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.model.Album;
import code.name.monkey.retromusic.model.Artist;
import code.name.monkey.retromusic.model.Genre;
import code.name.monkey.retromusic.model.Home;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.ui.adapter.CollageSongAdapter;
import code.name.monkey.retromusic.ui.adapter.GenreAdapter;
import code.name.monkey.retromusic.ui.adapter.album.AlbumAdapter;
import code.name.monkey.retromusic.ui.adapter.artist.ArtistAdapter;
import code.name.monkey.retromusic.ui.adapter.base.MediaEntryViewHolder;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private static final int ALBUMS = 0;
    private static final int ARTISTS = 1;
    private static final int GENERS = 2;
    private static final int SUGGESTIONS = 3;
    private Activity activity;
    private ArrayList<Home> sections = new ArrayList<>();


    public HomeAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.home_section_recycler_view,
                parent, false));
    }

    @Override
    public int getItemViewType(int position) {
        Home home = sections.get(position);
        ArrayList arrayList = home.getList();
        if (arrayList.get(0) instanceof Album) {
            return ALBUMS;
        } else if (arrayList.get(0) instanceof Artist) {
            return ARTISTS;
        } else if (arrayList.get(0) instanceof Genre) {
            return GENERS;
        } else {
            return SUGGESTIONS;
        }
    }

    private DisplayMetrics getDisplayMetrics() {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return metrics;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Home home = sections.get(position);

        if (holder.title != null) {
            holder.title.setText(home.getSectionTitle());
        }
        ArrayList arrayList = home.getList();
        if (arrayList.get(0) instanceof Album) {
            AlbumAdapter albumAdapter = new AlbumAdapter((AppCompatActivity) activity,
                    (ArrayList<Album>) arrayList, R.layout.item_image);
            if (holder.recyclerView != null) {
                holder.recyclerView.setLayoutManager(new GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false));
                holder.recyclerView.setAdapter(albumAdapter);
            }
        } else if (arrayList.get(0) instanceof Artist) {
            ArtistAdapter artistAdapter = new ArtistAdapter((AppCompatActivity) activity, (ArrayList<Artist>) arrayList, R.layout.item_artist);
            GridLayoutManager layoutManager = new GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false);
            if (holder.recyclerView != null) {
                holder.recyclerView.setLayoutManager(layoutManager);
                holder.recyclerView.setAdapter(artistAdapter);
            }
        } else if (arrayList.get(0) instanceof Genre) {
            GenreAdapter genreAdapter = new GenreAdapter(activity, (ArrayList<Genre>) arrayList, R.layout.item_list);
            if (holder.recyclerView != null) {
                holder.recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                holder.recyclerView.setAdapter(genreAdapter);
            }
        } else if (arrayList.get(0) instanceof Song) {
            CollageSongAdapter collageSongAdapter = new CollageSongAdapter(activity, (ArrayList<Song>) arrayList);
            if (holder.recyclerView != null) {
                holder.recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                holder.recyclerView.setAdapter(collageSongAdapter);
            }
        }
    }

    @Override
    public int getItemCount() {
        return sections.size();
    }

    public void swapData(ArrayList<Home> sections) {
        this.sections = sections;
        notifyDataSetChanged();
    }

    public class ViewHolder extends MediaEntryViewHolder {


        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
