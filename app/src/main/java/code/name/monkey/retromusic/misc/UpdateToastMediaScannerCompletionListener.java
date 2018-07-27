package code.name.monkey.retromusic.misc;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import code.name.monkey.retromusic.R;

/**
 * @author Karim Abou Zeid (kabouzeid)
 */
public class UpdateToastMediaScannerCompletionListener implements MediaScannerConnection.OnScanCompletedListener {
    private final String[] toBeScanned;
    private final String scannedFiles;
    private final String couldNotScanFiles;
    private final WeakReference<Activity> activityWeakReference;
    private int scanned = 0;
    private int failed = 0;
    private Toast toast;

    @SuppressLint("ShowToast")
    public UpdateToastMediaScannerCompletionListener(Activity activity, String[] toBeScanned) {
        this.toBeScanned = toBeScanned;
        scannedFiles = activity.getString(R.string.scanned_files);
        couldNotScanFiles = activity.getString(R.string.could_not_scan_files);
        toast = Toast.makeText(activity.getApplicationContext(), "", Toast.LENGTH_SHORT);
        activityWeakReference = new WeakReference<>(activity);
    }

    @Override
    public void onScanCompleted(final String path, final Uri uri) {
        Activity activity = activityWeakReference.get();
        if (activity != null) {
            activity.runOnUiThread(() -> {
                if (uri == null) {
                    failed++;
                } else {
                    scanned++;
                }
                String text = " " + String.format(scannedFiles, scanned, toBeScanned.length) + (failed > 0 ? " " + String.format(couldNotScanFiles, failed) : "");
                toast.setText(text);
                toast.show();
            });
        }
    }
}