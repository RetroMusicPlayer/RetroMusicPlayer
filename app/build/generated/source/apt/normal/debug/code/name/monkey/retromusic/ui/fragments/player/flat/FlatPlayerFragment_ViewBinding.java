// Generated code from Butter Knife. Do not modify!
package code.name.monkey.retromusic.ui.fragments.player.flat;

import android.view.View;
import android.widget.FrameLayout;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.Toolbar;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import code.name.monkey.retromusic.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class FlatPlayerFragment_ViewBinding implements Unbinder {
  private FlatPlayerFragment target;

  @UiThread
  public FlatPlayerFragment_ViewBinding(FlatPlayerFragment target, View source) {
    this.target = target;

    target.toolbar = Utils.findRequiredViewAsType(source, R.id.player_toolbar, "field 'toolbar'", Toolbar.class);
    target.colorBackground = Utils.findRequiredView(source, R.id.gradient_background, "field 'colorBackground'");
    target.toolbarContainer = Utils.findRequiredViewAsType(source, R.id.toolbar_container, "field 'toolbarContainer'", FrameLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    FlatPlayerFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbar = null;
    target.colorBackground = null;
    target.toolbarContainer = null;
  }
}
