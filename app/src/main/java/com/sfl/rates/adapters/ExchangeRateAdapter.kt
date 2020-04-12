package com.sfl.rates.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.sfl.rates.R
import com.sfl.rates.enums.CashType
import com.sfl.rates.models.ExchangePointModel
import com.sfl.rates.services.RatePreference
import kotlinx.android.synthetic.main.exchange_point_item.view.*

class ExchangeRateAdapter :
    RecyclerView.Adapter<ExchangeRateAdapter.ExchangeViewHolder>() {

    private val exchangeList = arrayListOf<ExchangePointModel>()
    var onExchangeItemClick: ((ExchangePointModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ExchangeViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.exchange_point_item, parent, false
            )
        )

    override fun getItemCount() = exchangeList.size

    override fun onBindViewHolder(holder: ExchangeViewHolder, position: Int) {
        val item = exchangeList[position]
        val cashOrNonBool = RatePreference.CashType.getAsObject() == CashType.CASH
        holder.exchangeBankNameTextView.text = item.title

        if (cashOrNonBool) {
            holder.exchangeBankBuyPriceTextView.text =
                item.currencyList[RatePreference.RateCurrency.getAsObject().name]?.cache?.buy.toString()
            holder.exchangeBankSellPriceTextView.text =
                item.currencyList[RatePreference.RateCurrency.getAsObject().name]?.cache?.sell.toString()
        } else {
            holder.exchangeBankBuyPriceTextView.text =
                item.currencyList[RatePreference.RateCurrency.getAsObject().name]?.nonCache?.buy.toString()
            holder.exchangeBankSellPriceTextView.text =
                item.currencyList[RatePreference.RateCurrency.getAsObject().name]?.nonCache?.sell.toString()
        }

        holder.exchangeItemRootLinearLayout.setOnClickListener {
            onExchangeItemClick?.invoke(item)
        }
    }

    fun update(exchangeList: List<ExchangePointModel>) {
        this.exchangeList.clear()
        this.exchangeList.addAll(exchangeList)
        notifyDataSetChanged()
    }

    inner class ExchangeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val exchangeItemRootLinearLayout: LinearLayoutCompat = itemView.exchangeItemRootLinearLayout
        val exchangeBankNameTextView: AppCompatTextView = itemView.exchangeBankNameTextView
        val exchangeBankSellPriceTextView: AppCompatTextView =
            itemView.exchangeBankSellPriceTextView
        val exchangeBankBuyPriceTextView: AppCompatTextView = itemView.exchangeBankBuyPriceTextView
        val exchangeBankImageView: AppCompatImageView = itemView.exchangeBankImageView
    }

}