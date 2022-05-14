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

package code.name.monkey.retromusic.misc;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

/**
 * Implementation of {@link PagerAdapter} that uses a {@link Fragment} to manage each page. This
 * class also handles saving and restoring of fragment's state.
 *
 * <p>
 *
 * <p>This version of the pager is more useful when there are a large number of pages, working more
 * like a list view. When pages are not visible to the user, their entire fragment may be destroyed,
 * only keeping the saved state of that fragment. This allows the pager to hold on to much less
 * memory associated with each visited page as compared to {@link FragmentPagerAdapter} at the cost
 * of potentially more overhead when switching between pages.
 *
 * <p>
 *
 * <p>When using FragmentPagerAdapter the host ViewPager must have a valid ID set.
 *
 * <p>
 *
 * <p>Subclasses only need to implement {@link #getItem(int)} and {@link #getCount()} to have a
 * working adapter.
 *
 * <p>
 *
 * <p>Here is an example implementation of a pager containing fragments of lists:
 *
 * <p>{@sample
 * development/samples/Support13Demos/src/com/example/android/supportv13/app/FragmentStatePagerSupport.java
 * complete}
 *
 * <p>
 *
 * <p>The <code>R.layout.fragment_pager</code> resource of the top-level fragment is:
 *
 * <p>{@sample development/samples/Support13Demos/res/layout/fragment_pager.xml complete}
 *
 * <p>
 *
 * <p>The <code>R.layout.fragment_pager_list</code> resource containing each individual fragment's
 * layout is:
 *
 * <p>{@sample development/samples/Support13Demos/res/layout/fragment_pager_list.xml complete}
 */
public abstract class CustomFragmentStatePagerAdapter extends PagerAdapter {
  public static final String TAG = CustomFragmentStatePagerAdapter.class.getSimpleName();
  private static final boolean DEBUG = false;

  private final FragmentManager mFragmentManager;
  private FragmentTransaction mCurTransaction = null;

  private final ArrayList<Fragment.SavedState> mSavedState = new ArrayList<>();
  private final ArrayList<Fragment> mFragments = new ArrayList<>();
  private Fragment mCurrentPrimaryItem = null;

  public CustomFragmentStatePagerAdapter(FragmentManager fm) {
    mFragmentManager = fm;
  }

  /**
   * Return the Fragment associated with a specified position.
   */
  public abstract Fragment getItem(int position);

  @Override
  public void startUpdate(@NonNull ViewGroup container) {}

  @NonNull
  @Override
  public Object instantiateItem(@NonNull ViewGroup container, int position) {
    // If we already have this item instantiated, there is nothing
    // to do.  This can happen when we are restoring the entire pager
    // from its saved state, where the fragment manager has already
    // taken care of restoring the fragments we previously had instantiated.
    if (mFragments.size() > position) {
      Fragment f = mFragments.get(position);
      if (f != null) {
        return f;
      }
    }

    if (mCurTransaction == null) {
      mCurTransaction = mFragmentManager.beginTransaction();
    }

    Fragment fragment = getItem(position);
    if (DEBUG) Log.v(TAG, "Adding item #" + position + ": f=" + fragment);
    if (mSavedState.size() > position) {
      Fragment.SavedState fss = mSavedState.get(position);
      if (fss != null) {
        fragment.setInitialSavedState(fss);
      }
    }
    while (mFragments.size() <= position) {
      mFragments.add(null);
    }
    fragment.setMenuVisibility(false);
    fragment.setUserVisibleHint(false);
    mFragments.set(position, fragment);
    mCurTransaction.add(container.getId(), fragment);

    return fragment;
  }

  @Override
  public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
    Fragment fragment = (Fragment) object;

    if (mCurTransaction == null) {
      mCurTransaction = mFragmentManager.beginTransaction();
    }
    if (DEBUG)
      Log.v(
          TAG,
          "Removing item #" + position + ": f=" + object + " v=" + ((Fragment) object).getView());
    while (mSavedState.size() <= position) {
      mSavedState.add(null);
    }
    mSavedState.set(position, mFragmentManager.saveFragmentInstanceState(fragment));
    mFragments.set(position, null);

    mCurTransaction.remove(fragment);
  }

  @Override
  public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
    Fragment fragment = (Fragment) object;
    if (fragment != mCurrentPrimaryItem) {
      if (mCurrentPrimaryItem != null) {
        mCurrentPrimaryItem.setMenuVisibility(false);
        mCurrentPrimaryItem.setUserVisibleHint(false);
      }
      fragment.setMenuVisibility(true);
      fragment.setUserVisibleHint(true);
      mCurrentPrimaryItem = fragment;
    }
  }

  @Override
  public void finishUpdate(@NonNull ViewGroup container) {
    if (mCurTransaction != null) {
      mCurTransaction.commitAllowingStateLoss();
      mCurTransaction = null;
      mFragmentManager.executePendingTransactions();
    }
  }

  @Override
  public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
    return ((Fragment) object).getView() == view;
  }

  @Override
  public Parcelable saveState() {
    Bundle state = null;
    if (mSavedState.size() > 0) {
      state = new Bundle();
      Fragment.SavedState[] fss = new Fragment.SavedState[mSavedState.size()];
      mSavedState.toArray(fss);
      state.putParcelableArray("states", fss);
    }
    for (int i = 0; i < mFragments.size(); i++) {
      Fragment f = mFragments.get(i);
      if (f != null && f.isAdded()) {
        if (state == null) {
          state = new Bundle();
        }
        String key = "f" + i;
        mFragmentManager.putFragment(state, key, f);
      }
    }
    return state;
  }

  @Override
  public void restoreState(Parcelable state, ClassLoader loader) {
    if (state != null) {
      Bundle bundle = (Bundle) state;
      bundle.setClassLoader(loader);
      Parcelable[] fss = bundle.getParcelableArray("states");
      mSavedState.clear();
      mFragments.clear();
      if (fss != null) {
        for (Parcelable parcelable : fss) {
          mSavedState.add((Fragment.SavedState) parcelable);
        }
      }
      Iterable<String> keys = bundle.keySet();
      for (String key : keys) {
        if (key.startsWith("f")) {
          int index = Integer.parseInt(key.substring(1));
          Fragment f = mFragmentManager.getFragment(bundle, key);
          if (f != null) {
            while (mFragments.size() <= index) {
              mFragments.add(null);
            }
            f.setMenuVisibility(false);
            mFragments.set(index, f);
          } else {
            Log.w(TAG, "Bad fragment at key " + key);
          }
        }
      }
    }
  }

  public Fragment getFragment(int position) {
    if (position < mFragments.size() && position >= 0) {
      return mFragments.get(position);
    }
    return null;
  }
}
