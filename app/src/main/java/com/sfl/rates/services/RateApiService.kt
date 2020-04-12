package com.sfl.rates.services

import com.sfl.rates.models.BankDetails
import com.sfl.rates.models.ExchangePointModel
import retrofit2.http.GET
import retrofit2.http.Query

interface RateApiService {

    @GET("rates.ashx")
    suspend fun getExchangePoints(@Query("lang") language: String = "en"): Map<String, ExchangePointModel>

    @GET("branches.ashx")
    suspend fun getBankBranches(@Query("id") id: String): BankDetails

}