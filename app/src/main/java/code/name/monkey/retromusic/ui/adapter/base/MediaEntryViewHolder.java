package code.name.monkey.retromusic.ui.adapter.base;

import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.retromusic.R;


public class MediaEntryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    @Nullable
    @BindView(R.id.image)
    public ImageView image;
    @Nullable
    @BindView(R.id.image_text)
    public TextView imageText;
    @Nullable
    @BindView(R.id.title)
    public TextView title;
    @Nullable
    @BindView(R.id.text)
    public TextView text;
    @Nullable
    @BindView(R.id.image_container)
    public ViewGroup imageContainer;
    @Nullable
    @BindView(R.id.image_container_card)
    public CardView imageContainerCard;
    @Nullable
    @BindView(R.id.menu)
    public View menu;
    @Nullable
    @BindView(R.id.separator)
    public View separator;
    @Nullable
    @BindView(R.id.short_separator)
    public View shortSeparator;
    @Nullable
    @BindView(R.id.drag_view)
    public View dragView;
    @Nullable
    @BindView(R.id.palette_color_container)
    public View paletteColorContainer;
    @BindView(R.id.time)
    @Nullable
    public TextView time;
    @BindView(R.id.recycler_view)
    @Nullable
    public RecyclerView recyclerView;
    @BindView(R.id.play_songs)
    @Nullable
    public ImageButton playSongs;
    @BindView(R.id.mask)
    @Nullable
    public View mask;
    @BindView(R.id.image_text_container)
    @Nullable
    public CardView imageTextContainer;


    public MediaEntryViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);

        if (imageTextContainer != null) {
            imageTextContainer.setCardBackgroundColor(ThemeStore.primaryColor(itemView.getContext()));
        }
        if (imageContainerCard != null) {
            imageContainerCard.setCardBackgroundColor(ThemeStore.primaryColor(itemView.getContext()));
        }
    }

    protected void setImageTransitionName(@NonNull String transitionName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && image != null) {
            image.setTransitionName(transitionName);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onClick(View v) {
    }
}
