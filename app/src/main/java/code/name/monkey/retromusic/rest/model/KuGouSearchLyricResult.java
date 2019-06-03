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

import java.util.List;

import androidx.annotation.NonNull;

/**
 * Created by hefuyi on 2017/1/20.
 */

public class KuGouSearchLyricResult {

    private static final String INFO = "info";
    private static final String STATUS = "status";
    private static final String PROPOSAL = "proposal";
    private static final String KEYWORD = "keyword";
    private static final String CANDIDATES = "candidates";

    @NonNull
    @SerializedName(INFO)
    public String info;

    @SerializedName(STATUS)
    public int status;

    @NonNull
    @SerializedName(PROPOSAL)
    public String proposal;

    @NonNull
    @SerializedName(KEYWORD)
    public String keyword;

    @NonNull
    @SerializedName(CANDIDATES)
    public List<Candidates> candidates;

    @Override
    public String toString() {
        return "KuGouSearchLyricResult{" +
                "info='" + info + '\'' +
                ", status=" + status +
                ", proposal='" + proposal + '\'' +
                ", keyword='" + keyword + '\'' +
                ", candidates=" + candidates +
                '}';
    }

    public static class Candidates {
        private static final String NICKNAME = "nickname";
        private static final String ACCESSKEY = "accesskey";
        private static final String SCORE = "score";
        private static final String DURATION = "duration";
        private static final String UID = "uid";
        private static final String SONG = "song";
        private static final String ID = "id";
        private static final String SINGER = "singer";
        private static final String LANGUAGE = "language";
        @SerializedName(NICKNAME)
        public String nickname;
        @SerializedName(ACCESSKEY)
        public String accesskey;
        @SerializedName(SCORE)
        public int score;
        @SerializedName(DURATION)
        public long duration;
        @SerializedName(UID)
        public String uid;
        @SerializedName(SONG)
        public String songName;
        @SerializedName(ID)
        public String id;
        @SerializedName(SINGER)
        public String singer;
        @SerializedName(LANGUAGE)
        public String language;

        @Override
        public String toString() {
            return "Candidates{" +
                    "nickname='" + nickname + '\'' +
                    ", accesskey='" + accesskey + '\'' +
                    ", score=" + score +
                    ", duration=" + duration +
                    ", uid='" + uid + '\'' +
                    ", songName='" + songName + '\'' +
                    ", id='" + id + '\'' +
                    ", singer='" + singer + '\'' +
                    ", language='" + language + '\'' +
                    '}';
        }

    }
}
