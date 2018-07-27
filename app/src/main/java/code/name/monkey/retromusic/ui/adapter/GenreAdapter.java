package code.name.monkey.retromusic.ui.adapter;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Locale;

import code.name.monkey.appthemehelper.util.ATHUtil;
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

    @Override
    public GenreAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mActivity).inflate(mItemLayoutRes, parent, false));
    }

    @Override
    public void onBindViewHolder(GenreAdapter.ViewHolder holder, int position) {
        Genre genre = mGenres.get(position);
        if (holder.title != null) {
            holder.title.setText(genre.name);
        }
        if (holder.text != null) {
            holder.text.setText(String.format(Locale.getDefault(), "%d %s", genre.songCount, genre.songCount > 1 ?
                    mActivity.getString(R.string.songs) :
                    mActivity.getString(R.string.song)));
        }
        if (holder.image != null) {
            holder.image.setImageResource(R.drawable.ic_recent_actors_white_24dp);
        }
        if (holder.shortSeparator != null) {
            holder.shortSeparator.setVisibility(View.VISIBLE);
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
            if (image != null) {
                int iconPadding = mActivity.getResources().getDimensionPixelSize(R.dimen.list_item_image_icon_padding);
                image.setPadding(iconPadding, iconPadding, iconPadding, iconPadding);
                image.setColorFilter(ATHUtil.resolveColor(mActivity, R.attr.iconColor), PorterDuff.Mode.SRC_IN);
            }
        }

        @Override
        public void onClick(View v) {
            super.onClick(v);
            Genre genre = mGenres.get(getAdapterPosition());
            NavigationUtil.goToGenre(mActivity, genre);
        }
    }
}
