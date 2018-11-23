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
import com.google.android.material.button.MaterialButton;
import java.lang.IllegalStateException;
import java.lang.Override;

public class DeletePlaylistDialog_ViewBinding implements Unbinder {
  private DeletePlaylistDialog target;

  private View view7f0a0021;

  private View view7f0a001b;

  @UiThread
  public DeletePlaylistDialog_ViewBinding(final DeletePlaylistDialog target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.action_delete, "field 'actionDelete' and method 'actions'");
    target.actionDelete = Utils.castView(view, R.id.action_delete, "field 'actionDelete'", MaterialButton.class);
    view7f0a0021 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.actions(p0);
      }
    });
    target.title = Utils.findRequiredViewAsType(source, R.id.title, "field 'title'", TextView.class);
    view = Utils.findRequiredView(source, R.id.action_cancel, "field 'actionCancel' and method 'actions'");
    target.actionCancel = Utils.castView(view, R.id.action_cancel, "field 'actionCancel'", MaterialButton.class);
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
    DeletePlaylistDialog target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.actionDelete = null;
    target.title = null;
    target.actionCancel = null;

    view7f0a0021.setOnClickListener(null);
    view7f0a0021 = null;
    view7f0a001b.setOnClickListener(null);
    view7f0a001b = null;
  }
}
