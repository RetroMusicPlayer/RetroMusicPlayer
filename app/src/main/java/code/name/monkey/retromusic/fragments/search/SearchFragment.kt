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
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.adapter.SearchAdapter
import code.name.monkey.retromusic.extensions.accentColor
import code.name.monkey.retromusic.extensions.dipToPix
import code.name.monkey.retromusic.extensions.focusAndShowKeyboard
import code.name.monkey.retromusic.extensions.showToast
import code.name.monkey.retromusic.fragments.base.AbsMainActivityFragment
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_search.*
import java.util.*
import kotlin.collections.ArrayList

class SearchFragment : AbsMainActivityFragment(R.layout.fragment_search), TextWatcher {
    companion object {
        const val QUERY = "query"
        const val REQ_CODE_SPEECH_INPUT = 9001
    }

    private lateinit var searchAdapter: SearchAdapter
    private var query: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity.setBottomBarVisibility(View.GONE)
        mainActivity.setSupportActionBar(toolbar)
        libraryViewModel.clearSearchResult()
        setupRecyclerView()
        searchView.apply {
            addTextChangedListener(this@SearchFragment)
            focusAndShowKeyboard()
        }
        voiceSearch.setOnClickListener { startMicSearch() }
        clearText.setOnClickListener { searchView.clearText() }
        keyboardPopup.apply {
            accentColor()
            setOnClickListener {
                searchView.focusAndShowKeyboard()
            }
        }
        if (savedInstanceState != null) {
            query = savedInstanceState.getString(QUERY)
        }
        libraryViewModel.getSearchResult().observe(viewLifecycleOwner, {
            showData(it)
        })
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
                empty.isVisible = searchAdapter.itemCount < 1
                val height = dipToPix(52f)
                recyclerView.setPadding(0, 0, 0, height.toInt())
            }
        })
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = searchAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy > 0) {
                        keyboardPopup.shrink()
                    } else if (dy < 0) {
                        keyboardPopup.extend()
                    }
                }
            })
        }
    }

    override fun afterTextChanged(newText: Editable?) {
        search(newText.toString())
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    private fun search(query: String) {
        this.query = query
        TransitionManager.beginDelayedTransition(appBarLayout)
        voiceSearch.isGone = query.isNotEmpty()
        clearText.isVisible = query.isNotEmpty()
        libraryViewModel.search(query)
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
}

fun TextInputEditText.clearText() {
    text = null
}
