package com.sfl.rates.viewModels

import androidx.lifecycle.ViewModel
import com.sfl.rates.enums.CashType
import com.sfl.rates.models.ExchangePointModel
import com.sfl.rates.repositories.ExchangeRateRepository
import com.sfl.rates.services.RatePreference

class ExchangeRateViewModel(private val rateRepository: ExchangeRateRepository) : ViewModel() {
    fun getRates() = rateRepository.getRates()
    fun getBankDetails(id: String) = rateRepository.getBankDetails(id)

    // Separate method for future testing
    fun sortListByBuy(
        list: List<ExchangePointModel>,
        reverseOrNot: Boolean
    ): List<ExchangePointModel> {
        val withoutNullValuesList = filterListBeforeDraw(list)
        val sortedList = if (RatePreference.CashType.getAsObject() == CashType.CASH)
            withoutNullValuesList.sortedBy { it.currencyList[RatePreference.RateCurrency.getAsObject().name]?.cache?.buy }
        else {
            withoutNullValuesList.sortedBy { it.currencyList[RatePreference.RateCurrency.getAsObject().name]?.nonCache?.buy }
        }
        return if (reverseOrNot) sortedList.asReversed() else sortedList
    }

    fun sortListBySell(
        list: List<ExchangePointModel>,
        reverseOrNot: Boolean
    ): List<ExchangePointModel> {
        val withoutNullValuesList = filterListBeforeDraw(list)
        val sortedList = if (RatePreference.CashType.getAsObject() == CashType.CASH)
            withoutNullValuesList.sortedBy { it.currencyList[RatePreference.RateCurrency.getAsObject().name]?.cache?.sell }
        else {
            withoutNullValuesList.sortedBy { it.currencyList[RatePreference.RateCurrency.getAsObject().name]?.nonCache?.sell }
        }
        return if (reverseOrNot) sortedList.asReversed() else sortedList
    }

    fun filterListBeforeDraw(list: List<ExchangePointModel>): List<ExchangePointModel> {
        return if (RatePreference.CashType.getAsObject() == CashType.CASH)
            list.filter { it.currencyList[RatePreference.RateCurrency.getAsObject().name]?.cache?.buy != null }
        else {
            list.sortedBy { it.currencyList[RatePreference.RateCurrency.getAsObject().name]?.nonCache?.buy != null }
        }
    }
}