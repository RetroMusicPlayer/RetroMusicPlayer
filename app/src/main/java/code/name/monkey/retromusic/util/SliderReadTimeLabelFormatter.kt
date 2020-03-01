package code.name.monkey.retromusic.util

import com.google.android.material.slider.Slider
import java.util.*

class SliderReadTimeLabelFormatter : Slider.LabelFormatter {

    override fun getFormattedValue(value: Float): String {
        var minutes: Long = value.toLong() / 1000 / 60
        val seconds: Long = value.toLong() / 1000 % 60
        return if (minutes < 60) {
            String.format(
                Locale.getDefault(),
                "%01d:%02d",
                minutes,
                seconds
            )
        } else {
            val hours = minutes / 60
            minutes %= 60
            String.format(
                Locale.getDefault(),
                "%d:%02d:%02d",
                hours,
                minutes,
                seconds
            )
        }
    }
}