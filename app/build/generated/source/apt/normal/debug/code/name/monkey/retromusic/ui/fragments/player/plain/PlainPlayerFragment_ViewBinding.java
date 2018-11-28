// Generated code from Butter Knife. Do not modify!
package code.name.monkey.retromusic.ui.fragments.player.plain;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.Toolbar;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import code.name.monkey.retromusic.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class PlainPlayerFragment_ViewBinding implements Unbinder {
  private PlainPlayerFragment target;

  @UiThread
  public PlainPlayerFragment_ViewBinding(PlainPlayerFragment target, View source) {
    this.target = target;

    target.title = Utils.findRequiredViewAsType(source, R.id.title, "field 'title'", TextView.class);
    target.text = Utils.findRequiredViewAsType(source, R.id.text, "field 'text'", TextView.class);
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.player_toolbar, "field 'toolbar'", Toolbar.class);
    target.toolbarContainer = Utils.findRequiredViewAsType(source, R.id.toolbar_container, "field 'toolbarContainer'", FrameLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    PlainPlayerFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.title = null;
    target.text = null;
    target.toolbar = null;
    target.toolbarContainer = null;
  }
}
