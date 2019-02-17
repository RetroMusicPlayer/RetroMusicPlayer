package code.name.monkey.retromusic.preferences

import android.content.Context
import android.util.AttributeSet
import androidx.preference.DialogPreference


class AlbumCoverStylePreference : DialogPreference {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)
}