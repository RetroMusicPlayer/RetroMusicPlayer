// Generated code from Butter Knife. Do not modify!
package code.name.monkey.retromusic.ui.fragments.mainactivity;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.Toolbar;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import code.name.monkey.retromusic.R;
import com.google.android.material.appbar.AppBarLayout;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LibraryFragment_ViewBinding implements Unbinder {
  private LibraryFragment target;

  @UiThread
  public LibraryFragment_ViewBinding(LibraryFragment target, View source) {
    this.target = target;

    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
    target.appbar = Utils.findRequiredViewAsType(source, R.id.app_bar, "field 'appbar'", AppBarLayout.class);
    target.title = Utils.findRequiredViewAsType(source, R.id.title, "field 'title'", TextView.class);
    target.contentContainer = Utils.findRequiredView(source, R.id.fragment_container, "field 'contentContainer'");
  }

  @Override
  @CallSuper
  public void unbind() {
    LibraryFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbar = null;
    target.appbar = null;
    target.title = null;
    target.contentContainer = null;
  }
}
