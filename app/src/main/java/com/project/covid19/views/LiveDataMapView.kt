package com.project.covid19.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.project.covid19.BuildConfig
import com.project.covid19.R
import com.project.covid19.di.Injectable
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_live_data_map_view.*
import timber.log.Timber

class LiveDataMapView : Fragment(), Injectable, OnMapReadyCallback {
    private var googleMap: GoogleMap?= null


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
        try {
            MapsInitializer.initialize(this.context!!)
        } catch (ex: Exception) {
            if (BuildConfig.DEBUG) {
                Timber.d(ex.localizedMessage)
            }
        }

        fragment_live_data_map_view_id.getMapAsync(this)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap
        this.googleMap?.uiSettings!!.isMyLocationButtonEnabled = false
        this.googleMap?.isMyLocationEnabled = true
        this.googleMap?.moveCamera(CameraUpdateFactory.newLatLng(LatLng(40.678177, -73.944160)))
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
}
