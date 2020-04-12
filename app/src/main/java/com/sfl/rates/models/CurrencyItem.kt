package com.sfl.rates.models

import com.sfl.rates.enums.CurrencyEnum

data class CurrencyItem(
    val currencyEnum: CurrencyEnum,
    val description: String,
    val isSelected: Boolean
)