// Generated code from Butter Knife. Do not modify!
package code.name.monkey.retromusic.ui.fragments.player.card;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import code.name.monkey.retromusic.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CardFragment_ViewBinding implements Unbinder {
  private CardFragment target;

  @UiThread
  public CardFragment_ViewBinding(CardFragment target, View source) {
    this.target = target;

    target.toolbar = Utils.findRequiredViewAsType(source, R.id.player_toolbar, "field 'toolbar'", Toolbar.class);
    target.recyclerView = Utils.findOptionalViewAsType(source, R.id.recycler_view, "field 'recyclerView'", RecyclerView.class);
    target.title = Utils.findOptionalViewAsType(source, R.id.title, "field 'title'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CardFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbar = null;
    target.recyclerView = null;
    target.title = null;
  }
}
