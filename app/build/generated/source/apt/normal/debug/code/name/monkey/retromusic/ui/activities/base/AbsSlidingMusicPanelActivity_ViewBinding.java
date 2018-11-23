// Generated code from Butter Knife. Do not modify!
package code.name.monkey.retromusic.ui.activities.base;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.views.BottomNavigationBarTinted;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AbsSlidingMusicPanelActivity_ViewBinding implements Unbinder {
  private AbsSlidingMusicPanelActivity target;

  @UiThread
  public AbsSlidingMusicPanelActivity_ViewBinding(AbsSlidingMusicPanelActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public AbsSlidingMusicPanelActivity_ViewBinding(AbsSlidingMusicPanelActivity target,
      View source) {
    this.target = target;

    target.slidingUpPanelLayout = Utils.findRequiredViewAsType(source, R.id.sliding_layout, "field 'slidingUpPanelLayout'", SlidingUpPanelLayout.class);
    target.bottomNavigationView = Utils.findRequiredViewAsType(source, R.id.bottom_navigation, "field 'bottomNavigationView'", BottomNavigationBarTinted.class);
    target.coordinatorLayout = Utils.findRequiredViewAsType(source, R.id.main_content, "field 'coordinatorLayout'", CoordinatorLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    AbsSlidingMusicPanelActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.slidingUpPanelLayout = null;
    target.bottomNavigationView = null;
    target.coordinatorLayout = null;
  }
}
