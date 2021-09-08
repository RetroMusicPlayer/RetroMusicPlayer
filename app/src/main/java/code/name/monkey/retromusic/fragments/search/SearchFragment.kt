/*
 * Copyright (c) 2020 Hemanth Savarla.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 */
package code.name.monkey.retromusic.fragments.search

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.CompoundButton
import androidx.core.view.children
import androidx.core.view.doOnPreDraw
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.adapter.SearchAdapter
import code.name.monkey.retromusic.databinding.FragmentSearchBinding
import code.name.monkey.retromusic.extensions.*
import code.name.monkey.retromusic.fragments.base.AbsMainActivityFragment
import com.google.android.material.chip.Chip
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.transition.MaterialSharedAxis
import java.util.*
import kotlin.collections.ArrayList

class SearchFragment : AbsMainActivityFragment(R.layout.fragment_search), TextWatcher,
    CompoundButton.OnCheckedChangeListener {
    companion object {
        const val QUERY = "query"
        const val REQ_CODE_SPEECH_INPUT = 9001
    }

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var searchAdapter: SearchAdapter
    private var query: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).addTarget(view)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
        _binding = FragmentSearchBinding.bind(view)
        mainActivity.setSupportActionBar(binding.toolbar)
        libraryViewModel.clearSearchResult()
        setupRecyclerView()

        binding.voiceSearch.setOnClickListener { startMicSearch() }
        binding.clearText.setOnClickListener { binding.searchView.clearText() }
        binding.searchView.apply {
            addTextChangedListener(this@SearchFragment)
            focusAndShowKeyboard()
        }
        binding.keyboardPopup.apply {
            accentColor()
            setOnClickListener {
                binding.searchView.focusAndShowKeyboard()
            }
        }
        if (savedInstanceState != null) {
            query = savedInstanceState.getString(QUERY)
        }
        libraryViewModel.getSearchResult().observe(viewLifecycleOwner, {
            showData(it)
        })
        setupChips()
        postponeEnterTransition()
        view.doOnPreDraw {
            startPostponedEnterTransition()
        }
    }

    private fun setupChips() {
        val chips = binding.searchFilterGroup.children.map { it as Chip }
        val states = arrayOf(
            intArrayOf(-android.R.attr.state_checked),
            intArrayOf(android.R.attr.state_checked)
        )

        val colors = intArrayOf(
            android.R.color.transparent,
            ThemeStore.accentColor(requireContext())
        )

        chips.forEach {
            it.chipBackgroundColor = ColorStateList(states, colors)
            it.chipIconTint = ColorStateList.valueOf(ThemeStore.textColorPrimary(requireContext()))
            it.chipStrokeColor =
                ColorStateList.valueOf(ThemeStore.textColorSecondary(requireContext()))
                    .withAlpha(30)
            it.closeIconTint =
                ColorStateList.valueOf(ThemeStore.textColorPrimaryInverse(requireContext()))
            it.chipStrokeWidth = 2F
            it.setOnCheckedChangeListener(this)
        }
    }

    private fun showData(data: List<Any>) {
        if (data.isNotEmpty()) {
            searchAdapter.swapDataSet(data)
        } else {
            searchAdapter.swapDataSet(ArrayList())
        }
    }

    private fun setupRecyclerView() {
        searchAdapter = SearchAdapter(requireActivity(), emptyList())
        searchAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                binding.empty.isVisible = searchAdapter.itemCount < 1
                val height = dipToPix(52f)
                binding.recyclerView.setPadding(0, 0, 0, height.toInt())
            }
        })
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = searchAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy > 0) {
                        binding.keyboardPopup.shrink()
                    } else if (dy < 0) {
                        binding.keyboardPopup.extend()
                    }
                }
            })
        }
    }

    override fun afterTextChanged(newText: Editable?) {
        if (!newText.isNullOrEmpty()) search(newText.toString())
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    private fun search(query: String) {
        this.query = query
        TransitionManager.beginDelayedTransition(binding.appBarLayout)
        binding.voiceSearch.isGone = query.isNotEmpty()
        binding.clearText.isVisible = query.isNotEmpty()
        val filters = getFilters()
        libraryViewModel.search(query, filters)
    }

    private fun getFilters(): List<Boolean> {
        return binding.searchFilterGroup.children.toList().map {
            (it as Chip).isChecked
        }
    }

    private fun startMicSearch() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt))
        try {
            startActivityForResult(
                intent,
                REQ_CODE_SPEECH_INPUT
            )
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
            showToast(getString(R.string.speech_not_supported))
        }
    }

    override fun onDestroyView() {
        hideKeyboard(view)
        super.onDestroyView()
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        hideKeyboard(view)
    }

    override fun onResume() {
        super.onResume()
        mainActivity.setBottomBarVisibility(false)
    }

    private fun hideKeyboard(view: View?) {
        if (view != null) {
            val imm: InputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        val checkedChip = (buttonView as Chip)
        checkedChip.isCloseIconVisible = isChecked
        if (isChecked) {
            val color = ThemeStore.textColorPrimaryInverse(requireContext())
            checkedChip.apply {
                setTextColor(color)
                chipIconTint = ColorStateList.valueOf(color)
                chipStrokeWidth = 0F
            }
        } else {
            val color = ThemeStore.textColorPrimary(requireContext())
            checkedChip.apply {
                setTextColor(color)
                chipIconTint = ColorStateList.valueOf(color)
                chipStrokeWidth = 2F
            }
        }
        search(binding.searchView.text.toString())
    }
}

fun TextInputEditText.clearText() {
    text = null
}
