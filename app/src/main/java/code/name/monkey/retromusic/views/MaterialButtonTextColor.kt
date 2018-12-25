package code.name.monkey.retromusic.views

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.ATHUtil
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.appthemehelper.util.MaterialValueHelper
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.util.RetroUtil
import com.google.android.material.button.MaterialButton

class MaterialButtonTextColor @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = -1) : MaterialButton(context, attrs, defStyleAttr) {

    init {
        setTextColor(MaterialValueHelper.getPrimaryTextColor(getContext(), ColorUtil.isColorLight(ThemeStore.primaryColor(getContext()))))
        iconTint = ColorStateList.valueOf(ATHUtil.resolveColor(context, R.attr.iconColor))
        iconPadding = RetroUtil.convertDpToPixel(16f, getContext()).toInt()
        rippleColor = ColorStateList.valueOf(ColorUtil.withAlpha(ThemeStore.accentColor(context), 0.4f))
        minHeight = RetroUtil.convertDpToPixel(52f, context).toInt()
    }
}
