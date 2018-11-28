// Generated code from Butter Knife. Do not modify!
package code.name.monkey.retromusic.dialogs;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import code.name.monkey.retromusic.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class RemoveFromPlaylistDialog_ViewBinding implements Unbinder {
  private RemoveFromPlaylistDialog target;

  private View view7f0a0047;

  private View view7f0a001b;

  @UiThread
  public RemoveFromPlaylistDialog_ViewBinding(final RemoveFromPlaylistDialog target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.action_remove, "field 'remove' and method 'actions'");
    target.remove = Utils.castView(view, R.id.action_remove, "field 'remove'", TextView.class);
    view7f0a0047 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.actions(p0);
      }
    });
    target.title = Utils.findRequiredViewAsType(source, R.id.title, "field 'title'", TextView.class);
    view = Utils.findRequiredView(source, R.id.action_cancel, "field 'cancel' and method 'actions'");
    target.cancel = Utils.castView(view, R.id.action_cancel, "field 'cancel'", TextView.class);
    view7f0a001b = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.actions(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    RemoveFromPlaylistDialog target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.remove = null;
    target.title = null;
    target.cancel = null;

    view7f0a0047.setOnClickListener(null);
    view7f0a0047 = null;
    view7f0a001b.setOnClickListener(null);
    view7f0a001b = null;
  }
}
