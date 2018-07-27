package code.name.monkey.retromusic.misc;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;


public abstract class DialogAsyncTask<Params, Progress, Result> extends WeakContextAsyncTask<Params, Progress, Result> {
    private final int delay;
    private WeakReference<Dialog> dialogWeakReference;

    private boolean supposedToBeDismissed;

    public DialogAsyncTask(Context context) {
        this(context, 0);
    }

    public DialogAsyncTask(Context context, int showDelay) {
        super(context);
        this.delay = showDelay;
        dialogWeakReference = new WeakReference<>(null);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (delay > 0) {
            new Handler().postDelayed(this::initAndShowDialog, delay);
        } else {
            initAndShowDialog();
        }
    }

    private void initAndShowDialog() {
        Context context = getContext();
        if (!supposedToBeDismissed && context != null) {
            Dialog dialog = createDialog(context);
            dialogWeakReference = new WeakReference<>(dialog);
            dialog.show();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onProgressUpdate(Progress... values) {
        super.onProgressUpdate(values);
        Dialog dialog = getDialog();
        if (dialog != null) {
            onProgressUpdate(dialog, values);
        }
    }

    @SuppressWarnings("unchecked")
    protected void onProgressUpdate(@NonNull Dialog dialog, Progress... values) {
    }

    @Nullable
    protected Dialog getDialog() {
        return dialogWeakReference.get();
    }

    @Override
    protected void onCancelled(Result result) {
        super.onCancelled(result);
        tryToDismiss();
    }

    @Override
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);
        tryToDismiss();
    }

    private void tryToDismiss() {
        supposedToBeDismissed = true;
        try {
            Dialog dialog = getDialog();
            if (dialog != null)
                dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract Dialog createDialog(@NonNull Context context);
}
