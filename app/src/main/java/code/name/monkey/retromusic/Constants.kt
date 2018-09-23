package code.name.monkey.retromusic

object Constants {

    val DISCORD_LINK = "https://discord.gg/qTecXXn"

    const val RETRO_MUSIC_PACKAGE_NAME = "code.name.monkey.retromusic"
    const val MUSIC_PACKAGE_NAME = "com.android.music"
    const val ACTION_TOGGLE_PAUSE = "$RETRO_MUSIC_PACKAGE_NAME.togglepause"
    const val ACTION_PLAY = "$RETRO_MUSIC_PACKAGE_NAME.play"
    const val ACTION_PLAY_PLAYLIST = "$RETRO_MUSIC_PACKAGE_NAME.play.playlist"
    const val ACTION_PAUSE = "$RETRO_MUSIC_PACKAGE_NAME.pause"
    const val ACTION_STOP = "$RETRO_MUSIC_PACKAGE_NAME.stop"
    const val ACTION_SKIP = "$RETRO_MUSIC_PACKAGE_NAME.skip"
    const val ACTION_REWIND = "$RETRO_MUSIC_PACKAGE_NAME.rewind"
    const val ACTION_QUIT = "$RETRO_MUSIC_PACKAGE_NAME.quitservice"
    const val INTENT_EXTRA_PLAYLIST = RETRO_MUSIC_PACKAGE_NAME + "intentextra.playlist"
    const val INTENT_EXTRA_SHUFFLE_MODE = "$RETRO_MUSIC_PACKAGE_NAME.intentextra.shufflemode"
    const val APP_WIDGET_UPDATE = "$RETRO_MUSIC_PACKAGE_NAME.appwidgetupdate"
    const val EXTRA_APP_WIDGET_NAME = RETRO_MUSIC_PACKAGE_NAME + "app_widget_name"
    // do not change these three strings as it will break support with other apps (e.g. last.fm scrobbling)
    const val META_CHANGED = "$RETRO_MUSIC_PACKAGE_NAME.metachanged"
    const val QUEUE_CHANGED = "$RETRO_MUSIC_PACKAGE_NAME.queuechanged"
    const val PLAY_STATE_CHANGED = "$RETRO_MUSIC_PACKAGE_NAME.playstatechanged"
    const val REPEAT_MODE_CHANGED = "$RETRO_MUSIC_PACKAGE_NAME.repeatmodechanged"
    const val SHUFFLE_MODE_CHANGED = "$RETRO_MUSIC_PACKAGE_NAME.shufflemodechanged"
    const val MEDIA_STORE_CHANGED = "$RETRO_MUSIC_PACKAGE_NAME.mediastorechanged"
    const val RATE_ON_GOOGLE_PLAY = "https://play.google.com/store/apps/details?id=code.name.monkey.retromusic"
    const val PAYPAL_ME_URL = "https://www.paypal.me/h4h14"
    const val GOOGLE_PLUS_COMMUNITY = "https://plus.google.com/communities/110811566242871492162"
    const val TRANSLATE = "http://monkeycodeapp.oneskyapp.com/collaboration/project?id=238534"
    const val GITHUB_PROJECT = "https://github.com/h4h13/RetroMusicPlayer"
    const val BASE_API_URL_KUGOU = "http://lyrics.kugou.com/"
    const val TELEGRAM_CHANGE_LOG = "https://t.me/retromusiclog"
    const val USER_PROFILE = "profile.jpg"
    const val USER_BANNER = "banner.jpg"
    const val APP_INSTAGRAM_LINK = "https://www.instagram.com/retromusicapp/"
    const val APP_TELEGRAM_LINK = "https://t.me/retromusicapp/"
    const val APP_TWITTER_LINK = "https://twitter.com/retromusicapp"
    const val FAQ_LINK = "https://github.com/h4h13/RetroMusicPlayer/blob/master/FAQ.md"
    const val CAST_SERVER_PORT = 8080
}
