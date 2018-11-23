// Generated code from Butter Knife. Do not modify!
package code.name.monkey.retromusic.ui.activities;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.views.IconImageView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SupportDevelopmentActivity$SkuDetailsAdapter$ViewHolder_ViewBinding implements Unbinder {
  private SupportDevelopmentActivity.SkuDetailsAdapter.ViewHolder target;

  @UiThread
  public SupportDevelopmentActivity$SkuDetailsAdapter$ViewHolder_ViewBinding(
      SupportDevelopmentActivity.SkuDetailsAdapter.ViewHolder target, View source) {
    this.target = target;

    target.title = Utils.findRequiredViewAsType(source, R.id.title, "field 'title'", TextView.class);
    target.text = Utils.findRequiredViewAsType(source, R.id.text, "field 'text'", TextView.class);
    target.price = Utils.findRequiredViewAsType(source, R.id.price, "field 'price'", TextView.class);
    target.image = Utils.findRequiredViewAsType(source, R.id.image, "field 'image'", IconImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    SupportDevelopmentActivity.SkuDetailsAdapter.ViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.title = null;
    target.text = null;
    target.price = null;
    target.image = null;
  }
}
