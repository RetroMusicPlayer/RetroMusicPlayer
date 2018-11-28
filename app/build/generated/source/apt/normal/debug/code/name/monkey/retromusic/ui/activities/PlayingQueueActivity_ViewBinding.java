// Generated code from Butter Knife. Do not modify!
package code.name.monkey.retromusic.ui.activities;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import code.name.monkey.retromusic.R;
import com.google.android.material.appbar.AppBarLayout;
import java.lang.IllegalStateException;
import java.lang.Override;

public class PlayingQueueActivity_ViewBinding implements Unbinder {
  private PlayingQueueActivity target;

  @UiThread
  public PlayingQueueActivity_ViewBinding(PlayingQueueActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public PlayingQueueActivity_ViewBinding(PlayingQueueActivity target, View source) {
    this.target = target;

    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
    target.textView = Utils.findRequiredViewAsType(source, R.id.player_queue_sub_header, "field 'textView'", TextView.class);
    target.appBarLayout = Utils.findRequiredViewAsType(source, R.id.app_bar, "field 'appBarLayout'", AppBarLayout.class);
    target.title = Utils.findRequiredViewAsType(source, R.id.title, "field 'title'", TextView.class);

    Context context = source.getContext();
    Resources res = context.getResources();
    target.close = ContextCompat.getDrawable(context, R.drawable.ic_keyboard_backspace_black_24dp);
    target.queue = res.getString(R.string.queue);
  }

  @Override
  @CallSuper
  public void unbind() {
    PlayingQueueActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbar = null;
    target.textView = null;
    target.appBarLayout = null;
    target.title = null;
  }
}
