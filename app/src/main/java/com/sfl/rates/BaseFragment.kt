package com.sfl.rates

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sfl.rates.utils.NetworkUtils

abstract class BaseFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Network Listener
        NetworkUtils.registerNetworkCallback(context)
        // App launch time network checker
        NetworkUtils.networkCallback = {
            onNetworkChanged(it)
        }

        return inflater.inflate(getLayoutId(), container, false)
    }

    abstract override fun onViewCreated(view: View, savedInstanceState: Bundle?)

    override fun onResume() {
        super.onResume()
        // Experimental
        onNetworkChanged(NetworkUtils.isNetworkConnected(context))
    }

    abstract fun getLayoutId(): Int

    // Experimental
    abstract fun onNetworkChanged(state: Boolean)
}