package com.sfl.rates.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.sfl.rates.R
import com.sfl.rates.enums.CurrencyEnum
import com.sfl.rates.models.BankDetails
import com.sfl.rates.models.CurrencyItem
import com.sfl.rates.models.ExchangePointModel
import com.sfl.rates.services.RatePreference
import java.util.*
import kotlin.collections.ArrayList


object Utils {
    fun getPreDefinedExchangeCurrencyList(context: Context?): List<CurrencyItem> {
        if (context == null) return emptyList()
        val data: MutableList<CurrencyItem> = ArrayList()
        val currentCurrency = RatePreference.RateCurrency.getAsObject()
        data.add(
            CurrencyItem(
                CurrencyEnum.USD,
                context.getString(R.string.usd_desc),
                currentCurrency == CurrencyEnum.USD
            )
        )
        data.add(
            CurrencyItem(
                CurrencyEnum.EUR,
                context.getString(R.string.eur_desc),
                currentCurrency == CurrencyEnum.EUR
            )
        )
        data.add(
            CurrencyItem(
                CurrencyEnum.GBP,
                context.getString(R.string.gbr_desc),
                currentCurrency == CurrencyEnum.GBP
            )
        )
        data.add(
            CurrencyItem(
                CurrencyEnum.GEL,
                context.getString(R.string.gel_desc),
                currentCurrency == CurrencyEnum.GEL
            )
        )
        data.add(
            CurrencyItem(
                CurrencyEnum.JPY,
                context.getString(R.string.jpy_desc),
                currentCurrency == CurrencyEnum.JPY
            )
        )
        data.add(
            CurrencyItem(
                CurrencyEnum.CHF,
                context.getString(R.string.chf_desc),
                currentCurrency == CurrencyEnum.CHF
            )
        )
        data.add(
            CurrencyItem(
                CurrencyEnum.AUD,
                context.getString(R.string.aud_desc),
                currentCurrency == CurrencyEnum.AUD
            )
        )
        data.add(
            CurrencyItem(
                CurrencyEnum.RUR,
                context.getString(R.string.rub_desc),
                currentCurrency == CurrencyEnum.RUR
            )
        )
        data.add(
            CurrencyItem(
                CurrencyEnum.XAU,
                context.getString(R.string.xau_desc),
                currentCurrency == CurrencyEnum.XAU
            )
        )
        data.add(
            CurrencyItem(
                CurrencyEnum.CAD,
                context.getString(R.string.cad_desc),
                currentCurrency == CurrencyEnum.CAD
            )
        )
        return data
    }

    private fun isNeedDrawWorkDaysAndHours(rangeOfDays: String?): Boolean {
        return rangeOfDays != null && !rangeOfDays.contains("7")
    }

    private fun convertNumbersToWeekDays(rangeOfDays: String): String? {
        var result = rangeOfDays
        for (letter in rangeOfDays) {
            if (letter.isDigit()) {
                result = result.replace(
                    letter.toString(),
                    getNameOfDay(letter.toString().toInt()),
                    ignoreCase = true
                )
            }
        }
        return result
    }

    private fun getNameOfDay(dayOfWeak: Int): String {
        val calendar = Calendar.getInstance()
        calendar[Calendar.DAY_OF_WEEK] = dayOfWeak
        val days = arrayOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
        val dayIndex = calendar[Calendar.DAY_OF_WEEK]
        return days[dayIndex]
    }

    fun getWeekWorkDaysAndHours(workHours: List<BankDetails.BankBranchDetail.Workhour>): List<String?>? {
        val daysAndHoursList = arrayListOf<String?>()
        workHours.forEach {
            if (isNeedDrawWorkDaysAndHours(it.days)) {
                val workDays = convertNumbersToWeekDays(it.days.orEmpty())
                daysAndHoursList.add(workDays)
                daysAndHoursList.add(it.hours)
            } else {
                // in this case loop can be enter one time
                return null
            }
        }
        return daysAndHoursList
    }

    fun openMap(context: Context?, latitude: Double?, longitude: Double?) {
        val gmmIntentUri = Uri.parse("geo:${latitude},${longitude}")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        context?.startActivity(mapIntent)
    }

    fun convertCurrencyToCurrencyEnum(currency: Map<String, ExchangePointModel.Currency>): List<CurrencyEnum> {
        val list = arrayListOf<CurrencyEnum>()
        currency.keys.forEach {
            when (it) {
                "USD" -> list.add(CurrencyEnum.USD)
                "EUR" -> list.add(CurrencyEnum.EUR)
                "RUR" -> list.add(CurrencyEnum.RUR)
                "AUD" -> list.add(CurrencyEnum.AUD)
                "CAD" -> list.add(CurrencyEnum.CAD)
                "CHF" -> list.add(CurrencyEnum.CHF)
                "GBP" -> list.add(CurrencyEnum.GBP)
                "GEL" -> list.add(CurrencyEnum.GEL)
                "JPY" -> list.add(CurrencyEnum.JPY)
                "XAU" -> list.add(CurrencyEnum.XAU)
            }
        }
        return list
    }

    fun restartApp(context: Context?) {
        context?.apply {
            val packageManager = this.packageManager
            val intent = packageManager.getLaunchIntentForPackage(this.packageName)
            val componentName = intent?.component
            val mainIntent = Intent.makeRestartActivityTask(componentName)
            this.startActivity(mainIntent)
            Runtime.getRuntime().exit(0)
        }

    }
}