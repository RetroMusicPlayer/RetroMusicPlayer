// Generated code from Butter Knife. Do not modify!
package code.name.monkey.retromusic.ui.fragments.player.cardblur;

import android.view.View;
import android.widget.ImageView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.Toolbar;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import code.name.monkey.retromusic.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CardBlurFragment_ViewBinding implements Unbinder {
  private CardBlurFragment target;

  @UiThread
  public CardBlurFragment_ViewBinding(CardBlurFragment target, View source) {
    this.target = target;

    target.toolbar = Utils.findRequiredViewAsType(source, R.id.player_toolbar, "field 'toolbar'", Toolbar.class);
    target.colorBackground = Utils.findRequiredViewAsType(source, R.id.gradient_background, "field 'colorBackground'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CardBlurFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbar = null;
    target.colorBackground = null;
  }
}
