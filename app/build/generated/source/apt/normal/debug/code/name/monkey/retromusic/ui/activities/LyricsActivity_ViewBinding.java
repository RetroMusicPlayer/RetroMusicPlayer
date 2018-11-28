// Generated code from Butter Knife. Do not modify!
package code.name.monkey.retromusic.ui.activities;

import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.views.FitSystemWindowsLayout;
import code.name.monkey.retromusic.views.LyricView;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LyricsActivity_ViewBinding implements Unbinder {
  private LyricsActivity target;

  private View view7f0a0102;

  @UiThread
  public LyricsActivity_ViewBinding(LyricsActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public LyricsActivity_ViewBinding(final LyricsActivity target, View source) {
    this.target = target;

    View view;
    target.bottomAppBar = Utils.findRequiredViewAsType(source, R.id.bottom_app_bar, "field 'bottomAppBar'", BottomAppBar.class);
    target.lyricView = Utils.findRequiredViewAsType(source, R.id.lyrics_view, "field 'lyricView'", LyricView.class);
    target.offlineLyrics = Utils.findRequiredViewAsType(source, R.id.offline_lyrics, "field 'offlineLyrics'", TextView.class);
    target.actionsLayout = Utils.findRequiredViewAsType(source, R.id.actions, "field 'actionsLayout'", RadioGroup.class);
    view = Utils.findRequiredView(source, R.id.fab, "field 'actionButton' and method 'onViewClicked'");
    target.actionButton = Utils.castView(view, R.id.fab, "field 'actionButton'", FloatingActionButton.class);
    view7f0a0102 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.fitSystemWindowsLayout = Utils.findRequiredViewAsType(source, R.id.container, "field 'fitSystemWindowsLayout'", FitSystemWindowsLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    LyricsActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.bottomAppBar = null;
    target.lyricView = null;
    target.offlineLyrics = null;
    target.actionsLayout = null;
    target.actionButton = null;
    target.fitSystemWindowsLayout = null;

    view7f0a0102.setOnClickListener(null);
    view7f0a0102 = null;
  }
}
