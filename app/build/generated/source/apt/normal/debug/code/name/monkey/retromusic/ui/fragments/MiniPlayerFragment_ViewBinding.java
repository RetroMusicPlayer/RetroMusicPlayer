// Generated code from Butter Knife. Do not modify!
package code.name.monkey.retromusic.ui.fragments;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import code.name.monkey.retromusic.R;
import java.lang.IllegalStateException;
import java.lang.Override;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class MiniPlayerFragment_ViewBinding implements Unbinder {
  private MiniPlayerFragment target;

  private View view7f0a003e;

  private View view7f0a0044;

  private View view7f0a0042;

  @UiThread
  public MiniPlayerFragment_ViewBinding(final MiniPlayerFragment target, View source) {
    this.target = target;

    View view;
    target.miniPlayerTitle = Utils.findRequiredViewAsType(source, R.id.mini_player_title, "field 'miniPlayerTitle'", TextView.class);
    target.miniPlayerPlayPauseButton = Utils.findRequiredViewAsType(source, R.id.mini_player_play_pause_button, "field 'miniPlayerPlayPauseButton'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.action_next, "field 'next' and method 'onClicks'");
    target.next = view;
    view7f0a003e = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClicks(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.action_prev, "field 'previous' and method 'onClicks'");
    target.previous = view;
    view7f0a0044 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClicks(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.action_playing_queue, "field 'playingQueue' and method 'onClicks'");
    target.playingQueue = view;
    view7f0a0042 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClicks(p0);
      }
    });
    target.progressBar = Utils.findRequiredViewAsType(source, R.id.progress_bar, "field 'progressBar'", MaterialProgressBar.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MiniPlayerFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.miniPlayerTitle = null;
    target.miniPlayerPlayPauseButton = null;
    target.next = null;
    target.previous = null;
    target.playingQueue = null;
    target.progressBar = null;

    view7f0a003e.setOnClickListener(null);
    view7f0a003e = null;
    view7f0a0044.setOnClickListener(null);
    view7f0a0044 = null;
    view7f0a0042.setOnClickListener(null);
    view7f0a0042 = null;
  }
}
