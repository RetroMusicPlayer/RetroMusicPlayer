package code.name.monkey.retromusic.transform;

import android.annotation.TargetApi;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.View;

/**
 * Created by xgc1986 on 2/Apr/2016
 */

public class ParallaxPagerTransformer implements ViewPager.PageTransformer {
    private int id;
    private int border = 0;
    private float speed = 0.2f;

    public ParallaxPagerTransformer(int id) {
        this.id = id;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void transformPage(@NonNull View view, float position) {

        View parallaxView = view.findViewById(id);

        if (view == null ) {
            Log.w("ParallaxPager", "There is no view");
        }

        if (parallaxView != null) {
            if (position > -1 && position < 1) {
                float width = parallaxView.getWidth();
                parallaxView.setTranslationX(-(position * width * speed));
                float sc = ((float)view.getWidth() - border)/ view.getWidth();
                if (position == 0) {
                    view.setScaleX(1);
                    view.setScaleY(1);
                } else {
                    view.setScaleX(sc);
                    view.setScaleY(sc);
                }
            }
        }
    }

    public void setBorder(int px) {
        border = px;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
