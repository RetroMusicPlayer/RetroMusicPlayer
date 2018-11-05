package code.name.monkey.retromusic.transform;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

public class DepthTransformation implements ViewPager.PageTransformer {
    @Override
    public void transformPage(@NonNull View page, float position) {
        if (position < -1) {    // [-Infinity,-1)
            // This page is way off-screen to the left.
            page.setAlpha(0);
        } else if (position <= 0) {    // [-1,0]
            page.setAlpha(1);
            page.setTranslationX(0);
            page.setScaleX(1);
            page.setScaleY(1);
        } else if (position <= 1) {    // (0,1]
            page.setTranslationX(-position * page.getWidth());
            page.setAlpha(1 - Math.abs(position));
            page.setScaleX(1 - Math.abs(position));
            page.setScaleY(1 - Math.abs(position));
        } else {    // (1,+Infinity]
            // This page is way off-screen to the right.
            page.setAlpha(0);

        }
    }
}