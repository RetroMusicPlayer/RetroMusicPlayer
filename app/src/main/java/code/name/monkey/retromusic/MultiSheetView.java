/*
 * Copyright (c) 2020 Hemanth Savarala.
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

package code.name.monkey.retromusic;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class MultiSheetView extends CoordinatorLayout {

    @interface Sheet {

        int NONE = 0;
        int FIRST = 1;
        int SECOND = 2;
    }

    private static final String TAG = "MultiSheetView";

    private CustomBottomSheetBehavior bottomSheetBehavior1;

    private CustomBottomSheetBehavior bottomSheetBehavior2;

    public MultiSheetView(@NonNull Context context) {
        this(context, null);
    }

    public MultiSheetView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiSheetView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean consumeBackPress() {
        switch (getCurrentSheet()) {
            case Sheet.SECOND:
                showSheet(Sheet.FIRST);
                return true;
            case Sheet.FIRST:
                showSheet(Sheet.NONE);
                return true;
            case Sheet.NONE:
                break;
        }
        return false;
    }

    @Sheet
    public int getCurrentSheet() {
        if (bottomSheetBehavior2.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            return Sheet.SECOND;
        } else if (bottomSheetBehavior1.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            return Sheet.FIRST;
        } else {
            return Sheet.NONE;
        }
    }

    @IdRes
    public int getMainContainerResId() {
        return R.id.mainContentFrame;
    }

    @IdRes
    public int getSheet1ContainerResId() {
        return R.id.playerFragmentContainer;
    }

    @IdRes
    public int getSheet1PeekViewResId() {
        return R.id.miniPlayerFragment;
    }

    @IdRes
    public int getSheet2ContainerResId() {
        return R.id.sheet2Container;
    }

    @IdRes
    public int getSheet2PeekViewResId() {
        return R.id.sheet2PeekView;
    }

    public void showSheet(@Sheet int sheet) {

        // if we are already at our target panel, then don't do anything
        if (sheet == getCurrentSheet()) {
            return;
        }

        switch (sheet) {
            case Sheet.NONE:
                bottomSheetBehavior2.setState(BottomSheetBehavior.STATE_COLLAPSED);
                bottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case Sheet.FIRST:
                bottomSheetBehavior2.setState(BottomSheetBehavior.STATE_COLLAPSED);
                bottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case Sheet.SECOND:
                bottomSheetBehavior2.setState(BottomSheetBehavior.STATE_EXPANDED);
                bottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        View sheet1 = findViewById(R.id.slidingPanel);
        bottomSheetBehavior1 = (CustomBottomSheetBehavior) BottomSheetBehavior.from(sheet1);

        View sheet2 = findViewById(R.id.sheet2);
        View thump = findViewById(R.id.thumb);
        View extendedToolbar = findViewById(R.id.extendedToolbar);
        bottomSheetBehavior2 = (CustomBottomSheetBehavior) BottomSheetBehavior.from(sheet2);
        bottomSheetBehavior2.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                bottomSheetBehavior1.setAllowDragging(false);
                thump.setRotation(slideOffset * 180);
                extendedToolbar.setAlpha(slideOffset);
            }

            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED
                        || newState == BottomSheetBehavior.STATE_DRAGGING) {
                    bottomSheetBehavior1.setAllowDragging(false);
                } else {
                    bottomSheetBehavior1.setAllowDragging(true);
                }
            }
        });

        //First sheet view click listener
        findViewById(getSheet1PeekViewResId())
                .setOnClickListener(v -> bottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED));

        // FIXME:
        // This click listener (combined with a nested RecyclerView in Sheet 2's container), causes
        // the second peek view to stop responding to drag events.
        // See `Sheet2Controller`. Remove this ClickListener here to see things working as they should.

        //Second sheet view click listener
        findViewById(getSheet2PeekViewResId())
                .setOnClickListener(v -> bottomSheetBehavior2.setState(BottomSheetBehavior.STATE_EXPANDED));

        // FIXED:
        // issue was  bottomSheetBehavior1 is taking drag event when getSheet2PeekView is dragging
        // so detect touch event  getSheet2PeekView set bottomSheetBehavior1 dragging false and bottomSheetBehavior2 true
        findViewById(getSheet2PeekViewResId()).setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e(TAG, "onTouch: ");
                bottomSheetBehavior1.setAllowDragging(false);
                bottomSheetBehavior2.setAllowDragging(true);
                return false;
            }
        });
    }
}