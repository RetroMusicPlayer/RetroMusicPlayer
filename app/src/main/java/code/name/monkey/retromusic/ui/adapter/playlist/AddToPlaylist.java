package code.name.monkey.retromusic.ui.adapter.playlist;

import android.app.Activity;
import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import code.name.monkey.retromusic.model.Playlist;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.ui.adapter.base.MediaEntryViewHolder;
import code.name.monkey.retromusic.ui.adapter.playlist.AddToPlaylist.ViewHolder;
import code.name.monkey.retromusic.util.PlaylistsUtil;

/**
 * @author Hemanth S (h4h13).
 */
public class AddToPlaylist extends RecyclerView.Adapter<ViewHolder> {

    private Activity activity;
    private ArrayList<Playlist> playlists;
    private int itemLayoutRes;
    private ArrayList<Song> songs;
    private Dialog dialog;

    public AddToPlaylist(Activity activity,
                         ArrayList<Playlist> playlists, int itemLayoutRes,
                         ArrayList<Song> songs, Dialog dialog) {
        this.activity = activity;
        this.playlists = playlists;
        this.itemLayoutRes = itemLayoutRes;
        this.songs = songs;
        this.dialog = dialog;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(activity).inflate(itemLayoutRes, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Playlist playlist = playlists.get(position);
        if (holder.title != null) {
            holder.title.setText(playlist.name);
        }
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    public class ViewHolder extends MediaEntryViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View v) {
            super.onClick(v);
            PlaylistsUtil.addToPlaylist(activity, songs, playlists.get(getAdapterPosition()).id, true);
            dialog.dismiss();
        }
    }
}
