package com.sfl.rates.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.sfl.rates.R
import com.sfl.rates.enums.CurrencyEnum
import com.sfl.rates.models.CurrencyItem
import kotlinx.android.synthetic.main.currency_dialog_item.view.*

class ExchangeCurrencyDialogAdapter(private val currencyData: List<CurrencyItem>) :
    RecyclerView.Adapter<ExchangeCurrencyDialogAdapter.DialogViewHolder>() {

    var onCurrencyDialogItemClick: ((CurrencyEnum) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DialogViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.currency_dialog_item, parent, false)
    )

    override fun getItemCount() = currencyData.size

    override fun onBindViewHolder(holder: DialogViewHolder, position: Int) {
        val item = currencyData[position]
        holder.currencyIconImageView.setImageResource(item.currencyEnum.drawableID)
        holder.currencyTitleTextView.text = item.currencyEnum.name
        holder.currencyDescriptionTextView.text = item.description
        holder.currencySelectedCheckBox.isChecked = item.isSelected
        holder.currencyDialogItemRootLinearLayout.setOnClickListener {
            onCurrencyDialogItemClick?.invoke(item.currencyEnum)
        }
    }


    inner class DialogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val currencyDialogItemRootLinearLayout: LinearLayoutCompat =
            itemView.currencyDialogItemRootLinearLayout
        val currencyIconImageView: AppCompatImageView = itemView.currencyIconImageView
        val currencyTitleTextView: AppCompatTextView = itemView.currencyTitleTextView
        val currencyDescriptionTextView: AppCompatTextView = itemView.currencyDescriptionTextView
        val currencySelectedCheckBox: AppCompatCheckBox = itemView.currencySelectedCheckBox
    }
}