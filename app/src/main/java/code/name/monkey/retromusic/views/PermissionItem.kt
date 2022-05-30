package code.name.monkey.retromusic.views

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.withStyledAttributes
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.databinding.ItemPermissionBinding
import code.name.monkey.retromusic.extensions.accentOutlineColor

class PermissionItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = -1,
    defStyleRes: Int = -1
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {
    private var binding: ItemPermissionBinding
    val checkImage get() = binding.checkImage

    init {
        binding = ItemPermissionBinding.inflate(LayoutInflater.from(context), this, true)

        context.withStyledAttributes(attrs, R.styleable.PermissionItem, 0, 0) {
            binding.title.text = getText(R.styleable.PermissionItem_permissionTitle)
            binding.summary.text = getText(R.styleable.PermissionItem_permissionTitleSubTitle)
            binding.number.text = getText(R.styleable.PermissionItem_permissionTitleNumber)
            binding.button.text = getText(R.styleable.PermissionItem_permissionButtonTitle)
            binding.button.setIconResource(
                getResourceId(
                    R.styleable.PermissionItem_permissionIcon,
                    R.drawable.ic_album
                )
            )
            val color = ThemeStore.accentColor(context)
            binding.number.backgroundTintList =
                ColorStateList.valueOf(ColorUtil.withAlpha(color, 0.22f))

            if (!isInEditMode) binding.button.accentOutlineColor()
        }
    }

    fun setButtonClick(callBack: () -> Unit) {
        binding.button.setOnClickListener { callBack() }
    }

    fun setNumber(number: String) {
        binding.number.text = number
    }
}