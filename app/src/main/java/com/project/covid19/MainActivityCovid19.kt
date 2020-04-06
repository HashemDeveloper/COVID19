package com.project.covid19

import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.covid19.data.local.ISharedPref
import com.project.covid19.data.remote.DataHandler
import com.project.covid19.di.viewmodel.ViewModelFactory
import com.project.covid19.events.DrawerLayoutEvent
import com.project.covid19.model.smartableai.COVIDSmartTableAIRes
import com.project.covid19.utils.Constants
import com.project.covid19.utils.rxevents.IRxEvents
import com.project.covid19.viewmodels.LiveDataMapViewModel
import com.project.covid19.views.recycler.DrawerItemAdapter
import com.project.covid19.views.recycler.items.DrawerHeaderItems
import com.project.covid19.views.recycler.items.DrawerNewsItemHeader
import com.project.covid19.views.recycler.items.DrawerNewsItems
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class MainActivityCovid19 : AppCompatActivity(), HasSupportFragmentInjector,
    SharedPreferences.OnSharedPreferenceChangeListener, DrawerItemAdapter.OnNewsItemClickListener {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    @Inject
    lateinit var iSharedPref: ISharedPref

    @Inject
    lateinit var iRxEvents: IRxEvents

    @Inject
    lateinit var viewModeLFactory: ViewModelFactory
    private val liveDataMapViewModel: LiveDataMapViewModel by viewModels {
        this.viewModeLFactory
    }

    @Inject
    lateinit var dispatchFragmentInjector: DispatchingAndroidInjector<Fragment>
    private lateinit var navController: NavController
    private var drawerItemAdapter: DrawerItemAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.navController = Navigation.findNavController(this, R.id.container)
        this.navController.setGraph(R.navigation.covid19_nav_layout)
        navigation_view_id?.setupWithNavController(this.navController)
    }

    override fun onStart() {
        super.onStart()
        this.liveDataMapViewModel.fetchAndSaveData()
        setupNavigationDrawer()
        monitorThemeState()
        testNewsData()
    }

    private fun testNewsData() {
        this.liveDataMapViewModel.getCOVID19NewsLiveData(Constants.COVID_NEWS_API_END_POINT + "US")
            ?.observe(this, Observer {
                when (it.status) {
                    DataHandler.Status.LOADING -> {

                    }
                    DataHandler.Status.SUCCESS -> {
                        if (it.data is COVIDSmartTableAIRes) {
                            val newsData: COVIDSmartTableAIRes = it.data as COVIDSmartTableAIRes
                            Timber.d("Data: ${newsData.updatedDateTime}")
                        }
                    }
                    DataHandler.Status.ERROR -> {

                    }
                }
            })
    }

    private fun setupNavigationDrawer() {
        this.compositeDisposable.add(this.iRxEvents.observable(DrawerLayoutEvent::class.java)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { event ->
                event?.floatingSearchView?.attachNavigationDrawerToMenuButton(
                    navigation_drawer_layout_id
                )
                setupDrawerItems()
            })
    }

    private fun setupDrawerItems() {
        navigation_view_menu_item_view_id?.layoutManager = LinearLayoutManager(this)
        this.drawerItemAdapter = DrawerItemAdapter(applicationContext, this)
        val isNightMode: Boolean = this.iSharedPref.getIsNightModeOn()
        this.drawerItemAdapter?.setIsNightMode(isNightMode)
        navigation_view_menu_item_view_id?.adapter = drawerItemAdapter
        val drawerHeaderItems = DrawerHeaderItems("")
        val newsHeader = DrawerNewsItemHeader("News")
        val localNews = DrawerNewsItems("Local")
        val globalNews = DrawerNewsItems("Global")
        val list: MutableList<Any> = arrayListOf()
        list.add(drawerHeaderItems)
        list.add(newsHeader)
        list.add(localNews)
        list.add(globalNews)

        this.drawerItemAdapter?.setData(list)
    }

    private fun monitorThemeState() {
        this.iSharedPref.registerOnSharedPrefListener(this)
        val configuration: Configuration? = resources.configuration
        configuration?.let { config ->
            when (config.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_NO -> {
                    this.iSharedPref.setIsNightModeOn(false)
                }
                Configuration.UI_MODE_NIGHT_YES -> {
                    this.iSharedPref.setIsNightModeOn(true)
                }
            }
        }
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return this.dispatchFragmentInjector
    }

    override fun onBackPressed() {
        if (navigation_drawer_layout_id.isDrawerOpen(GravityCompat.START)) {
            navigation_drawer_layout_id.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onSharedPreferenceChanged(pref: SharedPreferences?, key: String?) {
        when (key) {
            Constants.IS_NIGHT_MODE -> {
                val isNightMode: Boolean = pref?.getBoolean(Constants.IS_NIGHT_MODE, false)!!
                if (this.drawerItemAdapter != null) {
                    this.drawerItemAdapter?.setIsNightMode(isNightMode)
                }
            }
        }
    }

    override fun <T> onItemClicked(item: T) {
        when (item) {
            is DrawerNewsItems -> {
                val newsItem: DrawerNewsItems = item
                when (newsItem.newsItemName) {
                    "Local" -> {
                        val local: String = Locale.getDefault().country
                        val newsUrl: String = Constants.COVID_NEWS_API_END_POINT + "/$local"
                        navigateTowNewsPage(newsUrl)
                    }
                    "Global" -> {
                        val newsUrl: String = Constants.COVID_NEWS_API_END_POINT + "/GB"
                        navigateTowNewsPage(newsUrl)
                    }
                }
            }
        }
    }
    private fun navigateTowNewsPage(url: String) {
        if (navigation_drawer_layout_id.isDrawerOpen(GravityCompat.START)) {
            navigation_drawer_layout_id.closeDrawer(GravityCompat.START)
        }
        val bundle = Bundle()
        bundle.putString(Constants.BUNDLE_NEWS_URL, url)
        this.navController.navigate(R.id.newsView_id, bundle)
    }

    override fun onDestroy() {
        super.onDestroy()
        this.iSharedPref.unregisterOnSharedPrefListener(this)
    }
}
