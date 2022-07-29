package code.name.monkey.retromusic.activities

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import cat.ereza.customactivityoncrash.CustomActivityOnCrash
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.util.FileUtils.createFile
import code.name.monkey.retromusic.util.Share.shareFile
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class ErrorActivity : AppCompatActivity() {
    private val dayFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val reportPrefix = "bug_report-"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(cat.ereza.customactivityoncrash.R.layout.customactivityoncrash_default_error_activity)

        val restartButton =
            findViewById<Button>(cat.ereza.customactivityoncrash.R.id.customactivityoncrash_error_activity_restart_button)

        val config = CustomActivityOnCrash.getConfigFromIntent(intent)
        if (config == null) {
            finish()
            return
        }
        restartButton.setText(cat.ereza.customactivityoncrash.R.string.customactivityoncrash_error_activity_restart_app)
        restartButton.setOnClickListener {
            CustomActivityOnCrash.restartApplication(
                this@ErrorActivity,
                config
            )
        }
        val moreInfoButton =
            findViewById<Button>(cat.ereza.customactivityoncrash.R.id.customactivityoncrash_error_activity_more_info_button)

        moreInfoButton.setOnClickListener { //We retrieve all the error data and show it
            MaterialAlertDialogBuilder(this@ErrorActivity)
                .setTitle(cat.ereza.customactivityoncrash.R.string.customactivityoncrash_error_activity_error_details_title)
                .setMessage(
                    CustomActivityOnCrash.getAllErrorDetailsFromIntent(
                        this@ErrorActivity,
                        intent
                    )
                )
                .setPositiveButton(
                    cat.ereza.customactivityoncrash.R.string.customactivityoncrash_error_activity_error_details_close,
                    null
                )
                .setNeutralButton(
                    R.string.customactivityoncrash_error_activity_error_details_share
                ) { _, _ ->

                    val bugReport = createFile(
                        context = this,
                        "Bug Report",
                        "$reportPrefix${dayFormat.format(Date())}",
                        CustomActivityOnCrash.getAllErrorDetailsFromIntent(
                            this@ErrorActivity,
                            intent
                        ), ".txt"
                    )
                    shareFile(this, bugReport, "text/*")
                }
                .show()
        }
        val errorActivityDrawableId = config.errorDrawable
        val errorImageView =
            findViewById<ImageView>(cat.ereza.customactivityoncrash.R.id.customactivityoncrash_error_activity_image)
        if (errorActivityDrawableId != null) {
            errorImageView.setImageResource(
                errorActivityDrawableId
            )
        }
    }
}