package code.name.monkey.retromusic.fragments.backup

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.documentfile.provider.DocumentFile
import android.provider.DocumentsContract
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.adapter.backup.BackupAdapter
import code.name.monkey.retromusic.databinding.FragmentBackupBinding
import code.name.monkey.retromusic.extensions.accentColor
import code.name.monkey.retromusic.extensions.accentOutlineColor
import code.name.monkey.retromusic.extensions.materialDialog
import code.name.monkey.retromusic.extensions.showToast
import code.name.monkey.retromusic.helper.BackupEntry
import code.name.monkey.retromusic.helper.BackupHelper
import code.name.monkey.retromusic.helper.sanitize
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.util.Share
import com.afollestad.materialdialogs.input.input
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BackupFragment : Fragment(R.layout.fragment_backup), BackupAdapter.BackupClickedListener {

    private val backupViewModel by viewModels<BackupViewModel>()
    private var backupAdapter: BackupAdapter? = null

    private var _binding: FragmentBackupBinding? = null
    private val binding get() = _binding!!

    private val pickBackupFolder =
        registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri ->
            if (uri != null) {
                // Keep access after the app restarts or is reinstalled and the folder is picked again.
                requireContext().contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
                PreferenceUtil.backupFolderUri = uri.toString()
                updateFolderRow()
                backupViewModel.loadBackups(requireContext())
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentBackupBinding.bind(view)
        initAdapter()
        setupRecyclerview()
        backupViewModel.backupsLiveData.observe(viewLifecycleOwner) {
            if (it.isNotEmpty())
                backupAdapter?.swapDataset(it)
            else
                backupAdapter?.swapDataset(listOf())
        }
        backupViewModel.loadBackups(requireContext())
        val openFilePicker = registerForActivityResult(ActivityResultContracts.OpenDocument()) {
            lifecycleScope.launch(Dispatchers.IO) {
                it?.let {
                    startActivity(Intent(context, RestoreActivity::class.java).apply {
                        data = it
                    })
                }
            }
        }
        binding.chooseBackupFolder.setOnClickListener {
            pickBackupFolder.launch(null)
        }
        updateFolderRow()
        binding.createBackup.accentOutlineColor()
        binding.restoreBackup.accentColor()
        binding.createBackup.setOnClickListener {
            showCreateBackupDialog()
        }
        binding.restoreBackup.setOnClickListener {
            openFilePicker.launch(arrayOf("application/octet-stream"))
        }
    }

    private fun updateFolderRow() {
        val folder = BackupHelper.backupFolder(requireContext())
        binding.backupFolderPath.text = when {
            PreferenceUtil.backupFolderUri == null -> getString(R.string.backup_folder_not_set)
            folder == null -> getString(R.string.backup_folder_lost)
            else -> folder.name ?: PreferenceUtil.backupFolderUri
        }
    }

    private fun initAdapter() {
        backupAdapter = BackupAdapter(requireActivity(), ArrayList(), this)
        backupAdapter?.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                checkIsEmpty()
            }
        })
    }

    private fun checkIsEmpty() {
        val isEmpty = backupAdapter?.itemCount == 0
        binding.backupTitle.isVisible = !isEmpty
        binding.backupRecyclerview.isVisible = !isEmpty
    }

    fun setupRecyclerview() {
        binding.backupRecyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = backupAdapter
        }
    }

    @SuppressLint("CheckResult")
    private fun showCreateBackupDialog() {
        materialDialog().show {
            title(res = R.string.action_rename)
            input(prefill = BackupHelper.getTimeStamp()) { _, text ->
                // Text submitted with the action button
                lifecycleScope.launch {
                    BackupHelper.createBackup(requireContext(), text.sanitize())
                    backupViewModel.loadBackups(requireContext())
                }
            }
            positiveButton(android.R.string.ok)
            negativeButton(R.string.action_cancel)
            setTitle(R.string.title_new_backup)
        }
    }

    override fun onBackupClicked(entry: BackupEntry) {
        startActivity(Intent(context, RestoreActivity::class.java).apply {
            data = entry.uri
        })
    }

    @SuppressLint("CheckResult")
    override fun onBackupMenuClicked(entry: BackupEntry, menuItem: MenuItem): Boolean {
        val context = requireContext()
        when (menuItem.itemId) {
            R.id.action_delete -> {
                try {
                    if (entry.file != null) {
                        entry.file.delete()
                    } else {
                        DocumentFile.fromSingleUri(context, entry.uri)?.delete()
                    }
                } catch (exception: SecurityException) {
                    showToast(R.string.error_delete_backup)
                }
                backupViewModel.loadBackups(context)
                return true
            }
            R.id.action_share -> {
                if (entry.file != null) {
                    Share.shareFile(context, entry.file, "*/*")
                } else {
                    val send = Intent(Intent.ACTION_SEND).apply {
                        type = "*/*"
                        putExtra(Intent.EXTRA_STREAM, entry.uri)
                        clipData = ClipData.newRawUri("", entry.uri)
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    startActivity(Intent.createChooser(send, null))
                }
                return true
            }
            R.id.action_rename -> {
                materialDialog().show {
                    title(res = R.string.action_rename)
                    input(prefill = entry.name) { _, text ->
                        // Text submitted with the action button
                        val newName = "$text${BackupHelper.APPEND_EXTENSION}"
                        if (entry.file != null) {
                            val renamedFile = java.io.File(entry.file.parent, newName)
                            if (!renamedFile.exists()) {
                                entry.file.renameTo(renamedFile)
                                backupViewModel.loadBackups(context)
                            } else {
                                showToast(R.string.file_already_exists)
                            }
                        } else {
                            val renamed = runCatching {
                                DocumentsContract.renameDocument(
                                    context.contentResolver, entry.uri, newName
                                )
                            }.getOrNull()
                            if (renamed == null) showToast(R.string.file_already_exists)
                            backupViewModel.loadBackups(context)
                        }
                    }
                    positiveButton(android.R.string.ok)
                    negativeButton(R.string.action_cancel)
                    setTitle(R.string.action_rename)
                }
                return true
            }
        }
        return false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}