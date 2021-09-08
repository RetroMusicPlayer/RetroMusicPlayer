package code.name.monkey.retromusic.views

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
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
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.PermissionItem, 0, 0)
        binding = ItemPermissionBinding.inflate(LayoutInflater.from(context))
        addView(binding.root)

        binding.title.text = attributes.getText(R.styleable.PermissionItem_permissionTitle)
        binding.summary.text =
            attributes.getText(R.styleable.PermissionItem_permissionTitleSubTitle)
        binding.number.text = attributes.getText(R.styleable.PermissionItem_permissionTitleNumber)
        binding.button.text = attributes.getText(R.styleable.PermissionItem_permissionButtonTitle)
        binding.button.setIconResource(
            attributes.getResourceId(
                R.styleable.PermissionItem_permissionIcon,
                R.drawable.ic_album
            )
        )
        val color = ThemeStore.accentColor(context)
        binding.number.backgroundTintList =
            ColorStateList.valueOf(ColorUtil.withAlpha(color, 0.22f))

        binding.button.accentOutlineColor()
        attributes.recycle()
    }

    fun setButtonClick(callBack: () -> Unit) {
        binding.button.setOnClickListener { callBack.invoke() }
    }
}