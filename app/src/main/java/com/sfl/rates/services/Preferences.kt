package com.sfl.rates.services


import android.content.SharedPreferences
import com.sfl.rates.enums.CurrencyEnum
import com.sfl.rates.utils.GsonUtils
import org.koin.core.KoinComponent
import org.koin.core.inject

sealed class RatePreference<T>(val key: String, val defaultValue: T) : KoinComponent {

    val preference: SharedPreferences by inject()

    object RateCurrency :
        RatePreference<String>("currency", "") {

        fun getAsObject(): CurrencyEnum {
            return GsonUtils.getCurrencyEnumFromJson(preference.get(this))
        }

        override fun get(): String {
            return preference.get(this)
        }

        override fun set(json: String) {
            preference.put(this, json)
        }

        fun set(value: CurrencyEnum) {
            preference.put(this, GsonUtils.currencyEnumToJson(value))
        }
    }

    object CashType :
        RatePreference<String>("cash_type", "") {

        fun getAsObject(): com.sfl.rates.enums.CashType {
            return GsonUtils.getCashEnumFromJson(preference.get(this))
        }

        override fun get(): String {
            return preference.get(this)
        }

        override fun set(json: String) {
            preference.put(this, json)
        }

        fun set(value: com.sfl.rates.enums.CashType) {
            preference.put(this, GsonUtils.cashEnumToJson(value))
        }
    }

    abstract fun get(): T
    abstract fun set(value: T)

}

inline fun <reified T> SharedPreferences.put(
    item: RatePreference<T>,
    value: Any,
    immediate: Boolean = false
) {
    val editor = this.edit()

    when (T::class) {
        Boolean::class -> editor.putBoolean(item.key, value as Boolean)
        Float::class -> editor.putFloat(item.key, value as Float)
        Int::class -> editor.putInt(item.key, value as Int)
        Long::class -> editor.putLong(item.key, value as Long)
        String::class -> editor.putString(item.key, value as String)
        else -> {
            if (item.defaultValue is Set<*>) {
                editor.putStringSet(item.key, value as Set<String>)
            }
        }
    }

    if (immediate) editor.commit() else editor.apply()

}

inline fun <reified T> SharedPreferences.get(item: RatePreference<T>): T {
    when (T::class) {
        Boolean::class -> return this.getBoolean(item.key, item.defaultValue as Boolean) as T
        Float::class -> return this.getFloat(item.key, item.defaultValue as Float) as T
        Int::class -> return this.getInt(item.key, item.defaultValue as Int) as T
        Long::class -> return this.getLong(item.key, item.defaultValue as Long) as T
        String::class -> return this.getString(item.key, item.defaultValue as String) as T
        else -> {
            if (item.defaultValue is Set<*>) {
                return this.getStringSet(item.key, item.defaultValue as Set<String>) as T
            }
        }
    }

    return item.defaultValue
}

fun Boolean?.orFalse() = this ?: false

fun Boolean?.orTrue() = this ?: true