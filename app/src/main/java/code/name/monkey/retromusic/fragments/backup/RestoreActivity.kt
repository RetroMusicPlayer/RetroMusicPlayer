package code.name.monkey.retromusic.fragments.backup

import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import code.name.monkey.retromusic.databinding.ActivityRestoreBinding
import code.name.monkey.retromusic.helper.BackupContent
import code.name.monkey.retromusic.helper.BackupContent.*
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.util.theme.ThemeManager
import com.google.android.material.color.DynamicColors
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
        val backupUri = intent?.data
        binding.backupName.setText(getFileName(backupUri))
        binding.cancelButton.setOnClickListener {
            finish()
        }
        binding.restoreButton.setOnClickListener {
            val backupContents = mutableListOf<BackupContent>()
            if (binding.checkSettings.isChecked) backupContents.add(SETTINGS)
            if (binding.checkQueue.isChecked) backupContents.add(QUEUE)
            if (binding.checkDatabases.isChecked) backupContents.add(PLAYLISTS)
            if (binding.checkArtistImages.isChecked) backupContents.add(CUSTOM_ARTIST_IMAGES)
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
        AppCompatDelegate.setDefaultNightMode(ThemeManager.getNightMode(this))

        // Apply dynamic colors to activity if enabled
        if (PreferenceUtil.materialYou) {
            DynamicColors.applyIfAvailable(
                this,
                com.google.android.material.R.style.ThemeOverlay_Material3_DynamicColors_DayNight
            )
        }
    }

    private fun getFileName(uri: Uri?): String? {
        when (uri?.scheme) {
            "file" -> {
                return uri.lastPathSegment
            }
            "content" -> {
                val proj = arrayOf(MediaStore.Images.Media.TITLE)
                contentResolver.query(
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                        MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
                    } else {
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }, proj, null, null, null
                )?.use { cursor ->
                    if (cursor.count != 0) {
                        val columnIndex: Int =
                            cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE)
                        cursor.moveToFirst()
                        return cursor.getString(columnIndex)
                    }
                }
            }
        }
        return "Backup"
    }
}