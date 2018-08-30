package code.name.monkey.retromusic.ui.adapter.song;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.appthemehelper.util.TintHelper;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.util.MusicUtil;

/**
 * Created by Monkey D Luffy on 3/31/2016.
 */
public class SimpleSongAdapter extends SongAdapter {

    private int textColor;

    public SimpleSongAdapter(AppCompatActivity context, ArrayList<Song> songs, @LayoutRes int i) {
        super(context, songs, i, false, null);
        textColor = ThemeStore.textColorPrimary(context);
    }

    public void swapDataSet(ArrayList<Song> arrayList) {
        this.dataSet.clear();
        this.dataSet = arrayList;
        notifyDataSetChanged();
    }

    @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(activity).inflate(itemLayoutRes, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        int fixedTrackNumber = MusicUtil.getFixedTrackNumber(dataSet.get(position).trackNumber);

        if (holder.imageText != null) {
            holder.imageText.setText(fixedTrackNumber > 0 ? String.valueOf(fixedTrackNumber) : "-");
            holder.imageText.setTextColor(textColor);
        }

        if (holder.time != null) {
            holder.time.setText(MusicUtil.getReadableDurationString(dataSet.get(position).duration));
            holder.time.setTextColor(textColor);
        }
        if (holder.title != null) {
            holder.title.setTextColor(textColor);
        }
        if (holder.menu != null) {
            TintHelper.setTintAuto(holder.menu, textColor, false);
        }
    }

    public int getItemCount() {
        return dataSet.size();
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        notifyDataSetChanged();
    }
}
