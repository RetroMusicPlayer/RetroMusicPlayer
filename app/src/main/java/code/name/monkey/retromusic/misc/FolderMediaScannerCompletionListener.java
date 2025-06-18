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

    private final Context context;
    private final List<String> paths;
    private final ScanViewModel scanViewModel;
    private final String couldNotScanFiles;
    private final String scannedFiles;
    private int scanCount = 0;
    private final int totalPaths;
    private int failed = 0;
    private int scanned = 0;
    private int actualFilesFoundInScan = 0;

    public FolderMediaScannerCompletionListener(
            Context context,
            List<String> paths,
            ScanViewModel scanViewModel
    ) {
        this.context = context;
        this.paths = paths;
        this.scanViewModel = scanViewModel;
        this.totalPaths = (paths != null) ? paths.size() : 0;
        scannedFiles = context.getString(R.string.scanned_files);
        couldNotScanFiles = context.getString(R.string.could_not_scan_files);
    }


    @Override
    public void onScanCompleted(String path, Uri uri) {
        scanCount++;
        if (uri == null) {
            failed++;
        } else {
            scanned++;
            scanViewModel.pathScanned(path);

        }
        if (uri != null) {
            actualFilesFoundInScan++;
        }
        if (scanCount >= totalPaths) {

            if (scanViewModel != null) {

                if (actualFilesFoundInScan > 0) {
                    scanViewModel.notifyScanFinishedSuccessfully(
                            "Scan complete:" + actualFilesFoundInScan + " media items found.",
                            actualFilesFoundInScan
                    );
                } else if (totalPaths > 0) {
                    scanViewModel.notifyScanFinishedSuccessfully(
                            "Scan complete No new media items found",
                            0
                    );
                } else {
                    scanViewModel.notifyScanFinishedSuccessfully(
                            "Scan finished. Nothing to scan.",
                            0
                    );
                }
            }
        }
    }
}