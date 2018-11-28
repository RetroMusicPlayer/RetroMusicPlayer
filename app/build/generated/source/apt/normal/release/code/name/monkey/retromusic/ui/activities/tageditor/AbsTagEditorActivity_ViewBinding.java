// Generated code from Butter Knife. Do not modify!
package code.name.monkey.retromusic.ui.activities.tageditor;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import code.name.monkey.retromusic.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AbsTagEditorActivity_ViewBinding implements Unbinder {
  private AbsTagEditorActivity target;

  @UiThread
  public AbsTagEditorActivity_ViewBinding(AbsTagEditorActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public AbsTagEditorActivity_ViewBinding(AbsTagEditorActivity target, View source) {
    this.target = target;

    target.save = Utils.findRequiredViewAsType(source, R.id.save_fab, "field 'save'", FloatingActionButton.class);
    target.image = Utils.findRequiredViewAsType(source, R.id.image, "field 'image'", ImageView.class);
    target.imageContainer = Utils.findRequiredViewAsType(source, R.id.image_container, "field 'imageContainer'", FrameLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    AbsTagEditorActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.save = null;
    target.image = null;
    target.imageContainer = null;
  }
}
