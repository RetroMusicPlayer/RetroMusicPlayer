// Generated code from Butter Knife. Do not modify!
package code.name.monkey.retromusic.ui.fragments.player.blur;

import android.view.View;
import android.widget.ImageView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import code.name.monkey.retromusic.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class BlurPlayerFragment_ViewBinding implements Unbinder {
  private BlurPlayerFragment target;

  @UiThread
  public BlurPlayerFragment_ViewBinding(BlurPlayerFragment target, View source) {
    this.target = target;

    target.toolbar = Utils.findRequiredViewAsType(source, R.id.player_toolbar, "field 'toolbar'", Toolbar.class);
    target.toolbarContainer = Utils.findRequiredView(source, R.id.toolbar_container, "field 'toolbarContainer'");
    target.colorBackground = Utils.findRequiredViewAsType(source, R.id.gradient_background, "field 'colorBackground'", ImageView.class);
    target.recyclerView = Utils.findOptionalViewAsType(source, R.id.recycler_view, "field 'recyclerView'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    BlurPlayerFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbar = null;
    target.toolbarContainer = null;
    target.colorBackground = null;
    target.recyclerView = null;
  }
}
