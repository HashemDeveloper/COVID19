package com.project.covid19.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.project.covid19.R
import com.project.covid19.data.remote.DataHandler
import com.project.covid19.di.Injectable
import com.project.covid19.di.viewmodel.ViewModelFactory
import com.project.covid19.model.smartableai.COVIDSmartTableAIRes
import com.project.covid19.utils.Constants
import com.project.covid19.viewmodels.LiveDataMapViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class DisplayNews : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private var newsUrl: String? = ""
    private val liveDataMapViewModel: LiveDataMapViewModel by viewModels {
        this.viewModelFactory
    }

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
        val arg: Bundle? = arguments
        arg?.let { value ->
            val url: String = value.getString(Constants.BUNDLE_NEWS_URL, "")
            this.newsUrl = url
        }
    }

    override fun onResume() {
        super.onResume()
        this.newsUrl?.let { url ->
            this.liveDataMapViewModel.getCOVID19NewsLiveData(url)?.observe(viewLifecycleOwner, newsDataObserver())
        }
    }
    private fun newsDataObserver(): Observer<DataHandler<COVIDSmartTableAIRes>> {
        return Observer {
            when (it.status) {
                DataHandler.Status.LOADING -> {

                }
                DataHandler.Status.SUCCESS -> {
                    if (it.data is COVIDSmartTableAIRes) {
                        val newsData: COVIDSmartTableAIRes = it.data
                    }
                }
                DataHandler.Status.ERROR -> {

                }
            }
        }
    }
}
