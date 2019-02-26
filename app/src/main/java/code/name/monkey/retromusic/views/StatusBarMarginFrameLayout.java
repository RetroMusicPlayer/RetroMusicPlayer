package code.name.monkey.retromusic.views;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.WindowInsets;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

public class StatusBarMarginFrameLayout extends FrameLayout {


    public StatusBarMarginFrameLayout(@NonNull Context context) {
        super(context);
    }

    public StatusBarMarginFrameLayout(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
    }

    public StatusBarMarginFrameLayout(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @NonNull
    @Override
    public WindowInsets onApplyWindowInsets(@NonNull WindowInsets insets) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            MarginLayoutParams lp = (MarginLayoutParams) getLayoutParams();
            lp.topMargin = insets.getSystemWindowInsetTop();
            lp.bottomMargin = insets.getSystemWindowInsetBottom();
            setLayoutParams(lp);
        }
        return super.onApplyWindowInsets(insets);
    }
}
