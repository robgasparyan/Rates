package com.sfl.rates.utils

import com.google.gson.Gson
import com.sfl.rates.enums.CashType
import com.sfl.rates.enums.CurrencyEnum

object GsonUtils {

    fun currencyEnumToJson(currencyEnum: CurrencyEnum): String {
        return Gson().toJson(currencyEnum)
    }

    fun getCurrencyEnumFromJson(json: String?): CurrencyEnum {
        return Gson().fromJson(json, CurrencyEnum::class.java)
    }

    fun cashEnumToJson(cashType: CashType): String {
        return Gson().toJson(cashType)
    }

    fun getCashEnumFromJson(json: String?): CashType {
        return Gson().fromJson(json, CashType::class.java)
    }

}