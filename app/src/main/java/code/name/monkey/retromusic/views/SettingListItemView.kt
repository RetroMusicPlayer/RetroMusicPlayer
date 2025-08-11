package code.name.monkey.retromusic.views

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.withStyledAttributes
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.databinding.ListSettingItemViewBinding

/**
 * Created by hemanths on 2019-12-10.
 */
class SettingListItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = -1,
    defStyleRes: Int = -1
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {
    init {
        val binding: ListSettingItemViewBinding =
            ListSettingItemViewBinding.inflate(LayoutInflater.from(context), this, true)
        context.withStyledAttributes(attrs, R.styleable.SettingListItemView) {
            if (hasValue(R.styleable.SettingListItemView_settingListItemIcon)) {
                binding.icon.setImageDrawable(getDrawable(R.styleable.SettingListItemView_settingListItemIcon))
            }
            binding.icon.setIconBackgroundColor(
                getColor(R.styleable.SettingListItemView_settingListItemIconColor, Color.WHITE)
            )
            binding.title.text = getText(R.styleable.SettingListItemView_settingListItemTitle)
            binding.text.text = getText(R.styleable.SettingListItemView_settingListItemText)
        }
    }
}