package code.name.monkey.retromusic.misc;


import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
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

    private Context  context;
    private final List<String> paths;
    private final ScanViewModel scanViewModel;
    private int scanCount = 0;
    private final int totalPaths;
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
    }


    @Override
    public void onScanCompleted(String path, Uri uri) {
        scanCount++;

        if (uri != null) {
            actualFilesFoundInScan++;
            scanViewModel.pathScanned(path);
        }
        if (scanCount >= totalPaths) {
            if (scanViewModel != null) {
                scanViewModel.notifyScanFinishedSuccessfully(context.getString(R.string.scan_complete, actualFilesFoundInScan));
            }
        }
    }
}