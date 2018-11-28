// Generated code from Butter Knife. Do not modify!
package code.name.monkey.retromusic.ui.activities;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import code.name.monkey.retromusic.R;
import com.google.android.material.appbar.AppBarLayout;
import java.lang.IllegalStateException;
import java.lang.Override;

public class EqualizerActivity_ViewBinding implements Unbinder {
  private EqualizerActivity target;

  @UiThread
  public EqualizerActivity_ViewBinding(EqualizerActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public EqualizerActivity_ViewBinding(EqualizerActivity target, View source) {
    this.target = target;

    target.mEnable = Utils.findRequiredViewAsType(source, R.id.equalizer, "field 'mEnable'", SwitchCompat.class);
    target.mContent = Utils.findRequiredViewAsType(source, R.id.content, "field 'mContent'", LinearLayout.class);
    target.mLinearLayout = Utils.findRequiredViewAsType(source, R.id.bands, "field 'mLinearLayout'", LinearLayout.class);
    target.mBassBoostStrength = Utils.findRequiredViewAsType(source, R.id.bass_boost_strength, "field 'mBassBoostStrength'", SeekBar.class);
    target.mVirtualizerStrength = Utils.findRequiredViewAsType(source, R.id.virtualizer_strength, "field 'mVirtualizerStrength'", SeekBar.class);
    target.mBassBoost = Utils.findRequiredViewAsType(source, R.id.bass_boost, "field 'mBassBoost'", TextView.class);
    target.mVirtualizer = Utils.findRequiredViewAsType(source, R.id.virtualizer, "field 'mVirtualizer'", TextView.class);
    target.mToolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'mToolbar'", Toolbar.class);
    target.mAppBar = Utils.findRequiredViewAsType(source, R.id.app_bar, "field 'mAppBar'", AppBarLayout.class);
    target.mPresets = Utils.findRequiredViewAsType(source, R.id.presets, "field 'mPresets'", Spinner.class);
    target.mTitle = Utils.findRequiredViewAsType(source, R.id.title, "field 'mTitle'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    EqualizerActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mEnable = null;
    target.mContent = null;
    target.mLinearLayout = null;
    target.mBassBoostStrength = null;
    target.mVirtualizerStrength = null;
    target.mBassBoost = null;
    target.mVirtualizer = null;
    target.mToolbar = null;
    target.mAppBar = null;
    target.mPresets = null;
    target.mTitle = null;
  }
}
