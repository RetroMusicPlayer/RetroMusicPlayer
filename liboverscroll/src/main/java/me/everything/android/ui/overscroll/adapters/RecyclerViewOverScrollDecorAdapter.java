package me.everything.android.ui.overscroll.adapters;

import android.graphics.Canvas;
import android.view.View;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import java.util.List;
import me.everything.android.ui.overscroll.HorizontalOverScrollBounceEffectDecorator;
import me.everything.android.ui.overscroll.VerticalOverScrollBounceEffectDecorator;

/**
 * @author amitd
 * @see HorizontalOverScrollBounceEffectDecorator
 * @see VerticalOverScrollBounceEffectDecorator
 */
public class RecyclerViewOverScrollDecorAdapter implements IOverScrollDecoratorAdapter {

    private static class ItemTouchHelperCallbackWrapper extends ItemTouchHelper.Callback {

        final ItemTouchHelper.Callback mCallback;

        private ItemTouchHelperCallbackWrapper(ItemTouchHelper.Callback callback) {
            mCallback = callback;
        }

        @Override
        public boolean canDropOver(RecyclerView recyclerView, RecyclerView.ViewHolder current,
                RecyclerView.ViewHolder target) {
            return mCallback.canDropOver(recyclerView, current, target);
        }

        @Override
        public RecyclerView.ViewHolder chooseDropTarget(RecyclerView.ViewHolder selected,
                List<RecyclerView.ViewHolder> dropTargets, int curX, int curY) {
            return mCallback.chooseDropTarget(selected, dropTargets, curX, curY);
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            mCallback.clearView(recyclerView, viewHolder);
        }

        @Override
        public int convertToAbsoluteDirection(int flags, int layoutDirection) {
            return mCallback.convertToAbsoluteDirection(flags, layoutDirection);
        }

        @Override
        public long getAnimationDuration(RecyclerView recyclerView, int animationType, float animateDx,
                float animateDy) {
            return mCallback.getAnimationDuration(recyclerView, animationType, animateDx, animateDy);
        }

        @Override
        public int getBoundingBoxMargin() {
            return mCallback.getBoundingBoxMargin();
        }

        @Override
        public float getMoveThreshold(RecyclerView.ViewHolder viewHolder) {
            return mCallback.getMoveThreshold(viewHolder);
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return mCallback.getMovementFlags(recyclerView, viewHolder);
        }

        @Override
        public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
            return mCallback.getSwipeThreshold(viewHolder);
        }

        @Override
        public int interpolateOutOfBoundsScroll(RecyclerView recyclerView, int viewSize, int viewSizeOutOfBounds,
                int totalSize, long msSinceStartScroll) {
            return mCallback.interpolateOutOfBoundsScroll(recyclerView, viewSize, viewSizeOutOfBounds, totalSize,
                    msSinceStartScroll);
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return mCallback.isItemViewSwipeEnabled();
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return mCallback.isLongPressDragEnabled();
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX,
                float dY, int actionState, boolean isCurrentlyActive) {
            mCallback.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }

        @Override
        public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX,
                float dY, int actionState, boolean isCurrentlyActive) {
            mCallback.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                RecyclerView.ViewHolder target) {
            return mCallback.onMove(recyclerView, viewHolder, target);
        }

        @Override
        public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int fromPos,
                RecyclerView.ViewHolder target, int toPos, int x, int y) {
            mCallback.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            mCallback.onSelectedChanged(viewHolder, actionState);
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            mCallback.onSwiped(viewHolder, direction);
        }
    }

    protected class ImplHorizLayout implements Impl {

        @Override
        public boolean isInAbsoluteEnd() {
            return !mRecyclerView.canScrollHorizontally(1);
        }

        @Override
        public boolean isInAbsoluteStart() {
            return !mRecyclerView.canScrollHorizontally(-1);
        }
    }

    protected class ImplVerticalLayout implements Impl {

        @Override
        public boolean isInAbsoluteEnd() {
            return !mRecyclerView.canScrollVertically(1);
        }

        @Override
        public boolean isInAbsoluteStart() {
            return !mRecyclerView.canScrollVertically(-1);
        }
    }

    /**
     * A delegation of the adapter implementation of this view that should provide the processing
     * of {@link #isInAbsoluteStart()} and {@link #isInAbsoluteEnd()}. Essentially needed simply
     * because the implementation depends on the layout manager implementation being used.
     */
    protected interface Impl {

        boolean isInAbsoluteEnd();

        boolean isInAbsoluteStart();
    }

    protected final Impl mImpl;

    protected boolean mIsItemTouchInEffect = false;

    protected final RecyclerView mRecyclerView;

    public RecyclerViewOverScrollDecorAdapter(RecyclerView recyclerView) {

        mRecyclerView = recyclerView;

        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager ||
                layoutManager instanceof StaggeredGridLayoutManager) {
            final int orientation =
                    (layoutManager instanceof LinearLayoutManager
                            ? ((LinearLayoutManager) layoutManager).getOrientation()
                            : ((StaggeredGridLayoutManager) layoutManager).getOrientation());

            if (orientation == LinearLayoutManager.HORIZONTAL) {
                mImpl = new ImplHorizLayout();
            } else {
                mImpl = new ImplVerticalLayout();
            }
        } else {
            throw new IllegalArgumentException(
                    "Recycler views with custom layout managers are not supported by this adapter out of the box." +
                            "Try implementing and providing an explicit 'impl' parameter to the other c'tors, or otherwise create a custom adapter subclass of your own.");
        }
    }

    public RecyclerViewOverScrollDecorAdapter(RecyclerView recyclerView, Impl impl) {
        mRecyclerView = recyclerView;
        mImpl = impl;
    }

    public RecyclerViewOverScrollDecorAdapter(RecyclerView recyclerView,
            ItemTouchHelper.Callback itemTouchHelperCallback) {
        this(recyclerView);
        setUpTouchHelperCallback(itemTouchHelperCallback);
    }

    public RecyclerViewOverScrollDecorAdapter(RecyclerView recyclerView, Impl impl,
            ItemTouchHelper.Callback itemTouchHelperCallback) {
        this(recyclerView, impl);
        setUpTouchHelperCallback(itemTouchHelperCallback);
    }

    @Override
    public View getView() {
        return mRecyclerView;
    }

    @Override
    public boolean isInAbsoluteEnd() {
        return !mIsItemTouchInEffect && mImpl.isInAbsoluteEnd();
    }

    @Override
    public boolean isInAbsoluteStart() {
        return !mIsItemTouchInEffect && mImpl.isInAbsoluteStart();
    }

    protected void setUpTouchHelperCallback(final ItemTouchHelper.Callback itemTouchHelperCallback) {
        new ItemTouchHelper(new ItemTouchHelperCallbackWrapper(itemTouchHelperCallback) {
            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                mIsItemTouchInEffect = actionState != 0;
                super.onSelectedChanged(viewHolder, actionState);
            }
        }).attachToRecyclerView(mRecyclerView);
    }
}
