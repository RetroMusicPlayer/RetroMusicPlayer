package code.name.monkey.retromusic.util;

import android.graphics.Canvas;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class SwipeAndDragHelper extends ItemTouchHelper.Callback {

  private final ActionCompletionContract contract;

  public SwipeAndDragHelper(@NonNull ActionCompletionContract contract) {
    this.contract = contract;
  }

  @Override
  public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
    int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
    return makeMovementFlags(dragFlags, 0);
  }

  @Override
  public boolean onMove(
          @NonNull RecyclerView recyclerView,
          RecyclerView.ViewHolder viewHolder,
          RecyclerView.ViewHolder target) {
    contract.onViewMoved(viewHolder.getLayoutPosition(), target.getLayoutPosition());
    return true;
  }

  @Override
  public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {}

  @Override
  public boolean isLongPressDragEnabled() {
    return false;
  }

  @Override
  public void onChildDraw(
          @NonNull Canvas c,
          @NonNull RecyclerView recyclerView,
          @NonNull RecyclerView.ViewHolder viewHolder,
          float dX,
          float dY,
          int actionState,
          boolean isCurrentlyActive) {
    if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
      float alpha = 1 - (Math.abs(dX) / recyclerView.getWidth());
      viewHolder.itemView.setAlpha(alpha);
    }
    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
  }

  public interface ActionCompletionContract {
    void onViewMoved(int oldPosition, int newPosition);
  }
}
