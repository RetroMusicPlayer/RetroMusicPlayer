package code.name.monkey.retromusic.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import code.name.monkey.retromusic.R;

/**
 * Frame layout that has rounded corners (it clips content too).
 *
 * @author Anton Chekulaev
 */
public class RoundCornerFrameLayout extends FrameLayout {

  private final Path stencilPath = new Path();
  private float cornerRadius = 0;

  public RoundCornerFrameLayout(Context context) {
    this(context, null);
  }

  public RoundCornerFrameLayout(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public RoundCornerFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    TypedArray attrArray = context
        .obtainStyledAttributes(attrs, R.styleable.RoundCornerFrameLayout, 0, 0);
    try {
      cornerRadius = attrArray.getDimension(R.styleable.RoundCornerFrameLayout_corner_radius, 0f);
    } finally {
      attrArray.recycle();
    }
  }

  /*@Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);

    // compute the path
    stencilPath.reset();
    stencilPath.addRoundRect(0, 0, w, h, cornerRadius, cornerRadius, Path.Direction.CW);
    stencilPath.close();

  }
*/
  @Override
  protected void dispatchDraw(Canvas canvas) {
    final int count = canvas.save();
    final Path path = new Path();
    final RectF rect = new RectF(0, 0, canvas.getWidth(), canvas.getHeight());
    final float[] arrayRadius = {cornerRadius, cornerRadius, cornerRadius, cornerRadius,
        cornerRadius, cornerRadius, cornerRadius, cornerRadius};

    path.addRoundRect(rect, arrayRadius, Path.Direction.CW);
    canvas.clipPath(path, Region.Op.REPLACE);
    canvas.clipPath(path);

    super.dispatchDraw(canvas);

    canvas.restoreToCount(count);
  }
} 