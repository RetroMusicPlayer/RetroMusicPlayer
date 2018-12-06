package code.name.monkey.retromusic.preferences

import android.content.Context
import android.util.AttributeSet

import code.name.monkey.appthemehelper.common.prefs.supportv7.ATEDialogPreference


class NowPlayingScreenPreference : ATEDialogPreference {
    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {}
}