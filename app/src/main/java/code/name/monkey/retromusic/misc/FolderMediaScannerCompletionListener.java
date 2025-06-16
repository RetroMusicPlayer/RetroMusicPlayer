package code.name.monkey.retromusic.misc;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.util.Log;

import java.util.List;

import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.fragments.folder.ScanViewModel;


/**
 * @Author by Pinankh Patel
 * Created on Date = 15-06-2025  14:44
 * Github = https://github.com/Pinankh
 * LinkdIN = https://www.linkedin.com/in/pinankh-patel-19400350/
 * Stack Overflow = https://stackoverflow.com/users/4564376/pinankh
 * Medium = https://medium.com/@pinankhpatel
 * Email = pinankhpatel@gmail.com
 */
public class FolderMediaScannerCompletionListener implements MediaScannerConnection.OnScanCompletedListener {

    private final Context context; // Can be applicationContext for safety
    private final List<String> paths; // Assuming paths will not contain nulls, or handle them
    private final ScanViewModel scanViewModel;
    private final String couldNotScanFiles;
    private final String scannedFiles;
    private int scanCount = 0;
    private final int totalPaths;
    private int failed = 0;
    private int scanned = 0;
    private int actualFilesFoundInScan = 0;
    /**
     * Constructor for FolderMediaScannerCompletionListener.
     *
     * @param context       The context, preferably application context.
     * @param paths         A list of file paths to be scanned.
     * @param scanViewModel The ViewModel to notify when all scans are complete.
     */
    public FolderMediaScannerCompletionListener(
            Context context,
            List<String> paths, // Changed from List<String?> to List<String>
            // Java doesn't have nullable types in the same way.
            // If paths can contain nulls, you'll need to handle it or filter them out before passing.
            ScanViewModel scanViewModel
    ) {
        this.context = context;
        this.paths = paths;
        this.scanViewModel = scanViewModel;
        this.totalPaths = (paths != null) ? paths.size() : 0; // Handle null paths list gracefully
        scannedFiles = context.getString(R.string.scanned_files);
        couldNotScanFiles = context.getString(R.string.could_not_scan_files);
    }


    @Override
    public void onScanCompleted(String path, Uri uri) {
        scanCount++;
         Log.d("MediaScanner", "Scanned (" + scanCount + "/" + totalPaths + "): " + path);
        if (uri == null) {
            failed++;
        } else {
            scanned++;
            scanViewModel.pathScanned(path);
            Log.d("ScannedPath", path);

        }
        if (uri != null) { // A non-null URI might indicate the media scanner successfully processed it
            actualFilesFoundInScan++;
        }
        if (scanCount >= totalPaths) {
            // All paths have been processed.
            // Notify ViewModel. LiveData observers on the main thread will react.


            Log.d("MediaScannerComplete", "Scanned (" + scanCount + "/" + totalPaths + "): " + path);
            String text =
                    " "
                            + String.format(scannedFiles, scanned, paths.size())
                            + (failed > 0 ? " " + String.format(couldNotScanFiles, failed) : "");

            /*if (scanViewModel != null) {
                scanViewModel.notifyScanFinished(); // This should trigger UI updates via LiveData
            }*/

            if (scanViewModel != null) {
                // Example: Decide if it's overall success or partial, or if there were errors.
                // For simplicity, let's assume if it completes, it's a success for now.
                // You might have more complex logic to detect errors during the scan.
                if (actualFilesFoundInScan > 0) {
                    scanViewModel.notifyScanFinishedSuccessfully(
                            "Scan complete:\n" + actualFilesFoundInScan + " media items processed.",
                            actualFilesFoundInScan
                    );
                } else if (totalPaths > 0) {
                    scanViewModel.notifyScanFinishedSuccessfully(
                            "Scan finished. \n No new media items found or processed from the given paths.",
                            0
                    );
                } else { // totalPaths was 0
                    scanViewModel.notifyScanFinishedSuccessfully(
                            "Scan finished. Nothing to scan.",
                            0
                    );
                }
            }
        }
    }
}