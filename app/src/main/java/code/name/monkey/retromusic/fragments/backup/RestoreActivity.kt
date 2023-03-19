package code.name.monkey.retromusic.fragments.backup

import android.content.ContentResolver
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.lifecycleScope
import code.name.monkey.retromusic.databinding.ActivityRestoreBinding
import code.name.monkey.retromusic.extensions.accentColor
import code.name.monkey.retromusic.extensions.accentOutlineColor
import code.name.monkey.retromusic.extensions.addAccentColor
import code.name.monkey.retromusic.helper.BackupContent
import code.name.monkey.retromusic.helper.BackupContent.*
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.util.theme.getNightMode
import com.google.android.material.color.DynamicColors
import com.google.android.material.color.DynamicColorsOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class RestoreActivity : AppCompatActivity() {

    lateinit var binding: ActivityRestoreBinding
    private val backupViewModel: BackupViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        updateTheme()
        super.onCreate(savedInstanceState)
        binding = ActivityRestoreBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setWidth()
        val backupUri = intent?.data
        binding.backupName.setText(getFileName(backupUri))
        binding.cancelButton.accentOutlineColor()
        binding.cancelButton.setOnClickListener {
            finish()
        }
        binding.restoreButton.accentColor()
        binding.checkArtistImages.addAccentColor()
        binding.checkPlaylists.addAccentColor()
        binding.checkSettings.addAccentColor()
        binding.checkUserImages.addAccentColor()
        binding.restoreButton.setOnClickListener {
            val backupContents = mutableListOf<BackupContent>()
            if (binding.checkPlaylists.isChecked) backupContents.add(PLAYLISTS)
            if (binding.checkArtistImages.isChecked) backupContents.add(CUSTOM_ARTIST_IMAGES)
            if (binding.checkSettings.isChecked) backupContents.add(SETTINGS)
            if (binding.checkUserImages.isChecked) backupContents.add(USER_IMAGES)
            lifecycleScope.launch(Dispatchers.IO) {
                if (backupUri != null) {
                    contentResolver.openInputStream(backupUri)?.use {
                        backupViewModel.restoreBackup(this@RestoreActivity, it, backupContents)
                    }
                }
            }
        }
    }

    private fun updateTheme() {
        AppCompatDelegate.setDefaultNightMode(getNightMode())

        // Apply dynamic colors to activity if enabled
        if (PreferenceUtil.materialYou) {
            DynamicColors.applyToActivityIfAvailable(
                this,
                DynamicColorsOptions.Builder()
                    .setThemeOverlay(com.google.android.material.R.style.ThemeOverlay_Material3_DynamicColors_DayNight)
                    .build()
            )
        }
    }

    private fun getFileName(uri: Uri?): String? {
        when (uri?.scheme) {
            ContentResolver.SCHEME_FILE -> {
                return uri.lastPathSegment
            }
            ContentResolver.SCHEME_CONTENT -> {
                val proj = arrayOf(MediaStore.Files.FileColumns.DISPLAY_NAME)
                contentResolver.query(
                    uri, proj, null, null, null
                )?.use { cursor ->
                    if (cursor.count != 0) {
                        val columnIndex: Int =
                            cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)
                        cursor.moveToFirst()
                        return cursor.getString(columnIndex)
                    }
                }
            }
        }
        return "Backup"
    }

    private fun setWidth() {
        val width = resources.displayMetrics.widthPixels * 0.8
        binding.root.updateLayoutParams<ViewGroup.LayoutParams> { this.width = width.toInt() }
    }
}