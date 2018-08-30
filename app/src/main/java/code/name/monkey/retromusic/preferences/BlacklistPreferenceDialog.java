package code.name.monkey.retromusic.preferences;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.text.Html;
import android.view.View;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.dialogs.BlacklistFolderChooserDialog;
import code.name.monkey.retromusic.providers.BlacklistStore;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import java.io.File;
import java.util.ArrayList;


public class BlacklistPreferenceDialog extends DialogFragment implements
    BlacklistFolderChooserDialog.FolderCallback {

  public static final String TAG = BlacklistPreferenceDialog.class.getSimpleName();

  private ArrayList<String> paths;

  public static BlacklistPreferenceDialog newInstance() {
    return new BlacklistPreferenceDialog();
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    BlacklistFolderChooserDialog blacklistFolderChooserDialog = (BlacklistFolderChooserDialog) getChildFragmentManager()
        .findFragmentByTag("FOLDER_CHOOSER");
    if (blacklistFolderChooserDialog != null) {
      blacklistFolderChooserDialog.setCallback(this);
    }

    refreshBlacklistData();
    return new MaterialDialog.Builder(getContext())
        .title(R.string.blacklist)
        .positiveText(android.R.string.ok)
        .neutralText(R.string.clear_action)
        .negativeText(R.string.add_action)
        .items(paths)
        .autoDismiss(false)
        .itemsCallback(new MaterialDialog.ListCallback() {
          @Override
          public void onSelection(MaterialDialog materialDialog, View view, int i,
              final CharSequence charSequence) {
            new MaterialDialog.Builder(getContext())
                .title(R.string.remove_from_blacklist)
                .content(Html.fromHtml(
                    getString(R.string.do_you_want_to_remove_from_the_blacklist, charSequence)))
                .positiveText(R.string.remove_action)
                .negativeText(android.R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                  @Override
                  public void onClick(@NonNull MaterialDialog materialDialog,
                      @NonNull DialogAction dialogAction) {
                    BlacklistStore.getInstance(getContext())
                        .removePath(new File(charSequence.toString()));
                    refreshBlacklistData();
                  }
                }).show();
          }
        })
        // clear
        .onNeutral(new MaterialDialog.SingleButtonCallback() {
          @Override
          public void onClick(@NonNull MaterialDialog materialDialog,
              @NonNull DialogAction dialogAction) {
            new MaterialDialog.Builder(getContext())
                .title(R.string.clear_blacklist)
                .content(R.string.do_you_want_to_clear_the_blacklist)
                .positiveText(R.string.clear_action)
                .negativeText(android.R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                  @Override
                  public void onClick(@NonNull MaterialDialog materialDialog,
                      @NonNull DialogAction dialogAction) {
                    BlacklistStore.getInstance(getContext()).clear();
                    refreshBlacklistData();
                  }
                }).show();
          }
        })
        // add
        .onNegative(new MaterialDialog.SingleButtonCallback() {
          @Override
          public void onClick(@NonNull MaterialDialog materialDialog,
              @NonNull DialogAction dialogAction) {
            BlacklistFolderChooserDialog dialog = BlacklistFolderChooserDialog.create();
            dialog.setCallback(BlacklistPreferenceDialog.this);
            dialog.show(getChildFragmentManager(), "FOLDER_CHOOSER");
          }
        })
        .onPositive(new MaterialDialog.SingleButtonCallback() {
          @Override
          public void onClick(@NonNull MaterialDialog materialDialog,
              @NonNull DialogAction dialogAction) {
            dismiss();
          }
        })
        .build();
  }

  private void refreshBlacklistData() {
    paths = BlacklistStore.getInstance(getContext()).getPaths();

    MaterialDialog dialog = (MaterialDialog) getDialog();
    if (dialog != null) {
      String[] pathArray = new String[paths.size()];
      pathArray = paths.toArray(pathArray);
      dialog.setItems((CharSequence[]) pathArray);
    }
  }

  @Override
  public void onFolderSelection(@NonNull BlacklistFolderChooserDialog folderChooserDialog,
      @NonNull File file) {
    BlacklistStore.getInstance(getContext()).addPath(file);
    refreshBlacklistData();
  }
}
