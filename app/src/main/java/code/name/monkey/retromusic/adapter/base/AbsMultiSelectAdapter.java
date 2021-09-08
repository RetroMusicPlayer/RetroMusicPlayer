package code.name.monkey.retromusic.adapter.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialcab.MaterialCab;

import java.util.ArrayList;
import java.util.List;

import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.interfaces.ICabHolder;

public abstract class AbsMultiSelectAdapter<V extends RecyclerView.ViewHolder, I>
        extends RecyclerView.Adapter<V> implements MaterialCab.Callback {

  @Nullable
  private final ICabHolder ICabHolder;
  private final Context context;
  private MaterialCab cab;
  private final List<I> checked;
  private int menuRes;
  private AppCompatTextView dummyText;
  private int oldSize = 0;

  public AbsMultiSelectAdapter(
          @NonNull Context context, @Nullable ICabHolder ICabHolder, @MenuRes int menuRes) {
    this.ICabHolder = ICabHolder;
    checked = new ArrayList<>();
    this.menuRes = menuRes;
    this.context = context;
  }

  @Override
  public boolean onCabCreated(MaterialCab materialCab, Menu menu) {
    playCreateAnim(materialCab);
    createDummyTextView();
    return true;
  }

  @Override
  public boolean onCabFinished(MaterialCab materialCab) {
    clearChecked();
    cab.getToolbar().removeView(dummyText);
    oldSize = 0;
    return true;
  }

  @Override
  public boolean onCabItemClicked(MenuItem menuItem) {
    if (menuItem.getItemId() == R.id.action_multi_select_adapter_check_all) {
      checkAll();
    } else {
      onMultipleItemAction(menuItem, new ArrayList<>(checked));
      cab.finish();
      clearChecked();
    }
    return true;
  }

  protected void checkAll() {
    if (ICabHolder != null) {
      checked.clear();
      for (int i = 0; i < getItemCount(); i++) {
        I identifier = getIdentifier(i);
        if (identifier != null) {
          checked.add(identifier);
        }
      }
      notifyDataSetChanged();
      updateCab();
    }
  }

  @Nullable
  protected abstract I getIdentifier(int position);

  protected String getName(I object) {
    return object.toString();
  }

  protected boolean isChecked(I identifier) {
    return checked.contains(identifier);
  }

  protected boolean isInQuickSelectMode() {
    return cab != null && cab.isActive();
  }

  protected abstract void onMultipleItemAction(MenuItem menuItem, List<I> selection);

  protected void setMultiSelectMenuRes(@MenuRes int menuRes) {
    this.menuRes = menuRes;
  }

  protected boolean toggleChecked(final int position) {
    if (ICabHolder != null) {
      I identifier = getIdentifier(position);
      if (identifier == null) {
        return false;
      }

      if (!checked.remove(identifier)) {
        checked.add(identifier);
      }

      notifyItemChanged(position);
      updateCab();
      return true;
    }
    return false;
  }

  private void clearChecked() {
    checked.clear();
    notifyDataSetChanged();
  }

  @SuppressLint({"StringFormatInvalid", "StringFormatMatches"})
  private void updateCab() {
    if (ICabHolder != null) {
      if (cab == null || !cab.isActive()) {
        cab = ICabHolder.openCab(menuRes, this);
      }
      final int size = checked.size();
      if (size <= 0) {
        cab.finish();
      } else if (size == 1) {
        cab.setTitle(context.getString(R.string.x_selected, size));
        if (oldSize == 0) {
          cab.getToolbar().addView(dummyText);
        }
      } else {
        AppCompatTextView title = (AppCompatTextView) cab.getToolbar().getChildAt(2);
        dummyText.setText(title.getText());

        title.setAlpha(0);

        cab.setTitle(context.getString(R.string.x_selected, size));
        dummyText.setTranslationX(title.getLeft() - dummyText.getLeft());

        dummyText.setAlpha(1);

        dummyText.setTranslationY(0);
        if (oldSize > size) {
          title.setTranslationY(40);
          dummyText.animate().translationY(-40).alpha(0.0F).setDuration(300).start();
        } else {
          title.setTranslationY(-40);
          dummyText.animate().translationY(40).alpha(0.0F).setDuration(300).start();
        }
        title.animate().translationY(0).alpha(1.0F).setDuration(300).start();
      }
      oldSize = size;
    }
  }

  private void playCreateAnim(MaterialCab materialCab) {
    Toolbar cabToolbar = materialCab.getToolbar();
    int height = context.getResources().getDimensionPixelSize(R.dimen.toolbar_height);
    cabToolbar.setTranslationY(-height);
    cabToolbar.animate().translationYBy(height).setDuration(300).start();
  }

  private void createDummyTextView() {
    if (dummyText != null) return;
    dummyText = new AppCompatTextView(context);
    dummyText.setSingleLine();
    dummyText.setTextAppearance(context, R.style.ToolbarTextAppearanceNormal);
    Toolbar.LayoutParams l1 = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
    dummyText.setLayoutParams(l1);
  }
}
