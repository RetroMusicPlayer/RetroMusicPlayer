// Generated code from Butter Knife. Do not modify!
package code.name.monkey.retromusic.ui.activities;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import code.name.monkey.retromusic.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SearchActivity_ViewBinding implements Unbinder {
  private SearchActivity target;

  private View view7f0a0283;

  private View view7f0a0096;

  @UiThread
  public SearchActivity_ViewBinding(SearchActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public SearchActivity_ViewBinding(final SearchActivity target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.voice_search, "field 'micIcon' and method 'searchImageView'");
    target.micIcon = view;
    view7f0a0283 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.searchImageView(p0);
      }
    });
    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.recycler_view, "field 'recyclerView'", RecyclerView.class);
    target.empty = Utils.findRequiredViewAsType(source, android.R.id.empty, "field 'empty'", TextView.class);
    target.searchView = Utils.findRequiredViewAsType(source, R.id.search_view, "field 'searchView'", EditText.class);
    target.statusBar = Utils.findRequiredView(source, R.id.status_bar, "field 'statusBar'");
    view = Utils.findRequiredView(source, R.id.back, "method 'searchImageView'");
    view7f0a0096 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.searchImageView(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    SearchActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.micIcon = null;
    target.recyclerView = null;
    target.empty = null;
    target.searchView = null;
    target.statusBar = null;

    view7f0a0283.setOnClickListener(null);
    view7f0a0283 = null;
    view7f0a0096.setOnClickListener(null);
    view7f0a0096 = null;
  }
}
