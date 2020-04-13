package com.sfl.rates.utils

import com.google.gson.Gson
import com.sfl.rates.enums.CashType
import com.sfl.rates.enums.CurrencyEnum
import com.sfl.rates.enums.InterfaceType
import org.koin.core.KoinComponent
import org.koin.core.inject


object GsonUtils : KoinComponent {

    private val gson: Gson by inject()

    fun currencyEnumToJson(currencyEnum: CurrencyEnum): String {
        return gson.toJson(currencyEnum)
    }

    fun getCurrencyEnumFromJson(json: String?): CurrencyEnum {
        return gson.fromJson(json, CurrencyEnum::class.java)
    }

    fun cashEnumToJson(cashType: CashType): String {
        return gson.toJson(cashType)
    }

    fun getCashEnumFromJson(json: String?): CashType {
        return gson.fromJson(json, CashType::class.java)
    }

    fun interfaceEnumToJson(interfaceType: InterfaceType): String {
        return gson.toJson(interfaceType)
    }

    fun getInterfaceEnumFromJson(json: String?): InterfaceType {
        return gson.fromJson(json, InterfaceType::class.java)
    }
}
