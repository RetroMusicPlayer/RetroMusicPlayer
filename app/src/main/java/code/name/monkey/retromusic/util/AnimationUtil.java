package code.name.monkey.retromusic.util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import code.name.monkey.retromusic.R;

/**
 * @author Hemanth S (h4h13).
 */
public class AnimationUtil {

  public static void runLayoutAnimation(final RecyclerView recyclerView) {
    final Context context = recyclerView.getContext();
    final LayoutAnimationController controller =
        AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_slide_from_bottom);

    recyclerView.setLayoutAnimation(controller);
    recyclerView.getAdapter().notifyDataSetChanged();
    recyclerView.scheduleLayoutAnimation();
  }
}
