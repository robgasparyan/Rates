package com.sfl.rates.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.Navigation
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.sfl.rates.BaseFragment
import com.sfl.rates.R
import kotlinx.android.synthetic.main.bank_location_layout.*


class BankLocationFragment : BaseFragment() {

    override fun getLayoutId() = R.layout.bank_location_layout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mapView?.onCreate(savedInstanceState)
        val latLng = arguments?.getString("latlng", "")?.split(",")
        if (latLng == null || latLng.isEmpty()) {

            Navigation.findNavController(view).popBackStack()
        }
        mapView?.getMapAsync {
            it?.mapType = GoogleMap.MAP_TYPE_NORMAL
            it?.uiSettings?.setAllGesturesEnabled(true)
            latLng?.apply {
                it?.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            latLng[0].toDouble(),
                            latLng[1].toDouble()
                        ), 17f
                    )
                )
                it.addMarker(
                    MarkerOptions().position(
                        LatLng(
                            latLng[0].toDouble(),
                            latLng[1].toDouble()
                        )
                    )
                )
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    override fun onNetworkChanged(state: Boolean) {

    }
}