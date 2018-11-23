// Generated code from Butter Knife. Do not modify!
package code.name.monkey.retromusic.ui.fragments;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import code.name.monkey.retromusic.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class VolumeFragment_ViewBinding implements Unbinder {
  private VolumeFragment target;

  private View view7f0a0281;

  private View view7f0a0286;

  @UiThread
  public VolumeFragment_ViewBinding(final VolumeFragment target, View source) {
    this.target = target;

    View view;
    target.volumeSeekbar = Utils.findRequiredViewAsType(source, R.id.volume_seekbar, "field 'volumeSeekbar'", SeekBar.class);
    view = Utils.findRequiredView(source, R.id.volume_down, "field 'volumeDown' and method 'onViewClicked'");
    target.volumeDown = Utils.castView(view, R.id.volume_down, "field 'volumeDown'", ImageView.class);
    view7f0a0281 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.viewGroup = Utils.findRequiredViewAsType(source, R.id.container, "field 'viewGroup'", ViewGroup.class);
    view = Utils.findRequiredView(source, R.id.volume_up, "field 'volumeUp' and method 'onViewClicked'");
    target.volumeUp = Utils.castView(view, R.id.volume_up, "field 'volumeUp'", ImageView.class);
    view7f0a0286 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    VolumeFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.volumeSeekbar = null;
    target.volumeDown = null;
    target.viewGroup = null;
    target.volumeUp = null;

    view7f0a0281.setOnClickListener(null);
    view7f0a0281 = null;
    view7f0a0286.setOnClickListener(null);
    view7f0a0286 = null;
  }
}
