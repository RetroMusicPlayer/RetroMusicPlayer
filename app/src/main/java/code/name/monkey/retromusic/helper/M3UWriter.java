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

package code.name.monkey.retromusic.helper;

import android.content.Context;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import code.name.monkey.retromusic.loaders.PlaylistSongsLoader;
import code.name.monkey.retromusic.model.AbsCustomPlaylist;
import code.name.monkey.retromusic.model.Playlist;
import code.name.monkey.retromusic.model.Song;
import io.reactivex.Observable;

public class M3UWriter implements M3UConstants {

    public static Observable<File> write(Context context, File dir, Playlist playlist) throws IOException {
        if (!dir.exists()) //noinspection ResultOfMethodCallIgnored
            dir.mkdirs();
        File file = new File(dir, playlist.name.concat("." + M3UConstants.Companion.getEXTENSION()));

        ArrayList<? extends Song> songs;
        if (playlist instanceof AbsCustomPlaylist) {
            songs = ((AbsCustomPlaylist) playlist).getSongs(context).blockingFirst();
        } else {
            songs = PlaylistSongsLoader.INSTANCE.getPlaylistSongList(context, playlist.id).blockingFirst();
        }


        if (songs.size() > 0) {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));

            bw.write(M3UConstants.Companion.getHEADER());
            for (Song song : songs) {
                bw.newLine();
                bw.write(M3UConstants.Companion.getENTRY() + song.getDuration() + M3UConstants.Companion.getDURATION_SEPARATOR() + song.getArtistName() + " - " + song.getTitle());
                bw.newLine();
                bw.write(song.getData());
            }

            bw.close();
        }

        return Observable.just(file);
    }
}
