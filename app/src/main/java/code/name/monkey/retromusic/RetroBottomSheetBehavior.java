package code.name.monkey.retromusic;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import org.jetbrains.annotations.NotNull;

public class RetroBottomSheetBehavior<V extends View> extends BottomSheetBehavior<V> {

  private static final String TAG = "RetroBottomSheetBehavior";

  private boolean allowDragging = true;

  public RetroBottomSheetBehavior() {}

  public RetroBottomSheetBehavior(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public void setAllowDragging(boolean allowDragging) {
    this.allowDragging = allowDragging;
  }

  @Override
  public boolean onInterceptTouchEvent(
      @NotNull CoordinatorLayout parent, @NotNull V child, @NotNull MotionEvent event) {
    if (!allowDragging) {
      return false;
    }
    return super.onInterceptTouchEvent(parent, child, event);
  }
}
