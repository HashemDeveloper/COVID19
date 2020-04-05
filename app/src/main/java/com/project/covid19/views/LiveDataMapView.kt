package com.project.covid19.views

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RelativeLayout
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
import com.project.covid19.utils.Constants
import com.project.covid19.viewmodels.LiveDataMapViewModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_live_data_map_view.*
import timber.log.Timber
import javax.inject.Inject


class LiveDataMapView : Fragment(), Injectable, OnMapReadyCallback {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val liveDataMapViewModel: LiveDataMapViewModel by viewModels {
        this.viewModelFactory
    }
    private var googleMap: GoogleMap?= null
    private var mLastQuery: String = ""

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
        }
        //TODO: move camera initially
        listenForCameraChange()
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
                    }
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults.isNotEmpty()) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        googleMap!!.isMyLocationEnabled = true
                        googleMap!!.uiSettings.isMyLocationButtonEnabled = true
                    }
                }
            } else {
                if (grantResults.isNotEmpty()) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        googleMap!!.isMyLocationEnabled = true
                        googleMap!!.uiSettings.isMyLocationButtonEnabled = true
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
               val hopkinsCSSData: HopkinsCSSEDataRes = searchSuggestion as HopkinsCSSEDataRes
                mLastQuery = hopkinsCSSData.body
                Constants.hideKeyboard(activity!!)
                val coordinates: Coordinates? = if (hopkinsCSSData.coordinates != null) hopkinsCSSData.coordinates else null
                if (coordinates != null) {
                    moveMapCamera(coordinates.lattitude.toDouble(), coordinates.longitude.toDouble(), 8f)
                }
                live_data_search_view_id.clearSearchFocus()
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

    private fun moveMapCamera(lat: Double, lon: Double, zoomLevel: Float) {
        googleMap?.moveCamera(CameraUpdateFactory.newLatLng(LatLng(lat, lon)))
        googleMap?.animateCamera(CameraUpdateFactory.zoomTo(zoomLevel))
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
    }
}
