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
