package io.github.muntashirakon.music.fragments.search

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.google.android.material.textfield.TextInputEditText
import io.github.muntashirakon.music.R
import io.github.muntashirakon.music.adapter.SearchAdapter
import io.github.muntashirakon.music.extensions.accentColor
import io.github.muntashirakon.music.extensions.showToast
import io.github.muntashirakon.music.fragments.base.AbsMainActivityFragment
import kotlinx.android.synthetic.main.fragment_search.*
import org.koin.android.ext.android.inject
import java.util.*
import kotlin.collections.ArrayList

class SearchFragment : AbsMainActivityFragment(R.layout.fragment_search), TextWatcher {
    companion object {
        const val QUERY = "query"
        const val REQ_CODE_SPEECH_INPUT = 9001
    }

    private val viewModel: SearchViewModel by inject()
    private lateinit var searchAdapter: SearchAdapter
    private var query: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity.setSupportActionBar(toolbar)
        mainActivity.hideBottomBarVisibility(false)

        setupRecyclerView()
        keyboardPopup.accentColor()
        searchView.addTextChangedListener(this)
        voiceSearch.setOnClickListener { startMicSearch() }
        clearText.setOnClickListener { searchView.clearText() }
        keyboardPopup.setOnClickListener {
            val inputManager =
                getSystemService<InputMethodManager>(
                    requireContext(),
                    InputMethodManager::class.java
                )
            inputManager?.showSoftInput(searchView, InputMethodManager.SHOW_IMPLICIT)
        }

        if (savedInstanceState != null) {
            query = savedInstanceState.getString(QUERY)
        }

        viewModel.getSearchResult().observe(viewLifecycleOwner, Observer {
            showData(it)
        })
    }

    private fun showData(data: MutableList<Any>) {
        if (data.isNotEmpty()) {
            searchAdapter.swapDataSet(data)
        } else {
            searchAdapter.swapDataSet(ArrayList())
        }
    }


    private fun setupRecyclerView() {
        searchAdapter = SearchAdapter(requireActivity() as AppCompatActivity, emptyList())
        searchAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                empty.isVisible = searchAdapter.itemCount < 1
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
        viewModel.search(query)
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