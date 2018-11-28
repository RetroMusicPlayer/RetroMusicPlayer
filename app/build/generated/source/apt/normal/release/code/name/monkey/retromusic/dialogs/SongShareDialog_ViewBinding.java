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

public class SongShareDialog_ViewBinding implements Unbinder {
  private SongShareDialog target;

  private View view7f0a01c6;

  private View view7f0a01c7;

  @UiThread
  public SongShareDialog_ViewBinding(final SongShareDialog target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.option_1, "field 'audioFile' and method 'onClick'");
    target.audioFile = Utils.castView(view, R.id.option_1, "field 'audioFile'", TextView.class);
    view7f0a01c6 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.option_2, "field 'audioText' and method 'onClick'");
    target.audioText = Utils.castView(view, R.id.option_2, "field 'audioText'", TextView.class);
    view7f0a01c7 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.title = Utils.findRequiredViewAsType(source, R.id.title, "field 'title'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    SongShareDialog target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.audioFile = null;
    target.audioText = null;
    target.title = null;

    view7f0a01c6.setOnClickListener(null);
    view7f0a01c6 = null;
    view7f0a01c7.setOnClickListener(null);
    view7f0a01c7 = null;
  }
}
