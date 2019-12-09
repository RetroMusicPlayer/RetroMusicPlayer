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
package code.name.monkey.retromusic.service;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.media.MediaDescription;
import android.media.browse.MediaBrowser;
import android.media.session.MediaSession;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.service.media.MediaBrowserService;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.helper.MusicPlayerRemote;
import code.name.monkey.retromusic.loaders.AlbumLoader;
import code.name.monkey.retromusic.loaders.ArtistLoader;
import code.name.monkey.retromusic.loaders.PlaylistLoader;
import code.name.monkey.retromusic.loaders.PlaylistSongsLoader;
import code.name.monkey.retromusic.loaders.SongLoader;
import code.name.monkey.retromusic.model.Album;
import code.name.monkey.retromusic.model.Artist;
import code.name.monkey.retromusic.model.Playlist;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.util.RetroUtil;

/**
 * @author Hemanth S (h4h13).
 */
@TargetApi(21)
public class WearBrowserService extends MediaBrowserService {

    public static final String MEDIA_ID_ROOT = "__ROOT__";
    public static final int TYPE_ARTIST = 0;
    public static final int TYPE_ALBUM = 1;
    public static final int TYPE_SONG = 2;
    public static final int TYPE_PLAYLIST = 3;
    public static final int TYPE_ARTIST_SONG_ALBUMS = 4;
    public static final int TYPE_ALBUM_SONGS = 5;
    public static final int TYPE_ARTIST_ALL_SONGS = 6;
    public static final int TYPE_PLAYLIST_ALL_SONGS = 7;

    public static WearBrowserService sInstance;
    private MediaSession mSession;
    private Context mContext;
    private boolean mServiceStarted;

    public static WearBrowserService getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        mContext = this;
        mSession = new MediaSession(this, "WearBrowserService");
        setSessionToken(mSession.getSessionToken());
        mSession.setCallback(new MediaSessionCallback());
        mSession.setFlags(MediaSession.FLAG_HANDLES_MEDIA_BUTTONS | MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS);
    }

    @Override
    public int onStartCommand(Intent startIntent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        mServiceStarted = false;
        mSession.release();
    }

    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String s, int i, @Nullable Bundle bundle) {
        return new BrowserRoot(MEDIA_ID_ROOT, null);
    }

    @Override
    public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaBrowser.MediaItem>> result) {
        result.detach();
        loadChildren(parentId, result);
    }

    private void setSessionActive() {
        if (!mServiceStarted) {
            startService(new Intent(getApplicationContext(), WearBrowserService.class));
            mServiceStarted = true;
        }

        if (!mSession.isActive()) {
            mSession.setActive(true);
        }
    }

    private void setSessionInactive() {
        if (mServiceStarted) {
            stopSelf();
            mServiceStarted = false;
        }

        if (mSession.isActive()) {
            mSession.setActive(false);
        }
    }

    private void fillMediaItems(List<MediaBrowser.MediaItem> mediaItems,
                                String mediaId,
                                String title,
                                String subTitle,
                                Uri icon,
                                int playableOrBrowsable) {
        mediaItems.add(new MediaBrowser.MediaItem(
                new MediaDescription.Builder()
                        .setMediaId(mediaId)
                        .setTitle(title)
                        .setIconUri(icon)
                        .setSubtitle(subTitle)
                        .build(), playableOrBrowsable
        ));
    }

    private void addMediaRoots(List<MediaBrowser.MediaItem> mMediaRoot) {
        mMediaRoot.add(new MediaBrowser.MediaItem(
                new MediaDescription.Builder()
                        .setMediaId(Integer.toString(TYPE_ARTIST))
                        .setTitle(getString(R.string.artists))
                        .setIconBitmap(RetroUtil.createBitmap(ContextCompat.getDrawable(getApplicationContext(), R.drawable.default_artist_art), 1f))
                        .setSubtitle(getString(R.string.artists))
                        .build(), MediaBrowser.MediaItem.FLAG_BROWSABLE
        ));

        mMediaRoot.add(new MediaBrowser.MediaItem(
                new MediaDescription.Builder()
                        .setMediaId(Integer.toString(TYPE_ALBUM))
                        .setTitle(getString(R.string.albums))
                        .setIconBitmap(RetroUtil.createBitmap(ContextCompat.getDrawable(getApplicationContext(), R.drawable.default_album_art), 1f))
                        .setSubtitle(getString(R.string.albums))
                        .build(), MediaBrowser.MediaItem.FLAG_BROWSABLE
        ));

        mMediaRoot.add(new MediaBrowser.MediaItem(
                new MediaDescription.Builder()
                        .setMediaId(Integer.toString(TYPE_SONG))
                        .setTitle(getString(R.string.songs))
                        .setIconBitmap(RetroUtil.createBitmap(ContextCompat.getDrawable(getApplicationContext(), R.drawable.default_album_art), 1f))
                        .setSubtitle(getString(R.string.songs))
                        .build(), MediaBrowser.MediaItem.FLAG_BROWSABLE
        ));


        mMediaRoot.add(new MediaBrowser.MediaItem(
                new MediaDescription.Builder()
                        .setMediaId(Integer.toString(TYPE_PLAYLIST))
                        .setTitle(getString(R.string.playlists))
                        .setIconUri(Uri.parse("android.resource://code.name.monkey.retromusic/drawable/ic_queue_music_white_24dp"))
                        .setSubtitle(getString(R.string.playlists))
                        .build(), MediaBrowser.MediaItem.FLAG_BROWSABLE
        ));

    }

    private void loadChildren(final String parentId, final Result<List<MediaBrowser.MediaItem>> result) {

        final List<MediaBrowser.MediaItem> mediaItems = new ArrayList<>();


        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(final Void... unused) {

                if (parentId.equals(MEDIA_ID_ROOT)) {
                    addMediaRoots(mediaItems);
                } else {
                    switch (Integer.parseInt(Character.toString(parentId.charAt(0)))) {
                        case TYPE_ARTIST:
                            List<Artist> artistList = ArtistLoader.INSTANCE.getAllArtists(mContext) ;
                            for (Artist artist : artistList) {
                                String albumNmber = String.format("%d %s", artist.getAlbums().size(), artist.getAlbums().size() > 1 ? "Albums" : "Album");
                                String songCount = String.format("%d %s", artist.getSongs().size(), artist.getSongs().size() > 1 ? "Songs" : "Song");
                                fillMediaItems(mediaItems,
                                        Integer.toString(TYPE_ARTIST_SONG_ALBUMS) + Long.toString(artist.getId()),
                                        artist.getName(),
                                        albumNmber + " • " + songCount,
                                        Uri.parse("android.resource://code.name.monkey.retromusic/drawable/default_artist_art"),
                                        MediaBrowser.MediaItem.FLAG_BROWSABLE);
                            }
                            break;

                        case TYPE_ARTIST_SONG_ALBUMS:
                            fillMediaItems(mediaItems,
                                    Integer.toString(TYPE_ARTIST_ALL_SONGS) + Long.parseLong(parentId.substring(1)),
                                    "All songs",
                                    "All songs by artist",
                                    Uri.parse("android.resource://code.name.monkey.retromusic/drawable/default_artist_art"),
                                    MediaBrowser.MediaItem.FLAG_BROWSABLE);

                            List<Album> artistAlbums = ArtistLoader.INSTANCE.getArtist(mContext, Integer.parseInt(parentId.substring(1))).getAlbums(); //ArtistAlbumLoader.getAlbumsForArtist(mContext, Long.parseLong(parentId.substring(1)));
                            for (Album album : artistAlbums) {
                                String songCount = String.format("%d %s", album.getSongs().size(), album.getSongs().size() > 1 ? "Songs" : "Song");
                                fillMediaItems(mediaItems,
                                        Integer.toString(TYPE_ALBUM_SONGS) + Long.toString(album.getId()),
                                        album.getTitle(),
                                        songCount,
                                        Uri.parse("android.resource://code.name.monkey.retromusic/drawable/default_artist_art"),
                                        MediaBrowser.MediaItem.FLAG_BROWSABLE);
                            }
                            break;
                        case TYPE_ALBUM:
                            List<Album> albumList = AlbumLoader.INSTANCE.getAllAlbums(mContext);
                            for (Album album : albumList) {
                                fillMediaItems(mediaItems,
                                        Integer.toString(TYPE_ALBUM_SONGS) + Long.toString(album.getId()),
                                        album.getTitle(),
                                        album.getArtistName(),
                                        RetroUtil.getAlbumArtUri(album.getId()),
                                        MediaBrowser.MediaItem.FLAG_BROWSABLE);
                            }
                            break;
                        case TYPE_SONG:
                            List<Song> songList = SongLoader.INSTANCE.getAllSongs(mContext);
                            for (Song song : songList) {
                                fillMediaItems(mediaItems,
                                        String.valueOf(song.getId()),
                                        song.getTitle(),
                                        song.getAlbumName(),
                                        Uri.parse("android.resource://code.name.monkey.retromusic/drawable/default_album_art"),
                                        MediaBrowser.MediaItem.FLAG_PLAYABLE);
                            }
                            break;

                        case TYPE_ALBUM_SONGS:
                            List<Song> albumSongList = AlbumLoader.INSTANCE.getAlbum(mContext, Integer.parseInt(parentId.substring(1))).getSongs();
                            for (Song song : albumSongList) {
                                fillMediaItems(mediaItems,
                                        String.valueOf(song.getId()),
                                        song.getTitle(),
                                        song.getAlbumName(),
                                        Uri.parse("android.resource://code.name.monkey.retromusic/drawable/default_album_art"),
                                        MediaBrowser.MediaItem.FLAG_PLAYABLE);
                            }
                            break;
                        case TYPE_ARTIST_ALL_SONGS:
                            List<Song> artistSongs = ArtistLoader.INSTANCE.getArtist(mContext, Integer.parseInt(parentId.substring(1))).getSongs();
                            for (Song song : artistSongs) {
                                fillMediaItems(mediaItems,
                                        String.valueOf(song.getId()),
                                        song.getTitle(),
                                        song.getAlbumName(),
                                        Uri.parse("android.resource://code.name.monkey.retromusic/drawable/default_album_art"),
                                        MediaBrowser.MediaItem.FLAG_PLAYABLE);
                            }
                            break;
                        case TYPE_PLAYLIST:
                            List<Playlist> playlistList = PlaylistLoader.INSTANCE.getAllPlaylists(mContext);
                            for (Playlist playlist : playlistList) {
                                int size = PlaylistSongsLoader.INSTANCE.getPlaylistSongList(mContext, playlist).size();
                                String songCount = String.format("%d %s", size, size > 1 ? "Songs" : "Song");
                                fillMediaItems(mediaItems,
                                        Integer.toString(TYPE_PLAYLIST_ALL_SONGS) + Long.toString(playlist.id),
                                        playlist.name,
                                        songCount,
                                        Uri.parse("android.resource://code.name.monkey.retromusic/drawable/ic_queue_music_white_24dp"),
                                        MediaBrowser.MediaItem.FLAG_BROWSABLE);
                            }
                            break;
                        case TYPE_PLAYLIST_ALL_SONGS:
                            List<Song> playlistSongs = PlaylistSongsLoader.INSTANCE.getPlaylistSongList(mContext, Integer.parseInt(parentId.substring(1)));
                            for (Song song : playlistSongs) {
                                fillMediaItems(mediaItems,
                                        String.valueOf(song.getId()),
                                        song.getTitle(),
                                        song.getAlbumName(),
                                        Uri.parse("android.resource://code.name.monkey.retromusic/drawable/default_album_art"),
                                        MediaBrowser.MediaItem.FLAG_PLAYABLE);
                            }
                            break;

                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                result.sendResult(mediaItems);
            }
        }.execute();

    }

    private final class MediaSessionCallback extends MediaSession.Callback {

        @Override
        public void onPlay() {
            setSessionActive();
        }

        @Override
        public void onSeekTo(long position) {

        }

        @Override
        public void onPlayFromMediaId(final String mediaId, Bundle extras) {
            long songId = Long.parseLong(mediaId);
            setSessionActive();
            ArrayList<Song> songs = new ArrayList<>();
            songs.add(SongLoader.INSTANCE.getSong(mContext, Integer.parseInt(mediaId)));
            MusicPlayerRemote.INSTANCE.openQueue(songs, 0, true);
        }

        @Override
        public void onPause() {

        }

        @Override
        public void onStop() {
            setSessionInactive();
        }

        @Override
        public void onSkipToNext() {

        }

        @Override
        public void onSkipToPrevious() {

        }

        @Override
        public void onFastForward() {

        }

        @Override
        public void onRewind() {

        }

        @Override
        public void onCustomAction(@NonNull String action, Bundle extras) {

        }
    }
}
