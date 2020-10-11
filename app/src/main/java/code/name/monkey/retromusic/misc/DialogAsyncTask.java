/*
 * Copyright (c) 2019 Hemanth Savarala.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by
 *  the Free Software Foundation either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package code.name.monkey.retromusic.misc;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.lang.ref.WeakReference;

public abstract class DialogAsyncTask<Params, Progress, Result>
    extends WeakContextAsyncTask<Params, Progress, Result> {
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
  protected void onProgressUpdate(@NonNull Dialog dialog, Progress... values) {}

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
      if (dialog != null) dialog.dismiss();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  protected abstract Dialog createDialog(@NonNull Context context);
}
