package com.sfl.rates.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.sfl.rates.R
import com.sfl.rates.models.BankDetails
import kotlinx.android.synthetic.main.branch_item.view.*

class BankBranchesAdapter : RecyclerView.Adapter<BankBranchesAdapter.BankBranchesViewHolder>() {

    private val branchesList = arrayListOf<BankDetails.BankBranchDetail>()
    var onBranchClick: ((BankDetails.BankBranchDetail) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        BankBranchesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.branch_item, parent, false)
        )

    override fun getItemCount() = branchesList.size


    override fun onBindViewHolder(holder: BankBranchesViewHolder, position: Int) {
        val item = branchesList[position]
        holder.branchItemRootLinearLayout.setOnClickListener {
            onBranchClick?.invoke(item)
        }
        holder.branchTitleTextView.text = item.address?.en
    }

    fun update(branchesList: List<BankDetails.BankBranchDetail>) {
        this.branchesList.clear()
        this.branchesList.addAll(branchesList)
        notifyDataSetChanged()
    }

    inner class BankBranchesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val branchItemRootLinearLayout: LinearLayoutCompat = itemView.branchItemRootLinearLayout
        val branchTitleTextView: AppCompatTextView = itemView.branchTitleTextView
    }

}