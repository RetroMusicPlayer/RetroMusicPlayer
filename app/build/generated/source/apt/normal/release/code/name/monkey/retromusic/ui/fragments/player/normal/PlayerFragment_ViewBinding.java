// Generated code from Butter Knife. Do not modify!
package code.name.monkey.retromusic.ui.fragments.player.normal;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.Toolbar;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import code.name.monkey.retromusic.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class PlayerFragment_ViewBinding implements Unbinder {
  private PlayerFragment target;

  @UiThread
  public PlayerFragment_ViewBinding(PlayerFragment target, View source) {
    this.target = target;

    target.toolbar = Utils.findRequiredViewAsType(source, R.id.player_toolbar, "field 'toolbar'", Toolbar.class);
    target.colorBackground = Utils.findRequiredView(source, R.id.gradient_background, "field 'colorBackground'");
  }

  @Override
  @CallSuper
  public void unbind() {
    PlayerFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbar = null;
    target.colorBackground = null;
  }
}
