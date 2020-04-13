package com.sfl.rates.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.Navigation
import com.sfl.rates.BaseFragment
import com.sfl.rates.R
import com.sfl.rates.enums.InterfaceType
import com.sfl.rates.services.RatePreference
import com.sfl.rates.utils.Utils
import com.sfl.rates.utils.runDelayed
import kotlinx.android.synthetic.main.bank_setting_layout.*

class BankSettingFragment : BaseFragment() {

    override fun getLayoutId() = R.layout.bank_setting_layout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        userInterfaceSwitch.isChecked =
            RatePreference.InterfaceType.getAsObject() == InterfaceType.DARK

        interfaceChangeButton?.setOnClickListener {
            if (RatePreference.InterfaceType.getAsObject() == InterfaceType.DARK && userInterfaceSwitch.isChecked) {
                Navigation.findNavController(view).popBackStack()
                return@setOnClickListener
            }
            if (RatePreference.InterfaceType.getAsObject() == InterfaceType.LIGHT && !userInterfaceSwitch.isChecked) {
                Navigation.findNavController(view).popBackStack()
                return@setOnClickListener
            }
            RatePreference.InterfaceType.set(if (userInterfaceSwitch.isChecked) InterfaceType.DARK else InterfaceType.LIGHT)
            runDelayed(300) {
                // Delay for preference apply(this action is async)
                Utils.restartApp(context)
            }
        }
    }

    override fun onNetworkChanged(state: Boolean) {
        // ignore
    }
}