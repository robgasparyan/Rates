package com.sfl.rates.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.sfl.rates.R
import com.sfl.rates.enums.CurrencyEnum
import com.sfl.rates.models.ExchangePointModel
import kotlinx.android.synthetic.main.bank_currency_item.view.*

class BankCurrencyAdapter(
    private val titleAndFlagList: List<CurrencyEnum>
) :
    RecyclerView.Adapter<BankCurrencyAdapter.BankCurrencyViewHolder>() {
    private var cashOrNonCash = true
    private var currencyList = arrayListOf<ExchangePointModel.Currency>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BankCurrencyViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.bank_currency_item, parent, false)
    )

    override fun getItemCount() = currencyList.size

    override fun onBindViewHolder(holder: BankCurrencyViewHolder, position: Int) {
        val currencyItem = currencyList[position]
        val titleAndFlagItem = titleAndFlagList[position]
        holder.bankCurrencyImageView.setImageResource(titleAndFlagItem.drawableID)
        holder.bankCurrencyTextView.text = titleAndFlagItem.name
        if (cashOrNonCash) {
            holder.bankBuyPriceTextView.text = currencyItem.cache?.buy.toString()
            holder.bankSellPriceTextView.text = currencyItem.cache?.sell.toString()
        } else {
            holder.bankBuyPriceTextView.text = currencyItem.nonCache?.buy.toString()
            holder.bankSellPriceTextView.text = currencyItem.nonCache?.sell.toString()
        }

    }

    fun update(currencyList: List<ExchangePointModel.Currency>, cashOrNonCash: Boolean) {
        this.currencyList.clear()
        this.currencyList.addAll(currencyList)
        this.currencyList =
            this.currencyList.filter { if (cashOrNonCash) it.cache != null else it.nonCache != null } as ArrayList<ExchangePointModel.Currency>
        this.cashOrNonCash = cashOrNonCash
        notifyDataSetChanged()
    }

    inner class BankCurrencyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bankCurrencyImageView: AppCompatImageView = itemView.bankCurrencyImageView
        val bankCurrencyTextView: AppCompatTextView = itemView.bankCurrencyTextView
        val bankBuyPriceTextView: AppCompatTextView = itemView.bankBuyPriceTextView
        val bankSellPriceTextView: AppCompatTextView = itemView.bankSellPriceTextView
    }
}