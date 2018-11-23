// Generated code from Butter Knife. Do not modify!
package code.name.monkey.retromusic.dialogs;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import code.name.monkey.retromusic.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SongDetailDialog_ViewBinding implements Unbinder {
  private SongDetailDialog target;

  @UiThread
  public SongDetailDialog_ViewBinding(SongDetailDialog target, View source) {
    this.target = target;

    target.textViews = Utils.listFilteringNull(
        Utils.findRequiredViewAsType(source, R.id.title, "field 'textViews'", TextView.class), 
        Utils.findRequiredViewAsType(source, R.id.file_name, "field 'textViews'", TextView.class), 
        Utils.findRequiredViewAsType(source, R.id.file_path, "field 'textViews'", TextView.class), 
        Utils.findRequiredViewAsType(source, R.id.file_size, "field 'textViews'", TextView.class), 
        Utils.findRequiredViewAsType(source, R.id.file_format, "field 'textViews'", TextView.class), 
        Utils.findRequiredViewAsType(source, R.id.track_length, "field 'textViews'", TextView.class), 
        Utils.findRequiredViewAsType(source, R.id.bitrate, "field 'textViews'", TextView.class), 
        Utils.findRequiredViewAsType(source, R.id.sampling_rate, "field 'textViews'", TextView.class));
  }

  @Override
  @CallSuper
  public void unbind() {
    SongDetailDialog target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.textViews = null;
  }
}
