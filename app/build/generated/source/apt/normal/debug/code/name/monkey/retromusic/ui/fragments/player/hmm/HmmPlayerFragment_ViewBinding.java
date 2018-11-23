// Generated code from Butter Knife. Do not modify!
package code.name.monkey.retromusic.ui.fragments.player.hmm;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.Toolbar;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import code.name.monkey.retromusic.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class HmmPlayerFragment_ViewBinding implements Unbinder {
  private HmmPlayerFragment target;

  @UiThread
  public HmmPlayerFragment_ViewBinding(HmmPlayerFragment target, View source) {
    this.target = target;

    target.title = Utils.findRequiredViewAsType(source, R.id.title, "field 'title'", TextView.class);
    target.text = Utils.findRequiredViewAsType(source, R.id.text, "field 'text'", TextView.class);
    target.totalTime = Utils.findRequiredViewAsType(source, R.id.player_song_total_time, "field 'totalTime'", TextView.class);
    target.progressBar = Utils.findRequiredViewAsType(source, R.id.progress_bar, "field 'progressBar'", ProgressBar.class);
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.player_toolbar, "field 'toolbar'", Toolbar.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    HmmPlayerFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.title = null;
    target.text = null;
    target.totalTime = null;
    target.progressBar = null;
    target.toolbar = null;
  }
}
