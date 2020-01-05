package me.everything.android.ui.overscroll.adapters;

import android.view.View;
import android.widget.AbsListView;

import me.everything.android.ui.overscroll.HorizontalOverScrollBounceEffectDecorator;
import me.everything.android.ui.overscroll.VerticalOverScrollBounceEffectDecorator;

/**
 * An adapter to enable over-scrolling over object of {@link AbsListView}, namely {@link
 * android.widget.ListView} and it's extensions, and {@link android.widget.GridView}.
 *
 * @author amit
 *
 * @see HorizontalOverScrollBounceEffectDecorator
 * @see VerticalOverScrollBounceEffectDecorator
 */
public class AbsListViewOverScrollDecorAdapter implements IOverScrollDecoratorAdapter {

    protected final AbsListView mView;

    public AbsListViewOverScrollDecorAdapter(AbsListView view) {
        mView = view;
    }

    @Override
    public View getView() {
        return mView;
    }

    @Override
    public boolean isInAbsoluteStart() {
        return mView.getChildCount() > 0 && !canScrollListUp();
    }

    @Override
    public boolean isInAbsoluteEnd() {
        return mView.getChildCount() > 0 && !canScrollListDown();
    }

    public boolean canScrollListUp() {
        // Ported from AbsListView#canScrollList() which isn't compatible to all API levels
        final int firstTop = mView.getChildAt(0).getTop();
        final int firstPosition = mView.getFirstVisiblePosition();
        return firstPosition > 0 || firstTop < mView.getListPaddingTop();
    }

    public boolean canScrollListDown() {
        // Ported from AbsListView#canScrollList() which isn't compatible to all API levels
        final int childCount = mView.getChildCount();
        final int itemsCount = mView.getCount();
        final int firstPosition = mView.getFirstVisiblePosition();
        final int lastPosition = firstPosition + childCount;
        final int lastBottom = mView.getChildAt(childCount - 1).getBottom();
        return lastPosition < itemsCount || lastBottom > mView.getHeight() - mView.getListPaddingBottom();
    }
}
