package code.name.monkey.retromusic.adapter.base;

import android.graphics.Color;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.google.android.material.card.MaterialCardView;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableSwipeableItemViewHolder;

import code.name.monkey.retromusic.R;

public class MediaEntryViewHolder extends AbstractDraggableSwipeableItemViewHolder
        implements View.OnLongClickListener, View.OnClickListener {

    @Nullable
    public View dragView;

    @Nullable
    public View dummyContainer;

    @Nullable
    public ImageView image;

    @Nullable
    public MaterialCardView imageContainerCard;

    @Nullable
    public FrameLayout imageContainer;

    @Nullable
    public TextView imageText;

    @Nullable
    public MaterialCardView imageTextContainer;

    @Nullable
    public View mask;

    @Nullable
    public AppCompatImageView menu;

    @Nullable
    public View paletteColorContainer;

    @Nullable
    public TextView text;

    @Nullable
    public TextView text2;

    @Nullable
    public TextView time;

    @Nullable
    public TextView title;

    public MediaEntryViewHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.title);
        text = itemView.findViewById(R.id.text);
        text2 = itemView.findViewById(R.id.text2);

        image = itemView.findViewById(R.id.image);
        time = itemView.findViewById(R.id.time);

        imageText = itemView.findViewById(R.id.imageText);
        imageTextContainer = itemView.findViewById(R.id.imageTextContainer);
        imageContainerCard = itemView.findViewById(R.id.imageContainerCard);
        imageContainer = itemView.findViewById(R.id.imageContainer);

        menu = itemView.findViewById(R.id.menu);
        dragView = itemView.findViewById(R.id.drag_view);
        paletteColorContainer = itemView.findViewById(R.id.paletteColorContainer);
        mask = itemView.findViewById(R.id.mask);
        dummyContainer = itemView.findViewById(R.id.dummy_view);

        if (imageContainerCard != null) {
            imageContainerCard.setCardBackgroundColor(Color.TRANSPARENT);
        }
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    @Nullable
    @Override
    public View getSwipeableContainerView() {
        return null;
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    public void setImageTransitionName(@NonNull String transitionName) {
        itemView.setTransitionName(transitionName);
    /* if (imageContainerCard != null) {
        imageContainerCard.setTransitionName(transitionName);
    }
    if (image != null) {
        image.setTransitionName(transitionName);
    }*/
    }
}
