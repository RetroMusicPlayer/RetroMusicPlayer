// Generated code from Butter Knife. Do not modify!
package code.name.monkey.retromusic.ui.activities;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.ui.activities.base.AbsSlidingMusicPanelActivity_ViewBinding;
import code.name.monkey.retromusic.views.CollapsingFAB;
import com.google.android.material.appbar.AppBarLayout;
import java.lang.IllegalStateException;
import java.lang.Override;

public class PlaylistDetailActivity_ViewBinding extends AbsSlidingMusicPanelActivity_ViewBinding {
  private PlaylistDetailActivity target;

  private View view7f0a0057;

  @UiThread
  public PlaylistDetailActivity_ViewBinding(PlaylistDetailActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public PlaylistDetailActivity_ViewBinding(final PlaylistDetailActivity target, View source) {
    super(target, source);

    this.target = target;

    View view;
    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.recycler_view, "field 'recyclerView'", RecyclerView.class);
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
    target.empty = Utils.findRequiredViewAsType(source, android.R.id.empty, "field 'empty'", TextView.class);
    view = Utils.findRequiredView(source, R.id.action_shuffle, "field 'shuffleButton' and method 'onViewClicked'");
    target.shuffleButton = Utils.castView(view, R.id.action_shuffle, "field 'shuffleButton'", CollapsingFAB.class);
    view7f0a0057 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked();
      }
    });
    target.appBarLayout = Utils.findRequiredViewAsType(source, R.id.app_bar, "field 'appBarLayout'", AppBarLayout.class);
    target.title = Utils.findRequiredViewAsType(source, R.id.title, "field 'title'", TextView.class);
  }

  @Override
  public void unbind() {
    PlaylistDetailActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.recyclerView = null;
    target.toolbar = null;
    target.empty = null;
    target.shuffleButton = null;
    target.appBarLayout = null;
    target.title = null;

    view7f0a0057.setOnClickListener(null);
    view7f0a0057 = null;

    super.unbind();
  }
}
