/*
 * Copyright (c) 2019 Hemanth Savarala.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by
 *  the Free Software Foundation either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package code.name.monkey.retromusic.adapter.base;

import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import code.name.monkey.appthemehelper.util.ATHUtil;
import code.name.monkey.retromusic.R;

public class MediaEntryViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {
    @Nullable
    public TextView title;

    @Nullable
    public TextView text;

    @Nullable
    public TextView time;

    @Nullable
    public TextView imageText;

    @Nullable
    public ViewGroup imageContainer;

    @Nullable
    public MaterialCardView imageContainerCard;

    @Nullable
    public View menu;

    @Nullable
    public View dragView;

    @Nullable
    public View paletteColorContainer;

    @Nullable
    public RecyclerView recyclerView;

    @Nullable
    public ImageButton playSongs;

    @Nullable
    public View mask;

    @Nullable
    public MaterialCardView imageTextContainer;

    @Nullable
    public ImageView image;

    public MediaEntryViewHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.title);
        text = itemView.findViewById(R.id.text);

        image = itemView.findViewById(R.id.image);
        time = itemView.findViewById(R.id.time);

        imageText = itemView.findViewById(R.id.image_text);
        imageContainer = itemView.findViewById(R.id.image_container);
        imageTextContainer = itemView.findViewById(R.id.image_text_container);
        imageContainerCard = itemView.findViewById(R.id.image_container_card);

        menu = itemView.findViewById(R.id.menu);
        dragView = itemView.findViewById(R.id.drag_view);
        paletteColorContainer = itemView.findViewById(R.id.palette_color_container);
        recyclerView = itemView.findViewById(R.id.recycler_view);
        mask = itemView.findViewById(R.id.mask);
        playSongs = itemView.findViewById(R.id.playSongs);

        if (imageContainerCard != null) {
            imageContainerCard.setCardBackgroundColor(ATHUtil.INSTANCE.resolveColor(itemView.getContext(), R.attr.colorPrimary));
        }
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onClick(View v) {

    }

    public void setImageTransitionName(@NonNull String transitionName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && image != null) {
            image.setTransitionName(transitionName);
        }
    }
}
