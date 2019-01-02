package code.name.monkey.retromusic

import android.provider.BaseColumns
import android.provider.MediaStore

object Constants {

    @JvmField
    val DISCORD_LINK = "https://discord.gg/qTecXXn"

    @JvmField
    val RETRO_MUSIC_PACKAGE_NAME = "code.name.monkey.retromusic"
    @JvmField
    val MUSIC_PACKAGE_NAME = "com.android.music"
    @JvmField
    val ACTION_TOGGLE_PAUSE = "$RETRO_MUSIC_PACKAGE_NAME.togglepause"
    @JvmField
    val ACTION_PLAY = "$RETRO_MUSIC_PACKAGE_NAME.play"
    @JvmField
    val ACTION_PLAY_PLAYLIST = "$RETRO_MUSIC_PACKAGE_NAME.play.playlist"
    @JvmField
    val ACTION_PAUSE = "$RETRO_MUSIC_PACKAGE_NAME.pause"
    @JvmField
    val ACTION_STOP = "$RETRO_MUSIC_PACKAGE_NAME.stop"
    @JvmField
    val ACTION_SKIP = "$RETRO_MUSIC_PACKAGE_NAME.skip"
    @JvmField
    val ACTION_REWIND = "$RETRO_MUSIC_PACKAGE_NAME.rewind"
    @JvmField
    val ACTION_QUIT = "$RETRO_MUSIC_PACKAGE_NAME.quitservice"
    @JvmField
    val INTENT_EXTRA_PLAYLIST = RETRO_MUSIC_PACKAGE_NAME + "intentextra.playlist"
    @JvmField
    val INTENT_EXTRA_SHUFFLE_MODE = "$RETRO_MUSIC_PACKAGE_NAME.intentextra.shufflemode"
    @JvmField
    val APP_WIDGET_UPDATE = "$RETRO_MUSIC_PACKAGE_NAME.appwidgetupdate"
    @JvmField
    val EXTRA_APP_WIDGET_NAME = RETRO_MUSIC_PACKAGE_NAME + "app_widget_name"

    @JvmField
    val META_CHANGED = "$RETRO_MUSIC_PACKAGE_NAME.metachanged"
    @JvmField
    val QUEUE_CHANGED = "$RETRO_MUSIC_PACKAGE_NAME.queuechanged"
    @JvmField
    val PLAY_STATE_CHANGED = "$RETRO_MUSIC_PACKAGE_NAME.playstatechanged"
    @JvmField
    val REPEAT_MODE_CHANGED = "$RETRO_MUSIC_PACKAGE_NAME.repeatmodechanged"
    @JvmField
    val SHUFFLE_MODE_CHANGED = "$RETRO_MUSIC_PACKAGE_NAME.shufflemodechanged"
    @JvmField
    val MEDIA_STORE_CHANGED = "$RETRO_MUSIC_PACKAGE_NAME.mediastorechanged"
    @JvmField
    val RATE_ON_GOOGLE_PLAY = "https://play.google.com/store/apps/details?id=code.name.monkey.retromusic"
    @JvmField
    val PAYPAL_ME_URL = "https://www.paypal.me/h4h14"
    @JvmField
    val GOOGLE_PLUS_COMMUNITY = "https://plus.google.com/communities/110811566242871492162"
    @JvmField
    val TRANSLATE = "http://monkeycodeapp.oneskyapp.com/collaboration/project?id=238534"
    @JvmField
    val GITHUB_PROJECT = "https://github.com/h4h13/RetroMusicPlayer"
    @JvmField
    val BASE_API_URL_KUGOU = "http://lyrics.kugou.com/"
    @JvmField
    val TELEGRAM_CHANGE_LOG = "https://t.me/retromusiclog"
    @JvmField
    val USER_PROFILE = "profile.jpg"
    @JvmField
    val USER_BANNER = "banner.jpg"
    @JvmField
    val APP_INSTAGRAM_LINK = "https://www.instagram.com/retromusicapp/"
    @JvmField
    val APP_TELEGRAM_LINK = "https://t.me/retromusicapp/"
    @JvmField
    val APP_TWITTER_LINK = "https://twitter.com/retromusicapp"
    @JvmField
    val FAQ_LINK = "https://github.com/h4h13/RetroMusicPlayer/blob/master/FAQ.md"
    @JvmField
    val PINTEREST = "https://in.pinterest.com/retromusicapp/"
    @JvmField
    val CAST_SERVER_PORT = 8080

    const val BASE_SELECTION = MediaStore.Audio.AudioColumns.IS_MUSIC + "=1" + " AND " + MediaStore.Audio.AudioColumns.TITLE + " != ''"
    @JvmField
    val BASE_PROJECTION = arrayOf(BaseColumns._ID, // 0
            MediaStore.Audio.AudioColumns.TITLE, // 1
            MediaStore.Audio.AudioColumns.TRACK, // 2
            MediaStore.Audio.AudioColumns.YEAR, // 3
            MediaStore.Audio.AudioColumns.DURATION, // 4
            MediaStore.Audio.AudioColumns.DATA, // 5
            MediaStore.Audio.AudioColumns.DATE_MODIFIED, // 6
            MediaStore.Audio.AudioColumns.ALBUM_ID, // 7
            MediaStore.Audio.AudioColumns.ALBUM, // 8
            MediaStore.Audio.AudioColumns.ARTIST_ID, // 9
            MediaStore.Audio.AudioColumns.ARTIST)// 10
    const val NUMBER_OF_TOP_TRACKS = 99


}
