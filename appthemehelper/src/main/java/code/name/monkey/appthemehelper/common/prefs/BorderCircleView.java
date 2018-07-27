package code.name.monkey.appthemehelper.common.prefs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import code.name.monkey.appthemehelper.R;

public class BorderCircleView extends FrameLayout {

    private final Drawable mCheck;
    private final Paint paint;
    private final Paint paintBorder;
    private final int borderWidth;

    private Paint paintCheck;
    private PorterDuffColorFilter blackFilter;
    private PorterDuffColorFilter whiteFilter;

    public BorderCircleView(Context context) {
        this(context, null, 0);
    }

    public BorderCircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BorderCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mCheck = ContextCompat.getDrawable(context, R.drawable.ate_check);
        borderWidth = (int) getResources().getDimension(R.dimen.ate_circleview_border);

        paint = new Paint();
        paint.setAntiAlias(true);

        paintBorder = new Paint();
        paintBorder.setAntiAlias(true);
        paintBorder.setColor(Color.BLACK);

        setWillNotDraw(false);
    }

    @Override
    public void setBackgroundColor(int color) {
        paint.setColor(color);
        requestLayout();
        invalidate();
    }

    public void setBorderColor(int color) {
        paintBorder.setColor(color);
        requestLayout();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            //noinspection SuspiciousNameCombination
            int height = width;
            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, MeasureSpec.getSize(heightMeasureSpec));
            }
            setMeasuredDimension(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int canvasSize = canvas.getWidth();
        if (canvas.getHeight() < canvasSize) {
            canvasSize = canvas.getHeight();
        }

        int circleCenter = (canvasSize - (borderWidth * 2)) / 2;
        canvas.drawCircle(circleCenter + borderWidth, circleCenter + borderWidth, ((canvasSize - (borderWidth * 2)) / 2) + borderWidth - 4.0f, paintBorder);
        canvas.drawCircle(circleCenter + borderWidth, circleCenter + borderWidth, ((canvasSize - (borderWidth * 2)) / 2) - 4.0f, paint);

        if (isActivated()) {
            final int offset = (canvasSize / 2) - (mCheck.getIntrinsicWidth() / 2);
            if (paintCheck == null) {
                paintCheck = new Paint();
                paintCheck.setAntiAlias(true);
            }
            if (whiteFilter == null || blackFilter == null) {
                blackFilter = new PorterDuffColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
                whiteFilter = new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            }
            if (paint.getColor() == Color.WHITE) {
                paintCheck.setColorFilter(blackFilter);
            } else {
                paintCheck.setColorFilter(whiteFilter);
            }
            mCheck.setBounds(offset, offset, mCheck.getIntrinsicWidth() - offset, mCheck.getIntrinsicHeight() - offset);
            mCheck.draw(canvas);
        }
    }
}
