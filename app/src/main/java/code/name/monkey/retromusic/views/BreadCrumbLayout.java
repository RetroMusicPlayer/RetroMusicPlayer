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

package code.name.monkey.retromusic.views;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import code.name.monkey.appthemehelper.util.ATHUtil;
import code.name.monkey.retromusic.R;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/** @author Aidan Follestad (afollestad), modified for Phonograph by Karim Abou Zeid (kabouzeid) */
public class BreadCrumbLayout extends HorizontalScrollView implements View.OnClickListener {

  @ColorInt private int contentColorActivated;
  @ColorInt private int contentColorDeactivated;
  private int mActive;
  private SelectionCallback mCallback;
  private LinearLayout mChildFrame;
  // Stores currently visible crumbs
  private List<Crumb> mCrumbs;
  // Stores user's navigation history, like a fragment back stack
  private List<Crumb> mHistory;
  // Used in setActiveOrAdd() between clearing crumbs and adding the new set, nullified afterwards
  private List<Crumb> mOldCrumbs;

  public BreadCrumbLayout(Context context) {
    super(context);
    init();
  }

  public BreadCrumbLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public BreadCrumbLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  public void addCrumb(@NonNull Crumb crumb, boolean refreshLayout) {
    LinearLayout view =
        (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.bread_crumb, this, false);
    view.setTag(mCrumbs.size());
    view.setOnClickListener(this);

    ImageView iv = (ImageView) view.getChildAt(1);
    if (iv.getDrawable() != null) {
      iv.getDrawable().setAutoMirrored(true);
    }
    iv.setVisibility(View.GONE);

    mChildFrame.addView(
        view,
        new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    mCrumbs.add(crumb);
    if (refreshLayout) {
      mActive = mCrumbs.size() - 1;
      requestLayout();
    }
    invalidateActivatedAll();
  }

  public void addHistory(Crumb crumb) {
    mHistory.add(crumb);
  }

  public void clearCrumbs() {
    try {
      mOldCrumbs = new ArrayList<>(mCrumbs);
      mCrumbs.clear();
      mChildFrame.removeAllViews();
    } catch (IllegalStateException e) {
      e.printStackTrace();
    }
  }

  public void clearHistory() {
    mHistory.clear();
  }

  public Crumb findCrumb(@NonNull File forDir) {
    for (int i = 0; i < mCrumbs.size(); i++) {
      if (mCrumbs.get(i).getFile().equals(forDir)) {
        return mCrumbs.get(i);
      }
    }
    return null;
  }

  public int getActiveIndex() {
    return mActive;
  }

  public Crumb getCrumb(int index) {
    return mCrumbs.get(index);
  }

  public SavedStateWrapper getStateWrapper() {
    return new SavedStateWrapper(this);
  }

  public int historySize() {
    return mHistory.size();
  }

  public Crumb lastHistory() {
    if (mHistory.size() == 0) {
      return null;
    }
    return mHistory.get(mHistory.size() - 1);
  }

  @Override
  public void onClick(View v) {
    if (mCallback != null) {
      int index = (Integer) v.getTag();
      mCallback.onCrumbSelection(mCrumbs.get(index), index);
    }
  }

  public boolean popHistory() {
    if (mHistory.size() == 0) {
      return false;
    }
    mHistory.remove(mHistory.size() - 1);
    return mHistory.size() != 0;
  }

  public void restoreFromStateWrapper(SavedStateWrapper mSavedState) {
    if (mSavedState != null) {
      mActive = mSavedState.mActive;
      for (Crumb c : mSavedState.mCrumbs) {
        addCrumb(c, false);
      }
      requestLayout();
      setVisibility(mSavedState.mVisibility);
    }
  }

  public void reverseHistory() {
    Collections.reverse(mHistory);
  }

  public void setActivatedContentColor(@ColorInt int contentColorActivated) {
    this.contentColorActivated = contentColorActivated;
  }

  public void setActiveOrAdd(@NonNull Crumb crumb, boolean forceRecreate) {
    if (forceRecreate || !setActive(crumb)) {
      clearCrumbs();
      final List<File> newPathSet = new ArrayList<>();

      newPathSet.add(0, crumb.getFile());

      File p = crumb.getFile();
      while ((p = p.getParentFile()) != null) {
        newPathSet.add(0, p);
      }

      for (int index = 0; index < newPathSet.size(); index++) {
        final File fi = newPathSet.get(index);
        crumb = new Crumb(fi);

        // Restore scroll positions saved before clearing
        if (mOldCrumbs != null) {
          for (Iterator<Crumb> iterator = mOldCrumbs.iterator(); iterator.hasNext(); ) {
            Crumb old = iterator.next();
            if (old.equals(crumb)) {
              crumb.setScrollPosition(old.getScrollPosition());
              iterator.remove(); // minimize number of linear passes by removing un-used crumbs from
              // history
              break;
            }
          }
        }

        addCrumb(crumb, true);
      }

      // History no longer needed
      mOldCrumbs = null;
    }
  }

  public void setCallback(SelectionCallback callback) {
    mCallback = callback;
  }

  public void setDeactivatedContentColor(@ColorInt int contentColorDeactivated) {
    this.contentColorDeactivated = contentColorDeactivated;
  }

  public int size() {
    return mCrumbs.size();
  }

  public boolean trim(String path, boolean dir) {
    if (!dir) {
      return false;
    }
    int index = -1;
    for (int i = mCrumbs.size() - 1; i >= 0; i--) {
      File fi = mCrumbs.get(i).getFile();
      if (fi.getPath().equals(path)) {
        index = i;
        break;
      }
    }

    boolean removedActive = index >= mActive;
    if (index > -1) {
      while (index <= mCrumbs.size() - 1) {
        removeCrumbAt(index);
      }
      if (mChildFrame.getChildCount() > 0) {
        int lastIndex = mCrumbs.size() - 1;
        invalidateActivated(mChildFrame.getChildAt(lastIndex), mActive == lastIndex, false, false);
      }
    }
    return removedActive || mCrumbs.size() == 0;
  }

  public boolean trim(File file) {
    return trim(file.getPath(), file.isDirectory());
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    super.onLayout(changed, l, t, r, b);
    // RTL works fine like this
    View child = mChildFrame.getChildAt(mActive);
    if (child != null) {
      smoothScrollTo(child.getLeft(), 0);
    }
  }

  void invalidateActivatedAll() {
    for (int i = 0; i < mCrumbs.size(); i++) {
      Crumb crumb = mCrumbs.get(i);
      invalidateActivated(
              mChildFrame.getChildAt(i),
              mActive == mCrumbs.indexOf(crumb),
              false,
              i < mCrumbs.size() - 1)
          .setText(crumb.getTitle());
    }
  }

  void removeCrumbAt(int index) {
    mCrumbs.remove(index);
    mChildFrame.removeViewAt(index);
  }

  void updateIndices() {
    for (int i = 0; i < mChildFrame.getChildCount(); i++) {
      mChildFrame.getChildAt(i).setTag(i);
    }
  }

  private void init() {
    contentColorActivated =
        ATHUtil.INSTANCE.resolveColor(getContext(), android.R.attr.textColorPrimary);
    contentColorDeactivated =
        ATHUtil.INSTANCE.resolveColor(getContext(), android.R.attr.textColorSecondary);
    setMinimumHeight((int) getResources().getDimension(R.dimen.tab_height));
    setClipToPadding(false);
    setHorizontalScrollBarEnabled(false);
    mCrumbs = new ArrayList<>();
    mHistory = new ArrayList<>();
    mChildFrame = new LinearLayout(getContext());
    addView(
        mChildFrame,
        new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
  }

  private TextView invalidateActivated(
      View view,
      final boolean isActive,
      final boolean noArrowIfAlone,
      final boolean allowArrowVisible) {
    int contentColor = isActive ? contentColorActivated : contentColorDeactivated;
    LinearLayout child = (LinearLayout) view;
    TextView tv = (TextView) child.getChildAt(0);
    tv.setTextColor(contentColor);
    ImageView iv = (ImageView) child.getChildAt(1);
    iv.setColorFilter(contentColor, PorterDuff.Mode.SRC_IN);
    if (noArrowIfAlone && getChildCount() == 1) {
      iv.setVisibility(View.GONE);
    } else if (allowArrowVisible) {
      iv.setVisibility(View.VISIBLE);
    } else {
      iv.setVisibility(View.GONE);
    }
    return tv;
  }

  private boolean setActive(Crumb newActive) {
    mActive = mCrumbs.indexOf(newActive);
    invalidateActivatedAll();
    boolean success = mActive > -1;
    if (success) {
      requestLayout();
    }
    return success;
  }

  public interface SelectionCallback {

    void onCrumbSelection(Crumb crumb, int index);
  }

  public static class Crumb implements Parcelable {

    public static final Creator<Crumb> CREATOR =
        new Creator<Crumb>() {
          @Override
          public Crumb createFromParcel(Parcel source) {
            return new Crumb(source);
          }

          @Override
          public Crumb[] newArray(int size) {
            return new Crumb[size];
          }
        };

    private final File file;

    private int scrollPos;

    public Crumb(File file) {
      this.file = file;
    }

    protected Crumb(Parcel in) {
      this.file = (File) in.readSerializable();
      this.scrollPos = in.readInt();
    }

    @Override
    public int describeContents() {
      return 0;
    }

    @Override
    public boolean equals(Object o) {
      return (o instanceof Crumb)
          && ((Crumb) o).getFile() != null
          && ((Crumb) o).getFile().equals(getFile());
    }

    public File getFile() {
      return file;
    }

    public int getScrollPosition() {
      return scrollPos;
    }

    public void setScrollPosition(int scrollY) {
      this.scrollPos = scrollY;
    }

    public String getTitle() {
      return file.getPath().equals("/") ? "root" : file.getName();
    }

    @Override
    public String toString() {
      return "Crumb{" + "file=" + file + ", scrollPos=" + scrollPos + '}';
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
      dest.writeSerializable(this.file);
      dest.writeInt(this.scrollPos);
    }
  }

  public static class SavedStateWrapper implements Parcelable {

    public static final Creator<SavedStateWrapper> CREATOR =
        new Creator<SavedStateWrapper>() {
          public SavedStateWrapper createFromParcel(Parcel source) {
            return new SavedStateWrapper(source);
          }

          public SavedStateWrapper[] newArray(int size) {
            return new SavedStateWrapper[size];
          }
        };

    public final int mActive;

    public final List<Crumb> mCrumbs;

    public final int mVisibility;

    public SavedStateWrapper(BreadCrumbLayout view) {
      mActive = view.mActive;
      mCrumbs = view.mCrumbs;
      mVisibility = view.getVisibility();
    }

    protected SavedStateWrapper(Parcel in) {
      this.mActive = in.readInt();
      this.mCrumbs = in.createTypedArrayList(Crumb.CREATOR);
      this.mVisibility = in.readInt();
    }

    @Override
    public int describeContents() {
      return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
      dest.writeInt(this.mActive);
      dest.writeTypedList(mCrumbs);
      dest.writeInt(this.mVisibility);
    }
  }
}
