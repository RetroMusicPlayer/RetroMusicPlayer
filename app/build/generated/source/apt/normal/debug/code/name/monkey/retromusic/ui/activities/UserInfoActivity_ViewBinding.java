// Generated code from Butter Knife. Do not modify!
package code.name.monkey.retromusic.ui.activities;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.Toolbar;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.views.CircularImageView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.lang.IllegalStateException;
import java.lang.Override;

public class UserInfoActivity_ViewBinding implements Unbinder {
  private UserInfoActivity target;

  private View view7f0a027b;

  private View view7f0a01b7;

  private View view7f0a009c;

  @UiThread
  public UserInfoActivity_ViewBinding(UserInfoActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public UserInfoActivity_ViewBinding(final UserInfoActivity target, View source) {
    this.target = target;

    View view;
    target.title = Utils.findRequiredViewAsType(source, R.id.title, "field 'title'", TextView.class);
    target.nameLayout = Utils.findRequiredViewAsType(source, R.id.name_container, "field 'nameLayout'", TextInputLayout.class);
    target.name = Utils.findRequiredViewAsType(source, R.id.name, "field 'name'", TextInputEditText.class);
    view = Utils.findRequiredView(source, R.id.user_image, "field 'userImage' and method 'onViewClicked'");
    target.userImage = Utils.castView(view, R.id.user_image, "field 'userImage'", CircularImageView.class);
    view7f0a027b = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked();
      }
    });
    target.image = Utils.findRequiredViewAsType(source, R.id.banner_image, "field 'image'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.next, "field 'nextButton' and method 'next'");
    target.nextButton = Utils.castView(view, R.id.next, "field 'nextButton'", FloatingActionButton.class);
    view7f0a01b7 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.next(p0);
      }
    });
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
    target.appBarLayout = Utils.findRequiredViewAsType(source, R.id.app_bar, "field 'appBarLayout'", AppBarLayout.class);
    view = Utils.findRequiredView(source, R.id.banner_select, "method 'next'");
    view7f0a009c = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.next(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    UserInfoActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.title = null;
    target.nameLayout = null;
    target.name = null;
    target.userImage = null;
    target.image = null;
    target.nextButton = null;
    target.toolbar = null;
    target.appBarLayout = null;

    view7f0a027b.setOnClickListener(null);
    view7f0a027b = null;
    view7f0a01b7.setOnClickListener(null);
    view7f0a01b7 = null;
    view7f0a009c.setOnClickListener(null);
    view7f0a009c = null;
  }
}
