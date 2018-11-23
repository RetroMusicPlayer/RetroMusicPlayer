// Generated code from Butter Knife. Do not modify!
package code.name.monkey.retromusic.ui.fragments.player.lockscreen;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.AppCompatTextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import code.name.monkey.retromusic.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LockScreenPlayerControlsFragment_ViewBinding implements Unbinder {
  private LockScreenPlayerControlsFragment target;

  private View view7f0a01df;

  @UiThread
  public LockScreenPlayerControlsFragment_ViewBinding(final LockScreenPlayerControlsFragment target,
      View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.player_play_pause_button, "field 'playPauseFab' and method 'showAnimation'");
    target.playPauseFab = Utils.castView(view, R.id.player_play_pause_button, "field 'playPauseFab'", AppCompatImageButton.class);
    view7f0a01df = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.showAnimation();
      }
    });
    target.prevButton = Utils.findRequiredViewAsType(source, R.id.player_prev_button, "field 'prevButton'", ImageButton.class);
    target.nextButton = Utils.findRequiredViewAsType(source, R.id.player_next_button, "field 'nextButton'", ImageButton.class);
    target.progressSlider = Utils.findRequiredViewAsType(source, R.id.player_progress_slider, "field 'progressSlider'", AppCompatSeekBar.class);
    target.songTotalTime = Utils.findRequiredViewAsType(source, R.id.player_song_total_time, "field 'songTotalTime'", TextView.class);
    target.songCurrentProgress = Utils.findRequiredViewAsType(source, R.id.player_song_current_progress, "field 'songCurrentProgress'", TextView.class);
    target.title = Utils.findRequiredViewAsType(source, R.id.title, "field 'title'", AppCompatTextView.class);
    target.text = Utils.findRequiredViewAsType(source, R.id.text, "field 'text'", AppCompatTextView.class);
    target.volumeContainer = Utils.findRequiredView(source, R.id.volume_fragment_container, "field 'volumeContainer'");
  }

  @Override
  @CallSuper
  public void unbind() {
    LockScreenPlayerControlsFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.playPauseFab = null;
    target.prevButton = null;
    target.nextButton = null;
    target.progressSlider = null;
    target.songTotalTime = null;
    target.songCurrentProgress = null;
    target.title = null;
    target.text = null;
    target.volumeContainer = null;

    view7f0a01df.setOnClickListener(null);
    view7f0a01df = null;
  }
}
