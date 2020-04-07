package com.project.covid19.views

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.covid19.BuildConfig
import com.project.covid19.R
import com.project.covid19.data.remote.DataHandler
import com.project.covid19.di.Injectable
import com.project.covid19.di.viewmodel.ViewModelFactory
import com.project.covid19.model.smartableai.COVIDNews
import com.project.covid19.model.smartableai.COVIDSmartTableAIRes
import com.project.covid19.utils.Constants
import com.project.covid19.viewmodels.LiveDataMapViewModel
import com.project.covid19.views.recycler.NewsItemAdapter
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_news_layout.*
import timber.log.Timber
import javax.inject.Inject

class DisplayNews : Fragment(), Injectable, SharedPreferences.OnSharedPreferenceChangeListener, NewsItemAdapter.NewsItemClickListener {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private var newsUrl: String? = ""
    private val liveDataMapViewModel: LiveDataMapViewModel by viewModels {
        this.viewModelFactory
    }
    private var newsItemAdapter: NewsItemAdapter?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        AndroidSupportInjection.inject(this)
        return inflater.inflate(R.layout.fragment_news_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.liveDataMapViewModel.setupSharedPrefChangeListener(this)
        val isNightModeOn: Boolean = this.liveDataMapViewModel.getIsNightModeOn()
        this.newsItemAdapter = NewsItemAdapter(this)
        newsItemAdapter?.setIsNightMode(isNightModeOn)
        fragment_news_recycler_view_id?.layoutManager = LinearLayoutManager(this.context)
        fragment_news_recycler_view_id?.adapter = newsItemAdapter
        val arg: Bundle? = arguments
        arg?.let { value ->
            val url: String = value.getString(Constants.BUNDLE_NEWS_URL, "")
            this.newsUrl = url
        }
        swipeToRefresh()
    }

    private fun swipeToRefresh() {
        fragment_news_swipe_to_refresh_id?.setOnRefreshListener {
            fetchData()
        }
    }

    override fun onResume() {
        super.onResume()
       fetchData()
    }
    private fun fetchData() {
        this.newsUrl?.let { url ->
            this.liveDataMapViewModel.getCOVID19NewsLiveData(url)?.observe(viewLifecycleOwner, newsDataObserver())
        }
    }
    private fun newsDataObserver(): Observer<DataHandler<COVIDSmartTableAIRes>> {
        return Observer {
            when (it.status) {
                DataHandler.Status.LOADING -> {
                    fragment_news_progress_bar_id?.visibility = View.VISIBLE
                    fragment_news_swipe_to_refresh_id?.isRefreshing = true
                }
                DataHandler.Status.SUCCESS -> {
                    fragment_news_progress_bar_id?.visibility = View.GONE
                    fragment_news_swipe_to_refresh_id?.isRefreshing = false
                    if (it.data is COVIDSmartTableAIRes) {
                        it.data.news?.let { list ->
                            val newsList: MutableList<COVIDNews>? = arrayListOf()
                            newsList?.addAll(list)
                            newsList?.let {newsItems->
                                this.newsItemAdapter?.setData(newsItems)
                            }
                        }
                    }
                }
                DataHandler.Status.ERROR -> {
                    fragment_news_progress_bar_id?.visibility = View.GONE
                    fragment_news_swipe_to_refresh_id?.isRefreshing = false
                    Toast.makeText(this.context!!, "Server error.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onSharedPreferenceChanged(pref: SharedPreferences?, key: String?) {
        when (key) {
            Constants.IS_NIGHT_MODE -> {
                val isNightMode: Boolean = pref?.getBoolean(Constants.IS_NIGHT_MODE, false)!!
                if (this.newsItemAdapter != null) {
                    this.newsItemAdapter?.setIsNightMode(isNightMode)
                }
            }
        }
    }

    override fun onNewsItemClicked(newsItem: COVIDNews) {
        try {
            var newsUrl: String = ""
            val webUrl: String = newsItem.webUrl
            newsUrl = newsItem.cdnAmpWebUrl ?: webUrl
            val browseIntent = Intent(Intent.ACTION_VIEW, Uri.parse(newsUrl))
            startActivity(browseIntent)
        } catch (ex: Exception) {
            if (BuildConfig.DEBUG) {
                if (ex.localizedMessage != null)
                Timber.d(ex.localizedMessage)
            }
        }
    }
}
