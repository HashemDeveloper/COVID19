package com.project.covid19.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.project.covid19.R
import com.project.covid19.di.Injectable
import com.project.covid19.utils.Constants
import dagger.android.support.AndroidSupportInjection
import timber.log.Timber

class DisplayNews : Fragment(), Injectable {

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
            Timber.d("Url: $url")
        }
    }
}
