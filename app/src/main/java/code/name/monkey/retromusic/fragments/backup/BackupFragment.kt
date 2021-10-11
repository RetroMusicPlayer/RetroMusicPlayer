package code.name.monkey.retromusic.fragments.backup

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.adapter.backup.BackupAdapter
import code.name.monkey.retromusic.databinding.FragmentBackupBinding
import code.name.monkey.retromusic.helper.BackupHelper
import kotlinx.coroutines.launch
import java.io.File

class BackupFragment : Fragment(R.layout.fragment_backup), BackupAdapter.BackupClickedListener {

    private val backupViewModel by viewModels<BackupViewModel>()
    private var backupAdapter: BackupAdapter? = null

    private var _binding: FragmentBackupBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentBackupBinding.bind(view)
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
                BackupHelper.createBackup(requireContext())
                backupViewModel.loadBackups()
            }
        }
    }

    private fun initAdapter() {
        backupAdapter = BackupAdapter(requireContext(), ArrayList(), this)
        backupAdapter?.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                checkIsEmpty()
            }
        })
    }

    private fun checkIsEmpty() {
        val isEmpty = backupAdapter!!.itemCount == 0
        binding.empty.isVisible = isEmpty
        binding.backupTitle.isVisible = !isEmpty
        binding.backupRecyclerview.isVisible = !isEmpty
    }

    fun setupRecyclerview() {
        binding.backupRecyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = backupAdapter
        }
    }

    override fun onBackupClicked(file: File) {
        lifecycleScope.launch {
            backupViewModel.restoreBackup(requireActivity(), file)
        }
    }
}