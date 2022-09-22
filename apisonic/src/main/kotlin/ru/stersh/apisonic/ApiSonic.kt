package ru.stersh.apisonic

import okhttp3.ResponseBody
import ru.stersh.apisonic.models.*
import ru.stersh.apisonic.network.AuthenticationInterceptor
import ru.stersh.apisonic.network.NetworkFactory

// Time spent: 18h

// TODO
// [] create better tests with mockwebserver
// [] add more api calls
// [] maybe do not re-use models? (artists vs artist)
// [] organize api calls a little better
// [x] create a dope md file with sweet badges
// [x] upload to github
// [x] configure github actions for unit testing
// [] upload to some sort of repository (maven, jitpack)
// [] think about nullability, error handling etc... write test cases
// [] add javadoc
// [] add Wrapper for Ids in form of inline classes?
// [] use token instead of password
// [] check if some fields are missing in the models. like starred for song, album artist.
// [] why do getStarred and getStarred2 return so different results?
// [] hide all methods which arent relevant for a consumer

class ApiSonic(
    private val url: String,
    userName: String,
    password: String,
    apiVersion: String,
    clientId: String
) {

    private val authenticationInterceptor = AuthenticationInterceptor(
        userName, password, apiVersion, clientId
    )
    private val network = NetworkFactory(
        authenticationInterceptor
    )

    private val api = network.createApi(ru.stersh.apisonic.interfaces.Api::class.java, url)

    private fun buildUrlManually(path: String, queryMap: Map<String, Any?>): String {
        val queries = queryMap
            .filter { it.value != null }
            .map { "${it.key}=${it.value}" }
            .joinToString("&")

        return "$url${if (url.endsWith("/")) "" else "/"}$path?${authenticationInterceptor.joinedQueries}&$queries"
    }

    suspend fun ping(): EmptyResponse = api.ping().subsonicResponse

    suspend fun getLicense(): License = api.getLicense().subsonicResponse.license

    suspend fun getArtists(): Artists = api.getArtists().subsonicResponse.artists

    //TODO Test
    suspend fun getGenres(): List<Genre> = api.getGenres().subsonicResponse.genres.genres

    //TODO Test this
    suspend fun getArtist(id: String): Artist = api.getArtist(id).subsonicResponse.artist

    //TODO Test this
    suspend fun getAlbum(id: String): Album = api.getAlbum(id).subsonicResponse.album

    //TODO Test this
    suspend fun getSong(id: String): Song = api.getSong(id).subsonicResponse.song

    //TODO Test this
    suspend fun getVideos(): List<Video> = api.getVideos().subsonicResponse.videos.videos

    //TODO Test this
    suspend fun getVideoInfo(id: String): VideoInfo = api.getVideoInfo(id).subsonicResponse.videoInfo

    //TODO Test this
    suspend fun getArtistInfo(
        id: String,
        count: Int? = null,
        includeNotPresent: Boolean? = null
    ): ArtistInfo = api.getArtistInfo(id, count, includeNotPresent).subsonicResponse.artistInfo

    //TODO Test this
    suspend fun getArtistInfo2(
        id: String,
        count: Int? = null,
        includeNotPresent: Boolean? = null
    ): ArtistInfo = api.getArtistInfo2(id, count, includeNotPresent).subsonicResponse.artistInfo2

    suspend fun getSimilarSongs(
        id: String,
        count: Int? = null
    ): List<Song> {
        return api.getSimilarSongs(id, count).subsonicResponse.similarSongs.similarSongs
    }

    //TODO Test
    suspend fun getSimilarSongs2(
        id: String,
        count: Int? = null
    ): List<Song> {
        return api.getSimilarSongs2(id, count).subsonicResponse.similarSongs.similarSongs
    }

    //TODO Test
    suspend fun getTopSongs(
        artist: String,
        count: Int? = null
    ): List<Song> {
        return api.getTopSongs(artist, count).subsonicResponse.topSongs.topSongs
    }

    //TODO Test
    suspend fun getMusicFolders(): List<MusicFolder> {
        return api.getMusicFolders().subsonicResponse.musicFolders.musicFolders
    }

    //TODO Test
    suspend fun getIndexes(
        musicFolderId: String? = null,
        ifModifiedSince: Long? = null
    ): Indexes {
        return api.getIndexes(musicFolderId, ifModifiedSince).subsonicResponse.indexes
    }

    //TODO test
    suspend fun getMusicDirectory(
        id: String
    ): Directory {
        return api.getMusicDirectory(id).subsonicResponse.directory
    }

    enum class ListType(val value: String) {
        RANDOM("random"),
        NEWEST("newest"),
        HIGHEST("highest"),
        FREQUENT("frequent"),
        RECENT("recent"),
        ALPHABETICAL_BY_NAME("alphabeticalByName"),
        ALPHABETICAL_BY_ARTIST("alphabeticalByArtist"),
        STARRED("starred"),
        BY_YEAR("byYear"),
        BY_GENRE("byGenre"),
    }

    //TODO test
    suspend fun getAlbumList(
        type: ListType,
        size: Int? = null,
        offset: Int? = null,
        fromYear: Int? = null,
        toYear: Int? = null,
        genre: String? = null,
        musicFolderId: String? = null
    ): List<AlbumList.Album> = api.getAlbumList(
        type.value, size, offset, fromYear, toYear, genre, musicFolderId
    ).subsonicResponse.albumList.albums

    //TODO test
    suspend fun getAlbumList2(
        type: ListType,
        size: Int? = null,
        offset: Int? = null,
        fromYear: Int? = null,
        toYear: Int? = null,
        genre: String? = null,
        musicFolderId: String? = null
    ): List<AlbumList2.Album> = api.getAlbumList2(
        type.value, size, offset, fromYear, toYear, genre, musicFolderId
    ).subsonicResponse.albumList2.albums

    //TODO test
    suspend fun getRandomSongs(
        size: Int? = null,
        genre: String? = null,
        fromYear: Int? = null,
        toYear: Int? = null,
        musicFolderId: String? = null
    ): List<Song> = api.getRandomSongs(
        size, genre, fromYear, toYear, musicFolderId
    ).subsonicResponse.randomSongs.randomSongs

    //TODO test
    suspend fun getSongsByGenre(
        genre: String,
        count: Int? = null,
        offset: Int? = null,
        musicFolderId: String? = null
    ): List<Song> = api.getSongsByGenre(
        genre, count, offset, musicFolderId
    ).subsonicResponse.songsByGenre.songsByGenre

    //TODO test
    suspend fun getNowPlaying(): List<NowPlayingEntry> = api.getNowPlaying().subsonicResponse.nowPlaying.entries

    //TODO test
    suspend fun getStarred(
        musicFolderId: String? = null
    ): Starred = api.getStarred(musicFolderId).subsonicResponse.starred

    //TODO test
    suspend fun getStarred2(
        musicFolderId: String? = null
    ): Starred2 = api.getStarred2(musicFolderId).subsonicResponse.starred2

    //TODO TEST
    @Deprecated("Deprecated since 1.4.0, use search2 instead.")
    suspend fun search(
        artist: String? = null,
        album: String? = null,
        title: String? = null,
        any: String? = null,
        count: Int? = null,
        offset: Int? = null,
        newerThan: Long? = null
    ): SearchResult = api.search(artist, album, title, any, count, offset, newerThan).subsonicResponse.searchResult


    //TODO TEST
    suspend fun search2(
        query: String,
        artistCount: Int? = null,
        artistOffset: Int? = null,
        albumCount: Int? = null,
        albumOffset: Int? = null,
        songCount: Int? = null,
        songOffset: Int? = null,
        musicFolderId: String? = null
    ): SearchResult2 = api
        .search2(query, artistCount, artistOffset, albumCount, albumOffset, songCount, songOffset, musicFolderId)
        .subsonicResponse
        .searchResult2

    //TODO TEST
    suspend fun search3(
        query: String,
        artistCount: Int? = null,
        artistOffset: Int? = null,
        albumCount: Int? = null,
        albumOffset: Int? = null,
        songCount: Int? = null,
        songOffset: Int? = null,
        musicFolderId: String? = null
    ): SearchResult3 = api
        .search3(query, artistCount, artistOffset, albumCount, albumOffset, songCount, songOffset, musicFolderId)
        .subsonicResponse
        .searchResult3

    //TODO Test
    suspend fun startScan(): ScanStatus = api.startScan().subsonicResponse.scanStatus

    //Todo Test
    suspend fun getScanStatus(): ScanStatus = api.getScanStatus().subsonicResponse.scanStatus

    //Todo test
    suspend fun starFileOrFolder(
        vararg id: String
    ): EmptyResponse {
        return api.star(id = id.asList()).subsonicResponse
    }

    //Todo test
    suspend fun starAlbum(
        vararg albumId: String
    ): EmptyResponse {
        return api.star(albumIds = albumId.asList()).subsonicResponse
    }

    //Todo test
    suspend fun starArtist(
        vararg artistId: String
    ): EmptyResponse {
        return api.star(artistIds = artistId.asList()).subsonicResponse
    }

    //Todo test
    suspend fun unstarFileOrFolder(
        vararg id: String
    ): EmptyResponse {
        return api.unstar(id = id.asList()).subsonicResponse
    }

    //Todo test
    suspend fun unstarAlbum(vararg albumId: String): EmptyResponse =
        api.unstar(albumIds = albumId.asList()).subsonicResponse

    //Todo test
    suspend fun unstarArtist(vararg artistId: String): EmptyResponse =
        api.unstar(artistIds = artistId.asList()).subsonicResponse

    enum class Rating(val value: Int) {
        REMOVE_RATING(0),
        RATE_1_STAR(1),
        RATE_2_STAR(2),
        RATE_3_STAR(3),
        RATE_4_STAR(4),
        RATE_5_STAR(5)
    }

    //TODO TEST
    suspend fun setRating(
        id: String,
        rating: Rating
    ): EmptyResponse = api.setRating(id, rating.value).subsonicResponse

    //TODO TEST
    suspend fun removeRating(
        id: String
    ): EmptyResponse = api.setRating(id, Rating.REMOVE_RATING.value).subsonicResponse

    //TODO TEST
    suspend fun scrobble(
        id: String,
        time: Long? = null,
        submission: Boolean? = null
    ): EmptyResponse = api.scrobble(id, time, submission).subsonicResponse

    //TODO TEST
    suspend fun getPlaylists(
        username: String? = null
    ): List<Playlists.Playlist> = api.getPlaylists(username).subsonicResponse.playlists.playlists ?: emptyList()

    //TODO TEST
    suspend fun getPlaylist(
        id: String
    ): PlaylistResponse.Playlist = api.getPlaylist(id).subsonicResponse.playlist

    //TODO TEST
    suspend fun createPlaylist(
        name: String,
        songIds: List<String>? = null
    ): EmptyResponse =
        api.createPlaylist(playlistId = null, name = name, songIds = songIds).subsonicResponse

    //TODO TEST
    suspend fun overridePlaylist(
        playlistId: String,
        songIds: List<String>? = null
    ): EmptyResponse =
        api.createPlaylist(playlistId = playlistId, name = null, songIds = songIds).subsonicResponse

    //TODO TEST
    suspend fun overridePlaylist(
        playlistId: String,
        name: String? = null,
        comment: String? = null,
        public: Boolean? = null,
        songIdsToAdd: List<String>? = null,
        songIndicesToRemove: List<String>? = null
    ): EmptyResponse =
        api.updatePlaylist(playlistId, name, comment, public, songIdsToAdd, songIndicesToRemove).subsonicResponse

    //TODO Helper method to remove songIds from playlist, not index

    //TODO TEST
    suspend fun deletePlaylist(
        playlistId: String
    ): EmptyResponse =
        api.deletePlaylist(playlistId).subsonicResponse

    //TODO TEST
    fun streamAudioUrl(
        id: String,
        maxBitRate: Int? = null,
        format: String? = null,
        estimateContentLength: Boolean? = null,
        transcode: Boolean = false
    ): String = buildUrlManually(
        "stream",
        mapOf(
            "id" to id,
            "maxBitRate" to maxBitRate,
            "format" to if (transcode) "raw" else format,
            "estimateContentLength" to estimateContentLength
        )
    )

    //TODO TEST
    fun streamVideoUrl(
        id: String,
        maxBitRate: Int? = null,
        format: String? = null,
        timeOffset: Int? = null,
        size: String? = null,
        estimateContentLength: Boolean? = null,
        converted: Boolean? = null,
        transcode: Boolean = false
    ): String = buildUrlManually(
        "stream",
        mapOf(
            "id" to id,
            "maxBitRate" to maxBitRate,
            "format" to if (transcode) "raw" else format,
            "timeOffset" to timeOffset,
            "size" to size,
            "estimateContentLength" to estimateContentLength,
            "converted" to converted
        )
    )

    //TODO
    fun downloadUrl(
        id: String
    ): String = buildUrlManually("download", mapOf("id" to id))

    //TODO TEST
    fun hlsUrl(
        id: String,
        bitRate: List<String>? = null,
        audioTrack: String?
    ): String = buildUrlManually(
        "hls",
        mapOf(
            "id" to id,
            "audioTrack" to audioTrack,
            *(bitRate?.map { "bitRate" to it }?.toTypedArray() ?: emptyArray())
        )
    )

    //TODO TEST
    fun getCaptionsUrl(
        id: String,
        format: String? = null
    ): String = buildUrlManually(
        "getCaptions",
        mapOf("id" to id, "format" to format)
    )

    //TODO TEST
    fun getLyricsUrl(
        artist: String? = null,
        title: String? = null
    ): String = buildUrlManually(
        "getLyrics",
        mapOf("artist" to artist, "title" to title)
    )

    //TODO TEST
    fun getCoverArtUrl(
        id: String,
        size: Int? = null
    ): String = buildUrlManually(
        "getCoverArt",
        mapOf<String, Any?>("id" to id, "size" to size)
    )

    //TODO TEST
    fun getAvatarUrl(
        username: String
    ): String = buildUrlManually(
        "getAvatar",
        mapOf("username" to username)
    )

    //TODO TEST
    fun streamAudio(
        id: String,
        maxBitRate: Int? = null,
        format: String? = null,
        estimateContentLength: Boolean? = null,
        transcode: Boolean = false
    ): ResponseBody = api.stream(
        id = id,
        maxBitRate = maxBitRate,
        format = if (transcode) "raw" else format,
        estimateContentLength = estimateContentLength,
        timeOffset = null,
        size = null,
        converted = null
    )

    //TODO TEST
    fun streamVideo(
        id: String,
        maxBitRate: Int? = null,
        format: String? = null,
        timeOffset: Int? = null,
        estimateContentLength: Boolean? = null,
        size: String? = null,
        converted: Boolean? = null,
        transcode: Boolean = false
    ): ResponseBody = api.stream(
        id = id,
        maxBitRate = maxBitRate,
        format = if (transcode) "raw" else format,
        estimateContentLength = estimateContentLength,
        timeOffset = timeOffset,
        size = size,
        converted = converted
    )

    //TODO TEST
    suspend fun download(
        id: String
    ): ResponseBody = api.download(id)

    //TODO TEST
    suspend fun hls(
        id: String,
        bitRate: List<String>? = null,
        audioTrack: String?
    ): ResponseBody = api.hls(id, bitRate, audioTrack)

    //TODO TEST
    suspend fun getCaptions(
        id: String,
        format: String? = null
    ): ResponseBody = api.getCaptions(id, format)

    //TODO TEST
    suspend fun getLyrics(
        artist: String? = null,
        title: String? = null
    ): ResponseBody = api.getLyrics(artist, title)

    //TODO TEST
    suspend fun getCoverArt(
        id: String,
        size: Int? = null
    ): ResponseBody = api.getCoverArt(id, size)

    //TODO TEST
    suspend fun getAvatar(
        username: String
    ): ResponseBody = api.getAvatar(username)


//    suspend fun get():  {
//        return api.get().subsonicResponse.
//    }
//    suspend fun get():  {
//        return api.get().subsonicResponse.
//    }
//
//    suspend fun get():  {
//        return api.get().subsonicResponse.
//    }
//    suspend fun get():  {
//        return api.get().subsonicResponse.
//    }
//
//    suspend fun get():  {
//        return api.get().subsonicResponse.
//    }
//    suspend fun get():  {
//        return api.get().subsonicResponse.
//    }
//
//    suspend fun get():  {
//        return api.get().subsonicResponse.
//    }
//    suspend fun get():  {
//        return api.get().subsonicResponse.
//    }
//
//    suspend fun get():  {
//        return api.get().subsonicResponse.
//    }
//    suspend fun get():  {
//        return api.get().subsonicResponse.
//    }
//
//    suspend fun get():  {
//        return api.get().subsonicResponse.
//    }
//    suspend fun get():  {
//        return api.get().subsonicResponse.
//    }
//
//    suspend fun get():  {
//        return api.get().subsonicResponse.
//    }
}
