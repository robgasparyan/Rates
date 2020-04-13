package com.sfl.rates.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sfl.rates.R
import com.sfl.rates.enums.CashType
import com.sfl.rates.enums.CurrencyEnum
import com.sfl.rates.enums.InterfaceType
import com.sfl.rates.services.RatePreference

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (RatePreference.RateCurrency.get().isEmpty()) {
            // Default Currency
            RatePreference.RateCurrency.set(CurrencyEnum.USD)
        }
        if (RatePreference.CashType.get().isEmpty()) {
            // Default Cash Type
            RatePreference.CashType.set(CashType.CASH)
        }
        if (RatePreference.InterfaceType.get().isEmpty()) {
            RatePreference.InterfaceType.set(InterfaceType.LIGHT)
        }
        setTheme(if (RatePreference.InterfaceType.getAsObject() == InterfaceType.LIGHT) R.style.LightTheme else R.style.DarkTheme)
    }

}