package code.name.monkey.retromusic.rest.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hefuyi on 2017/1/20.
 */

public class KuGouRawLyric {

    private static final String CHARSET = "charset";
    private static final String CONTENT = "content";
    private static final String FMT = "fmt";
    private static final String INFO = "info";
    private static final String STATUS = "status";

    @SerializedName(CHARSET)
    public String charset;

    @SerializedName(CONTENT)
    public String content;

    @SerializedName(FMT)
    public String fmt;
    @SerializedName(INFO)
    public String info;
    @SerializedName(STATUS)
    public int status;

    @Override
    public String toString() {
        return "KuGouRawLyric{" +
                "charset='" + charset + '\'' +
                ", content='" + content + '\'' +
                ", fmt='" + fmt + '\'' +
                ", info='" + info + '\'' +
                ", status=" + status +
                '}';
    }

}
