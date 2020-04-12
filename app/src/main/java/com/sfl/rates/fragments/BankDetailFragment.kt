package com.sfl.rates.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.sfl.rates.BaseFragment
import com.sfl.rates.R
import com.sfl.rates.adapters.BankBranchesAdapter
import com.sfl.rates.adapters.BankCurrencyAdapter
import com.sfl.rates.models.BankDetails
import com.sfl.rates.models.ExchangePointModel
import com.sfl.rates.utils.Utils
import com.sfl.rates.utils.hide
import com.sfl.rates.utils.initRecyclerView
import com.sfl.rates.viewModels.ExchangeRateViewModel
import kotlinx.android.synthetic.main.fragment_bank_details.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class BankDetailFragment : BaseFragment() {

    private val exchangeRateViewModel: ExchangeRateViewModel by viewModel()

    override fun getLayoutId() = R.layout.fragment_bank_details

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val exchangeModel = arguments?.getSerializable("exchange_model") as ExchangePointModel
        bankTitleTextView.text = exchangeModel.title
        val bankBranchAdapter = BankBranchesAdapter()

        val bankCurrencyAdapter = BankCurrencyAdapter(
            Utils.convertCurrencyToCurrencyEnum(exchangeModel.currencyList)
        )
        bankCurrencyAdapter.update(exchangeModel.currencyList.values.toMutableList(), true)

        currencyRecyclerView?.initRecyclerView(context, bankCurrencyAdapter)

        branchesRecyclerView?.initRecyclerView(context, bankBranchAdapter)

        exchangeRateViewModel.getBankDetails(exchangeModel.uuid.orEmpty())
            .observe(viewLifecycleOwner, Observer {
                if (it.isSuccess) {
                    it.getOrNull()?.apply {
                        // already filtered list, for more detail see in repo
                        // first item is a head branch
                        bankDetails(this[0])
                        bankBranchAdapter.update(this)
                    }
                } else {

                }
            })
        bankBranchAdapter.onBranchClick = {
            bankDetails(it)
            appBarLayout.setExpanded(true)
        }

        cashRadioButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // cash state
                bankCurrencyAdapter.update(exchangeModel.currencyList.values.toMutableList(), true)
            }
        }

        nonCashRadioButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // non cash state
                bankCurrencyAdapter.update(exchangeModel.currencyList.values.toMutableList(), false)
            }
        }

    }

    private fun bankDetails(branch: BankDetails.BankBranchDetail) {
        addressTextView.text = branch.address?.en
        phoneNumberTextView.text = branch.contacts
        branchNameTextView.text = branch.title?.en

        when (val workingDaysAndHoursList = Utils.getWeekWorkDaysAndHours(branch.workhours)) {
            null -> {
                // it`s mean this branch working 1-7 days 24 hours
                workdayHourInfoRelativeLayout.hide()
            }
            else -> {
                if (workingDaysAndHoursList.size == 2) {
                    // in this case this branch working 1-5 days
                    workingDaysTextView.text = workingDaysAndHoursList[0]
                    workHoursDaysTextView.text = workingDaysAndHoursList[1]
                    workingWeekendDaysTextView.hide()
                    workHoursWeekendDaysTextView.hide()
                } else if (workingDaysAndHoursList.size == 4) {
                    // in this case this branch working 1-5 days and 6th day
                    workingDaysTextView.text = workingDaysAndHoursList[0]
                    workHoursDaysTextView.text = workingDaysAndHoursList[1]
                    workingWeekendDaysTextView.text = workingDaysAndHoursList[2]
                    workHoursWeekendDaysTextView.text = workingDaysAndHoursList[3]
                }
            }
        }
        viewOnMapTextView?.setOnClickListener {
//            Utils.openMap(context, branch.location?.lat, branch.location?.lng)
            view?.apply {
                val bundle = Bundle()
                bundle.putString("latlng", "${branch.location?.lat},${branch.location?.lng}")
                Navigation.findNavController(this).navigate(R.id.bankDeatilsToBankLocation, bundle)
            }
        }

    }

    override fun onNetworkChanged(state: Boolean) {

    }
}