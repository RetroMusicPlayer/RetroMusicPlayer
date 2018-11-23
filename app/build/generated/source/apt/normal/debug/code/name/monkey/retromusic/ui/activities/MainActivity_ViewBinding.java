// Generated code from Butter Knife. Do not modify!
package code.name.monkey.retromusic.ui.activities;

import android.view.View;
import android.widget.FrameLayout;
import androidx.annotation.UiThread;
import butterknife.internal.Utils;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.ui.activities.base.AbsSlidingMusicPanelActivity_ViewBinding;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MainActivity_ViewBinding extends AbsSlidingMusicPanelActivity_ViewBinding {
  private MainActivity target;

  @UiThread
  public MainActivity_ViewBinding(MainActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public MainActivity_ViewBinding(MainActivity target, View source) {
    super(target, source);

    this.target = target;

    target.drawerLayout = Utils.findRequiredViewAsType(source, R.id.parent_container, "field 'drawerLayout'", FrameLayout.class);
  }

  @Override
  public void unbind() {
    MainActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.drawerLayout = null;

    super.unbind();
  }
}
