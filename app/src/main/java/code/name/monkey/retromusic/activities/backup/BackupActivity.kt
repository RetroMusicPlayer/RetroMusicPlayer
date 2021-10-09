package code.name.monkey.retromusic.activities.backup

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.activities.base.AbsThemeActivity
import code.name.monkey.retromusic.adapter.backup.BackupAdapter
import code.name.monkey.retromusic.databinding.ActivityBackupBinding
import code.name.monkey.retromusic.helper.BackupHelper
import com.google.android.material.shape.MaterialShapeDrawable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BackupActivity : AbsThemeActivity() {

    private val backupViewModel by viewModels<BackupViewModel>()
    private var backupAdapter: BackupAdapter? = null

    lateinit var binding: ActivityBackupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBackupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.appBarLayout.statusBarForeground =
            MaterialShapeDrawable.createWithElevationOverlay(this)
        binding.toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_black)
        initAdapter()
        setupRecyclerview()
        backupViewModel.backupsLiveData.observe(this) {
            if (it.isNotEmpty())
                backupAdapter?.swapDataset(it)
            else
                backupAdapter?.swapDataset(listOf())
        }
        backupViewModel.loadBackups()
        setupButtons()
    }

    private fun setupButtons() {
        binding.createBackup.setOnClickListener {
            lifecycleScope.launch {
                BackupHelper.createBackup(this@BackupActivity)
                backupViewModel.loadBackups()
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@BackupActivity,
                        "Backup Completed Successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun initAdapter() {
        backupAdapter = BackupAdapter(this@BackupActivity, ArrayList())
        backupAdapter?.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                checkIsEmpty()
            }
        })
    }

    private fun checkIsEmpty() {
        binding.emptyText.setText(R.string.no_backups_found)
        (backupAdapter!!.itemCount == 0).run {
            binding.empty.isVisible = this
            binding.backupTitle.isVisible = this
        }
    }

    fun setupRecyclerview() {
        binding.backupRecyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = backupAdapter
        }
    }
}