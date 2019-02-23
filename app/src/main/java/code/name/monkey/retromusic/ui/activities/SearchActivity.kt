package code.name.monkey.retromusic.ui.activities

import android.app.Activity
import android.app.SearchManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView.BufferType
import android.widget.Toast
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.mvp.contract.SearchContract
import code.name.monkey.retromusic.mvp.presenter.SearchPresenter
import code.name.monkey.retromusic.ui.activities.base.AbsMusicServiceActivity
import code.name.monkey.retromusic.ui.adapter.SearchAdapter
import code.name.monkey.retromusic.util.RetroUtil
import kotlinx.android.synthetic.main.activity_search.*
import java.util.*

class SearchActivity : AbsMusicServiceActivity(), OnQueryTextListener, SearchContract.SearchView, TextWatcher {

    private lateinit var searchPresenter: SearchPresenter
    private var searchAdapter: SearchAdapter? = null
    private var query: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setDrawUnderStatusBar()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchPresenter = SearchPresenter(this)

        setStatusbarColorAuto()
        setNavigationbarColorAuto()
        setTaskDescriptionColorAuto()
        setLightNavigationBar(true)

        setupRecyclerView()
        setUpToolBar()
        setupSearchView()

        if (intent.getBooleanExtra("mic_search", false)) {
            startMicSearch()
        }
        back.setOnClickListener { onBackPressed() }
        voiceSearch.setOnClickListener { startMicSearch() }

        searchContainer.setCardBackgroundColor(ColorStateList.valueOf(ColorUtil.darkenColor(ThemeStore.primaryColor(this))))

    }

    private fun setupRecyclerView() {
        searchAdapter = SearchAdapter(this, emptyList())
        searchAdapter!!.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                empty.visibility = if (searchAdapter!!.itemCount < 1) View.VISIBLE else View.GONE
            }
        })
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = searchAdapter
        }
    }

    private fun setupSearchView() {
        getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.addTextChangedListener(this)
    }

    override fun onResume() {
        super.onResume()
        searchPresenter.subscribe()
        searchPresenter.search(query)
    }

    override fun onDestroy() {
        super.onDestroy()
        searchPresenter.unsubscribe()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(QUERY, query)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchPresenter.search(savedInstanceState.getString(QUERY, ""))
    }

    private fun setUpToolBar() {
        title = null
    }


    private fun search(query: String) {
        this.query = query.trim { it <= ' ' }
        voiceSearch.visibility = if (query.isNotEmpty()) View.GONE else View.VISIBLE
        searchPresenter.search(query)
    }

    override fun onMediaStoreChanged() {
        super.onMediaStoreChanged()
        searchPresenter.search(query!!)
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        hideSoftKeyboard()
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        search(newText)
        return false
    }

    private fun hideSoftKeyboard() {
        RetroUtil.hideSoftKeyboard(this@SearchActivity)
        if (searchView != null) {
            searchView.clearFocus()
        }
    }

    override fun loading() {

    }

    override fun showEmptyView() {
        searchAdapter!!.swapDataSet(ArrayList())
    }

    override fun completed() {

    }

    override fun showData(list: ArrayList<Any>) {
        searchAdapter!!.swapDataSet(list)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQ_CODE_SPEECH_INPUT -> {
                if (resultCode == Activity.RESULT_OK && null != data) {

                    val result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    query = result[0]
                    searchView.setText(query, BufferType.EDITABLE)
                    searchPresenter.search(query!!)
                }
            }
        }
    }

    private fun startMicSearch() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt))
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
            Toast.makeText(this, getString(R.string.speech_not_supported), Toast.LENGTH_SHORT).show()
        }

    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(newText: CharSequence, start: Int, before: Int, count: Int) {
        search(newText.toString())
    }

    override fun afterTextChanged(s: Editable) {

    }

    companion object {

        val TAG: String = SearchActivity::class.java.simpleName
        const val QUERY: String = "query"
        private const val REQ_CODE_SPEECH_INPUT = 9002
    }
}
