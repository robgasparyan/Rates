package com.sfl.rates.enums

import com.google.gson.annotations.SerializedName
import com.sfl.rates.R

enum class CurrencyEnum(val value: String, val drawableID: Int) {

    @SerializedName("USD")
    USD("USD", R.mipmap.ic_usd),

    @SerializedName("EUR")
    EUR("EUR", R.mipmap.ic_eur),

    @SerializedName("RUR")
    RUR("RUR", R.mipmap.ic_rur),

    @SerializedName("AUD")
    AUD("AUD", R.mipmap.ic_aud),

    @SerializedName("CAD")
    CAD("CAD", R.mipmap.ic_cad),

    @SerializedName("CHF")
    CHF("CHF", R.mipmap.ic_chf),

    @SerializedName("GBP")
    GBP("GBP", R.mipmap.ic_gbp),

    @SerializedName("GEL")
    GEL("GEL", R.mipmap.ic_gel),

    @SerializedName("JPY")
    JPY("JPY", R.mipmap.ic_jpy),

    @SerializedName("XAU")
    XAU("XAU", R.mipmap.ic_xau)

}