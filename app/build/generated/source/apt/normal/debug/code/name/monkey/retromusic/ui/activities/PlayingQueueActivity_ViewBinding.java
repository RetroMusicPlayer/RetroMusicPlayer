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
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.views.CollapsingFAB;
import com.google.android.material.appbar.AppBarLayout;
import java.lang.IllegalStateException;
import java.lang.Override;

public class PlayingQueueActivity_ViewBinding implements Unbinder {
  private PlayingQueueActivity target;

  private View view7f0a00cc;

  @UiThread
  public PlayingQueueActivity_ViewBinding(PlayingQueueActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public PlayingQueueActivity_ViewBinding(final PlayingQueueActivity target, View source) {
    this.target = target;

    View view;
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
    target.textView = Utils.findRequiredViewAsType(source, R.id.player_queue_sub_header, "field 'textView'", TextView.class);
    target.appBarLayout = Utils.findRequiredViewAsType(source, R.id.app_bar, "field 'appBarLayout'", AppBarLayout.class);
    target.title = Utils.findRequiredViewAsType(source, R.id.title, "field 'title'", TextView.class);
    target.mRecyclerView = Utils.findRequiredViewAsType(source, R.id.recycler_view, "field 'mRecyclerView'", RecyclerView.class);
    view = Utils.findRequiredView(source, R.id.clear_queue, "field 'clearQueue' and method 'clearQueue'");
    target.clearQueue = Utils.castView(view, R.id.clear_queue, "field 'clearQueue'", CollapsingFAB.class);
    view7f0a00cc = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.clearQueue();
      }
    });

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
    target.mRecyclerView = null;
    target.clearQueue = null;

    view7f0a00cc.setOnClickListener(null);
    view7f0a00cc = null;
  }
}
