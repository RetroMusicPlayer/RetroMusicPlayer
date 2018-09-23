package code.name.monkey.retromusic.transform;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

public class StackPagerTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.75f;


    @Override
    public void transformPage(@NonNull View view, float position) {
        if (position <= 0f) {
            view.setTranslationX(0f);
            view.setScaleX(1f);
            view.setScaleY(1f);

        } else if (position <= 1f) {

            final float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
            view.setPivotY(0.5f * view.getHeight());
            view.setTranslationX(view.getWidth() * -position);
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
        }
    }
}
