// Generated code from Butter Knife. Do not modify!
package code.name.monkey.retromusic.dialogs;

import android.view.View;
import android.widget.SeekBar;
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

public class SleepTimerDialog_ViewBinding implements Unbinder {
  private SleepTimerDialog target;

  private View view7f0a0050;

  private View view7f0a001b;

  @UiThread
  public SleepTimerDialog_ViewBinding(final SleepTimerDialog target, View source) {
    this.target = target;

    View view;
    target.seekArc = Utils.findRequiredViewAsType(source, R.id.seek_arc, "field 'seekArc'", SeekBar.class);
    target.timerDisplay = Utils.findRequiredViewAsType(source, R.id.timer_display, "field 'timerDisplay'", TextView.class);
    view = Utils.findRequiredView(source, R.id.action_set, "field 'setButton' and method 'set'");
    target.setButton = Utils.castView(view, R.id.action_set, "field 'setButton'", MaterialButton.class);
    view7f0a0050 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.set(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.action_cancel, "field 'cancelButton' and method 'set'");
    target.cancelButton = Utils.castView(view, R.id.action_cancel, "field 'cancelButton'", MaterialButton.class);
    view7f0a001b = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.set(p0);
      }
    });
    target.title = Utils.findRequiredViewAsType(source, R.id.title, "field 'title'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    SleepTimerDialog target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.seekArc = null;
    target.timerDisplay = null;
    target.setButton = null;
    target.cancelButton = null;
    target.title = null;

    view7f0a0050.setOnClickListener(null);
    view7f0a0050 = null;
    view7f0a001b.setOnClickListener(null);
    view7f0a001b = null;
  }
}
