package com.project.covid19.views

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.arlib.floatingsearchview.FloatingSearchView
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.project.covid19.BuildConfig
import com.project.covid19.R
import com.project.covid19.di.Injectable
import com.project.covid19.di.viewmodel.ViewModelFactory
import com.project.covid19.model.hopkinsdata.Coordinates
import com.project.covid19.model.hopkinsdata.HopkinsCSSEDataRes
import com.project.covid19.model.hopkinsdata.SearchHopkinData
import com.project.covid19.model.hopkinsdata.Stats
import com.project.covid19.utils.Constants
import com.project.covid19.viewmodels.LiveDataMapViewModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_live_data_map_view.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject


class LiveDataMapView : Fragment(), Injectable, OnMapReadyCallback {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private var locationManager: LocationManager?= null
    private var isGpsEnabled = false
    private var isNetworkEnabled = false
    private var lastKnownLocation: Location?= null
    private val liveDataMapViewModel: LiveDataMapViewModel by viewModels {
        this.viewModelFactory
    }
    private var googleMap: GoogleMap?= null
    private var mLastQuery: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!checkPermission()) {
            requestPermission()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        AndroidSupportInjection.inject(this)
        return inflater.inflate(R.layout.fragment_live_data_map_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fragment_live_data_map_view_id.onCreate(savedInstanceState)
        fragment_live_data_map_view_id.onResume()
        positionMyLocationButton()
        try {
            MapsInitializer.initialize(this.context!!)
        } catch (ex: Exception) {
            if (BuildConfig.DEBUG) {
                Timber.d(ex.localizedMessage)
            }
        }

        fragment_live_data_map_view_id.getMapAsync(this)
        setupSearch()
        super.onViewCreated(view, savedInstanceState)
    }


    //***GOOGLE MAP STARTS***
    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap
        if (!checkPermission()) {
            requestPermission()
        } else {
            googleMap!!.isMyLocationEnabled = true
            googleMap.uiSettings.isMyLocationButtonEnabled = true
            setupCurrentLocationAndState(true)
            listenForCameraChange()
        }
    }

    private fun getCurrentState(lat: Double, lon: Double): String? {
        var addList: List<Address>?= null
        try {
            val geocoder: Geocoder = Geocoder(this.context!!, Locale.getDefault())
            addList= geocoder.getFromLocation(lat, lon, 1)
        } catch (ex: Exception) {
            if (BuildConfig.DEBUG) {
                if (ex.localizedMessage != null) {
                    Timber.e(ex.localizedMessage)
                }
            }
        }
        return if (addList != null && addList.isNotEmpty()) {
            addList[0].adminArea
        } else {
            null
        }
    }

    private fun positionMyLocationButton() {
        val locationButton = (fragment_live_data_map_view_id.findViewById<View>("1".toInt())
            .parent as View).findViewById<View>("2".toInt())
        getMapLocationBt()?.setImageResource(R.drawable.ic_gps_fixed_gray_24dp)
        val rlp =
            locationButton.layoutParams as RelativeLayout.LayoutParams
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
        rlp.setMargins(0, 0, 30, 30) // positions to right bottom
    }
    private fun getMapLocationBt(): ImageView? {
        return (fragment_live_data_map_view_id.findViewById<View>("1".toInt())
            .parent as View).findViewById<View>("2".toInt()) as ImageView
    }
    private fun listenForCameraChange() {
        this.googleMap?.setOnCameraMoveStartedListener {resultCode ->
            if (resultCode == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
                if (googleMap?.isMyLocationEnabled!!) {
                    googleMap?.uiSettings?.isMyLocationButtonEnabled = false
                    val fadeOutAnim = AnimationUtils.loadAnimation(context, R.anim.anim_fade_out)
                    getMapLocationBt()?.animation = fadeOutAnim
                }
                if (live_data_search_view_id.visibility == View.VISIBLE) {
                    live_data_search_view_id.visibility = View.GONE
                    val fadeOutAnim = AnimationUtils.loadAnimation(context, R.anim.anim_fade_out)
                    live_data_search_view_id?.animation = fadeOutAnim
                }
                if (fragment_data_display_holder_id.visibility == View.VISIBLE) {
                    val fadeOutAnim = AnimationUtils.loadAnimation(context, R.anim.anim_fade_out)
                    fragment_data_display_holder_id?.animation = fadeOutAnim
                }
            }
        }

        this.googleMap?.setOnCameraIdleListener {
            if (googleMap?.isMyLocationEnabled!!) {
                this.googleMap?.uiSettings?.isMyLocationButtonEnabled = true
                val fadeInAnim: Animation = AnimationUtils.loadAnimation(context, R.anim.anim_fade_in)
                getMapLocationBt()?.animation = fadeInAnim
            }
            if (live_data_search_view_id.visibility == View.GONE) {
                live_data_search_view_id.visibility = View.VISIBLE
                val fadeInAnim: Animation = AnimationUtils.loadAnimation(context, R.anim.anim_fade_in)
                live_data_search_view_id?.animation = fadeInAnim
            }
        }
        this.googleMap?.setOnMyLocationButtonClickListener {
            setupCurrentLocationAndState(false)
            true
        }
    }

    private fun checkPermission() : Boolean {
        val foreGroundFineLocationState: Int = ContextCompat.checkSelfPermission(this.context!!, Manifest.permission.ACCESS_FINE_LOCATION)
        val foreGroundCoarseLocationState: Int = ContextCompat.checkSelfPermission(this.context!!, Manifest.permission.ACCESS_COARSE_LOCATION)
        var backgroundLocationState = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            backgroundLocationState = ContextCompat.checkSelfPermission(this.context!!, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }
        return (foreGroundCoarseLocationState == PackageManager.PERMISSION_GRANTED) && (foreGroundFineLocationState == PackageManager.PERMISSION_GRANTED)
                && (backgroundLocationState == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                    LOCATION_REQUEST_CODE
                )
            } else {
                googleMap!!.isMyLocationEnabled = true
                googleMap!!.uiSettings.isMyLocationButtonEnabled = true
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions( arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                    LOCATION_REQUEST_CODE
                )
            } else {
                googleMap!!.isMyLocationEnabled = true
                googleMap!!.uiSettings.isMyLocationButtonEnabled = true
            }
        } else {
            if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestLowApiPermission()
            } else {
                googleMap!!.isMyLocationEnabled = true
                googleMap!!.uiSettings.isMyLocationButtonEnabled = true
            }
        }
    }

    private fun requestLowApiPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!, Manifest.permission.ACCESS_FINE_LOCATION) ||
            ActivityCompat.shouldShowRequestPermissionRationale(activity!!, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            googleMap!!.isMyLocationEnabled = true
            googleMap!!.uiSettings.isMyLocationButtonEnabled = true
        } else {
            ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                ACCESS_COARSE_AND_FINE_LOCATION_CODE
            )
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (grantResults.isNotEmpty()) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        googleMap!!.isMyLocationEnabled = true
                        googleMap!!.uiSettings.isMyLocationButtonEnabled = true
                        setupCurrentLocationAndState(true)
                    }
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults.isNotEmpty()) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        googleMap!!.isMyLocationEnabled = true
                        googleMap!!.uiSettings.isMyLocationButtonEnabled = true
                        setupCurrentLocationAndState(true)
                    }
                }
            } else {
                if (grantResults.isNotEmpty()) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        googleMap!!.isMyLocationEnabled = true
                        googleMap!!.uiSettings.isMyLocationButtonEnabled = true
                        setupCurrentLocationAndState(true)
                    }
                }
            }
        }
    }
    @SuppressLint("MissingPermission")
    private fun setupCurrentLocationAndState(initiateLocationManager: Boolean) {
        this.locationManager = this.context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (initiateLocationManager) {
            if (locationManager != null) {
                isGpsEnabled = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
                isNetworkEnabled = locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            }
        }
        var gpsLocation: Location? = null
        var netLocation: Location?= null
        if (isGpsEnabled) {
            locationManager?.let {
                gpsLocation = it.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            }
        }
        if (isNetworkEnabled) {
            locationManager?.let {
                netLocation = it.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            }
        }
        lastKnownLocation = if (gpsLocation != null && netLocation != null) {
            if (gpsLocation?.time!! > netLocation?.time!!) {
                gpsLocation
            } else {
                netLocation
            }
        } else if (gpsLocation != null) {
            gpsLocation
        } else {
            netLocation
        }
        lastKnownLocation?.let {
            moveMapCamera(it.latitude, it.longitude, 18.0f)
            val state: String? = getCurrentState(it.latitude, it.longitude)
            state?.let { s ->
                val hopkinsCSSEDataRes: HopkinsCSSEDataRes? = this.liveDataMapViewModel.postDataByState(s)
                hopkinsCSSEDataRes?.let { res ->
                    val stats: Stats? = if (res.stats != null) res.stats else null
                    stats?.let {data ->
                        setupInitialStat(res, data)
                    }
                }
            }
        }
    }
    //***GOOGLE MAP ENDS***
    private fun setupSearch() {
        live_data_search_view_id.setOnQueryChangeListener { oldQuery, newQuery ->
            if (oldQuery.isNotEmpty() && newQuery.isEmpty()) {
                live_data_search_view_id.clearSuggestions()
            } else {
                this.liveDataMapViewModel.findSuggestions(newQuery, live_data_search_view_id)
            }
        }

        live_data_search_view_id.setOnSearchListener(object : FloatingSearchView.OnSearchListener{
            override fun onSearchAction(currentQuery: String?) {
                currentQuery?.let {
                    mLastQuery = it
                }
                Timber.e(mLastQuery)
            }

            override fun onSuggestionClicked(searchSuggestion: SearchSuggestion?) {
               val hopkinsCSSData: SearchHopkinData = searchSuggestion as SearchHopkinData
                mLastQuery = hopkinsCSSData.body
                Constants.hideKeyboard(activity!!)
                val coordinates: Coordinates? = if (hopkinsCSSData.coordinates != null) hopkinsCSSData.coordinates else null
                val stats: Stats? = if (hopkinsCSSData.stats != null) hopkinsCSSData.stats else null
                if (coordinates != null) {
                    moveMapCamera(coordinates.lattitude.toDouble(), coordinates.longitude.toDouble(), 8f)
                    stats?.let { data ->
                       setupCOVID19Stat(hopkinsCSSData, data)
                    }
                } else {
                    Toast.makeText(context!!, "Unknown", Toast.LENGTH_SHORT).show()
                }
                live_data_search_view_id.clearSearchFocus()
                this@LiveDataMapView.liveDataMapViewModel.saveSearchHistory(hopkinsCSSData)
            }
        })
        live_data_search_view_id.setOnFocusChangeListener(object : FloatingSearchView.OnFocusChangeListener {
            override fun onFocusCleared() {
                live_data_search_view_id.setSearchBarTitle(mLastQuery)
            }

            override fun onFocus() {
                this@LiveDataMapView.liveDataMapViewModel.setupSearchHistory(live_data_search_view_id)
            }
        })

    }

    private fun setupCOVID19Stat(hopkinsCSSData: SearchHopkinData, data: Stats) {
        fragment_data_display_holder_id.visibility = View.VISIBLE
        val confirmedStat: String = "Confirmed Cases ${data.confirmed}"
        val deathStat: String = "Deaths ${data.deaths}"
        val recovered: String = "Recovered ${data.recovered}"
        val province: String? = if (hopkinsCSSData.province != null) hopkinsCSSData.province else ""
        val placeName: String = "Location: ${hopkinsCSSData.country}, $province"
        val lastUpdate: String = "Last Update: ${hopkinsCSSData.updatedAt}"
        fragment_live_data_confirmed_cases_view_id.text = confirmedStat
        fragment_live_data_deaths_view_id.text = deathStat
        fragment_live_data_recovered_view_id.text = recovered
        fragment_live_data_place_view_id.text = placeName
        fragment_live_data_updated_view_id.text =lastUpdate
    }

    private fun setupInitialStat(hopkinsCSSData: HopkinsCSSEDataRes, data: Stats) {
        fragment_data_display_holder_id.visibility = View.VISIBLE
        val confirmedStat: String = "Confirmed Cases ${data.confirmed}"
        val deathStat: String = "Deaths ${data.deaths}"
        val recovered: String = "Recovered ${data.recovered}"
        val province: String? = if (hopkinsCSSData.province != null) hopkinsCSSData.province else ""
        val placeName: String = "Location: ${hopkinsCSSData.country}, $province"
        val lastUpdate: String = "Last Update: ${hopkinsCSSData.updatedAt}"
        fragment_live_data_confirmed_cases_view_id.text = confirmedStat
        fragment_live_data_deaths_view_id.text = deathStat
        fragment_live_data_recovered_view_id.text = recovered
        fragment_live_data_place_view_id.text = placeName
        fragment_live_data_updated_view_id.text =lastUpdate
    }

    private fun moveMapCamera(lat: Double, lon: Double, zoomLevel: Float) {
//        googleMap?.moveCamera(CameraUpdateFactory.newLatLng(LatLng(lat, lon)))
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat, lon), zoomLevel))
    }

    override fun onResume() {
        fragment_live_data_map_view_id.onResume()
        super.onResume()
    }

    override fun onPause() {
        fragment_live_data_map_view_id.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        fragment_live_data_map_view_id.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        fragment_live_data_map_view_id.onLowMemory()
        super.onLowMemory()
    }

    companion object {
        private const val LOCATION_REQUEST_CODE = 34
        private const val ACCESS_COARSE_AND_FINE_LOCATION_CODE = 1
        private const val MAP_ZOOM_LEVEL: Float = 8f
    }
}
