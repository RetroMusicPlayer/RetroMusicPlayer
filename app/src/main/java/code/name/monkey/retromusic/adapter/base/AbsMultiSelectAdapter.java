package code.name.monkey.retromusic.adapter.base;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.interfaces.ICabHolder;
import com.afollestad.materialcab.MaterialCab;
import java.util.ArrayList;
import java.util.List;

public abstract class AbsMultiSelectAdapter<V extends RecyclerView.ViewHolder, I>
    extends RecyclerView.Adapter<V> implements MaterialCab.Callback {

  @Nullable private final ICabHolder ICabHolder;
  private final Context context;
  private MaterialCab cab;
  private List<I> checked;
  private int menuRes;

  public AbsMultiSelectAdapter(
      @NonNull Context context, @Nullable ICabHolder ICabHolder, @MenuRes int menuRes) {
    this.ICabHolder = ICabHolder;
    checked = new ArrayList<>();
    this.menuRes = menuRes;
    this.context = context;
  }

  @Override
  public boolean onCabCreated(MaterialCab materialCab, Menu menu) {
    return true;
  }

  @Override
  public boolean onCabFinished(MaterialCab materialCab) {
    clearChecked();
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

  private void updateCab() {
    if (ICabHolder != null) {
      if (cab == null || !cab.isActive()) {
        cab = ICabHolder.openCab(menuRes, this);
      }
      final int size = checked.size();
      if (size <= 0) {
        cab.finish();
      } else if (size == 1) {
        cab.setTitle(getName(checked.get(0)));
      } else {
        cab.setTitle(context.getString(R.string.x_selected, size));
      }
    }
  }
}
