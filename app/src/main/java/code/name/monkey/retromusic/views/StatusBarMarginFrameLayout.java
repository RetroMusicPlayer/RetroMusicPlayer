package code.name.monkey.retromusic.views;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.WindowInsets;
import android.widget.FrameLayout;

public class StatusBarMarginFrameLayout extends FrameLayout {


    public StatusBarMarginFrameLayout(Context context) {
        super(context);
    }

    public StatusBarMarginFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StatusBarMarginFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            MarginLayoutParams lp = (MarginLayoutParams) getLayoutParams();
            lp.topMargin = insets.getSystemWindowInsetTop();
            setLayoutParams(lp);
        }
        return super.onApplyWindowInsets(insets);
    }
}
