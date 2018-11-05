package code.name.monkey.retromusic.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.model.Genre;
import code.name.monkey.retromusic.ui.adapter.base.MediaEntryViewHolder;
import code.name.monkey.retromusic.util.NavigationUtil;

/**
 * @author Hemanth S (h4h13).
 */

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.ViewHolder> {
    private ArrayList<Genre> mGenres = new ArrayList<>();
    private Activity mActivity;
    private int mItemLayoutRes;

    public GenreAdapter(@NonNull Activity activity, ArrayList<Genre> dataSet, int itemLayoutRes) {
        mActivity = activity;
        mGenres = dataSet;
        mItemLayoutRes = itemLayoutRes;
    }

    public ArrayList<Genre> getDataSet() {
        return mGenres;
    }

    @NonNull
    @Override
    public GenreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mActivity).inflate(mItemLayoutRes, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GenreAdapter.ViewHolder holder, int position) {
        Genre genre = mGenres.get(position);
        if (holder.title != null) {
            holder.title.setText(genre.name);
        }
        if (holder.text != null) {
            holder.text.setText(String.format(Locale.getDefault(), "%d %s", genre.songCount, genre.songCount > 1 ?
                    mActivity.getString(R.string.songs) :
                    mActivity.getString(R.string.song)));
        }

        if (holder.separator != null) {
            holder.separator.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mGenres.size();
    }

    public void swapDataSet(ArrayList<Genre> list) {
        mGenres = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends MediaEntryViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
            if (menu != null) {
                menu.setVisibility(View.GONE);
            }
            assert imageContainer != null;
            imageContainer.setVisibility(View.GONE);
        }

        @Override
        public void onClick(View v) {
            super.onClick(v);
            Genre genre = mGenres.get(getAdapterPosition());
            NavigationUtil.goToGenre(mActivity, genre);
        }
    }
}
