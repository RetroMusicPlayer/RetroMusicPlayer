package code.name.monkey.retromusic.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.model.Contributor;
import code.name.monkey.retromusic.ui.adapter.base.MediaEntryViewHolder;
import code.name.monkey.retromusic.views.NetworkImageView;

import static code.name.monkey.retromusic.util.RetroUtil.openUrl;

public class ContributorAdapter extends RecyclerView.Adapter<ContributorAdapter.ViewHolder> {
    private List<Contributor> contributors = new ArrayList<>();

    public ContributorAdapter(List<Contributor> contributors) {
        this.contributors = contributors;
    }

    public ContributorAdapter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contributor, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contributor contributor = contributors.get(position);
        holder.bindData(contributor);
    }

    @Override
    public int getItemCount() {
        return contributors.size();
    }

    public class ViewHolder extends MediaEntryViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        void bindData(Contributor contributor) {
            if (title != null) {
                title.setText(contributor.getName());
            }
            if (text != null) {
                text.setText(contributor.getSummary());
            }
            if (image instanceof NetworkImageView) {
                ((NetworkImageView) image).setImageUrl(contributor.getProfileImage());
            }
        }

        @Override
        public void onClick(View v) {
            super.onClick(v);
            openUrl((Activity) v.getContext(), contributors.get(getAdapterPosition()).getLink());
        }
    }
}
