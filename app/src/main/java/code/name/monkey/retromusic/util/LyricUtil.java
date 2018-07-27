package code.name.monkey.retromusic.util;

import android.util.Base64;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by hefuyi on 2016/11/8.
 */

public class LyricUtil {

    private static final String lrcRootPath = android.os.Environment
            .getExternalStorageDirectory().toString() + "/RetroMusic/lyrics/";

    public static File writeLrcToLoc(String title, String artist, String lrcContext) {
        FileWriter writer = null;
        try {
            File file = new File(getLrcPath(title, artist));
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            writer = new FileWriter(getLrcPath(title, artist));
            writer.write(lrcContext);
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (writer != null)
                    writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean deleteLrcFile(String title, String artist) {
        File file = new File(getLrcPath(title, artist));
        return file.delete();
    }

    public static boolean isLrcFileExist(String title, String artist) {
        File file = new File(getLrcPath(title, artist));
        return file.exists();
    }

    public static File getLocalLyricFile(String title, String artist) {
        File file = new File(getLrcPath(title, artist));
        if (file.exists()) {
            return file;
        } else {
            return new File("lyric file not exist");
        }
    }

    private static String getLrcPath(String title, String artist) {
        return lrcRootPath + title + " - " + artist + ".lrc";
    }

    public static String decryptBASE64(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        try {
            byte[] encode = str.getBytes("UTF-8");
            // base64 解密
            return new String(Base64.decode(encode, 0, encode.length, Base64.DEFAULT), "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getStringFromFile(String title, String artist) throws Exception {
        File file = new File(getLrcPath(title, artist));
        FileInputStream fin = new FileInputStream(file);
        String ret = convertStreamToString(fin);
        fin.close();
        return ret;
    }

    private static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }
}
