package code.name.monkey.retromusic.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class HeightFitSquareLayout extends FrameLayout {
    private boolean forceSquare = true;

    public HeightFitSquareLayout(Context context) {
        super(context);
    }

    public HeightFitSquareLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public HeightFitSquareLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @TargetApi(21)
    public HeightFitSquareLayout(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public void forceSquare(boolean z) {
        this.forceSquare = z;
        requestLayout();
    }

    protected void onMeasure(int i, int i2) {
        if (this.forceSquare) {
            i = i2;
        }
        super.onMeasure(i, i2);
    }
}
