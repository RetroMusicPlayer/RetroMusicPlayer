// Generated code from Butter Knife. Do not modify!
package code.name.monkey.retromusic.ui.activities;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import code.name.monkey.retromusic.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ErrorHandlerActivity_ViewBinding implements Unbinder {
  private ErrorHandlerActivity target;

  private View view7f0a00cb;

  @UiThread
  public ErrorHandlerActivity_ViewBinding(ErrorHandlerActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ErrorHandlerActivity_ViewBinding(final ErrorHandlerActivity target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.clear_app_data, "method 'clearAppDate'");
    view7f0a00cb = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.clearAppDate(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    target = null;


    view7f0a00cb.setOnClickListener(null);
    view7f0a00cb = null;
  }
}
