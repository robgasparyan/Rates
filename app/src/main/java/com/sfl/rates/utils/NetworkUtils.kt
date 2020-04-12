package com.sfl.rates.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkRequest


object NetworkUtils {

    private val networkCountDownTimer = SimpleTimer<Boolean>(500) {
        it?.let {
            networkCallback?.invoke(it)
        }
    }
    var networkCallback: ((Boolean) -> Unit)? = null
    fun registerNetworkCallback(context: Context?) {
        context?.apply {
            val builder = NetworkRequest.Builder()
            (this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).registerNetworkCallback(
                builder.build(), object : NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        super.onAvailable(network)
                        networkCountDownTimer.restart(true)
                    }

                    override fun onLost(network: Network) {
                        super.onLost(network)
                        networkCountDownTimer.restart(false)
                    }
                })
        }

    }

    fun isNetworkConnected(context: Context?): Boolean {
        if (context == null) {
            return false
        }
        val cm =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null
    }
}