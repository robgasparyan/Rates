package com.sfl.rates.repositories

import androidx.lifecycle.liveData
import com.sfl.rates.services.RateApiService
import com.sfl.rates.services.RatePreference
import kotlinx.coroutines.Dispatchers

class ExchangeRateRepository(private val rateApiService: RateApiService) {

    fun getRates() = liveData(Dispatchers.IO) {
        try {
            val result = rateApiService.getExchangePoints()

            val data = result.map {
                it.value.uuid = it.key
                it.value
            }.filter {
                it.currencyList.containsKey(RatePreference.RateCurrency.getAsObject().name)
            }
            // Main filter which filtering currency
            emit(Result.success(data))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun getBankDetails(id: String) = liveData(Dispatchers.IO) {
        try {
            val result = rateApiService.getBankBranches(id)
            val data = result.list.map {
                it.value
            }.sortedBy {
                it.head
            }.asReversed()

            emit(Result.success(data))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

}