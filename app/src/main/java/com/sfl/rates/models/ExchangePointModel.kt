package com.sfl.rates.models


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ExchangePointModel(
    @SerializedName("date")
    val date: Int? = 0, // 1586548860
    @SerializedName("list")
    val currencyList: Map<String, Currency> = mapOf(),
    @SerializedName("logo")
    val logo: String? = "", // 13.gif
    @SerializedName("title")
    val title: String? = "", // Inecobank
    var uuid: String? = null
) : Serializable {

    data class Currency(
        @SerializedName("0")
        var cache: Rate? = null,

        @SerializedName("1")
        var nonCache: Rate? = null
    ) : Serializable {
        data class Rate(
            @SerializedName("buy")
            val buy: Double? = 0.0, // 526.00
            @SerializedName("sell")
            val sell: Double? = 0.0 // 542.00
        ) : Serializable
    }


}
