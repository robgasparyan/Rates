package com.sfl.rates.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.sfl.rates.BaseFragment
import com.sfl.rates.R
import com.sfl.rates.adapters.ExchangeCurrencyDialogAdapter
import com.sfl.rates.adapters.ExchangeRateAdapter
import com.sfl.rates.enums.CashType
import com.sfl.rates.models.ExchangePointModel
import com.sfl.rates.services.RatePreference
import com.sfl.rates.utils.*
import com.sfl.rates.viewModels.ExchangeRateViewModel
import kotlinx.android.synthetic.main.connection_layout.*
import kotlinx.android.synthetic.main.currency_dialog_layout.view.*
import kotlinx.android.synthetic.main.fragment_exchange_rates.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ExchangeRateFragment : BaseFragment() {

    private val exchangeRateViewModel: ExchangeRateViewModel by viewModel()
    private val exchangeAdapter = ExchangeRateAdapter()

    // Sorting variables, you cant use two filters at the same time.
    private var buySortingBool = false
    private var sellSortingBool = false

    // Raw exchange response for future processing
    private var rawListWithoutFiltering: List<ExchangePointModel>? = null

    override fun getLayoutId() = R.layout.fragment_exchange_rates


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setCurrencyValues()
        setCashValues()
        exchangeRecyclerView?.initRecyclerView(context, exchangeAdapter, useItemDecorator = true)
        getExchangeRateData()

        currencyChangeFrameLayout?.setOnClickListener {
            context?.apply {
                val builder = AlertDialog.Builder(this)
                val mView = LayoutInflater.from(this).inflate(R.layout.currency_dialog_layout, null)
                builder.setView(mView)
                builder.setCancelable(true)
                val dialog = builder.create()
                dialog.show()
                currencyDialogAndActions(mView, dialog)
            }
        }

        cashTypeChangeFrameLayout?.setOnClickListener {
            context?.apply {
                cashDialogAndActions(this)
            }
        }

        // Buy And Sell Sorting Logic
        buyTextView.setOnClickListener {
            // reset sell filter checker
            sellSortingBool = false

            buySortingBool = !buySortingBool
            // buyTextView change icon
            buyTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                null, null,
                if (buySortingBool) context?.getDrawable(R.drawable.ic_arrow)
                else context?.getDrawable(R.drawable.ic_down_arrow), null
            )
            // you mast reset pair drawable in this case sellTextView
            sellTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                null, null,
                context?.getDrawable(R.drawable.ic_down_arrow), null
            )
            // Sorting main logic
            rawListWithoutFiltering?.apply {
                exchangeAdapter.update(
                    exchangeRateViewModel.sortListByBuy(
                        this,
                        buySortingBool
                    )
                )
            }
        }

        sellTextView.setOnClickListener {
            // reset buy filter checker
            buySortingBool = false

            sellSortingBool = !sellSortingBool
            // buyTextView change icon
            sellTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                null, null,
                if (sellSortingBool) context?.getDrawable(R.drawable.ic_arrow)
                else context?.getDrawable(R.drawable.ic_down_arrow), null
            )
            // you mast reset pair drawable in this case sellTextView
            buyTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                null, null,
                context?.getDrawable(R.drawable.ic_down_arrow), null
            )
            rawListWithoutFiltering?.apply {
                exchangeAdapter.update(
                    exchangeRateViewModel.sortListBySell(
                        this,
                        sellSortingBool
                    )
                )
            }
        }

    }

    private fun getExchangeRateData() {
        exchangeRateViewModel.getRates().observe(viewLifecycleOwner, Observer {
            if (it.isSuccess) {
                it.getOrNull()?.apply {
                    rawListWithoutFiltering = this
                    exchangeAdapter.update(exchangeRateViewModel.filterListBeforeDraw(this))
                }
            } else {
                // handle error here
            }
        })

        // exchange adapter item click listener
        exchangeAdapter.onExchangeItemClick = { exchangePointModel ->
            view?.let {
                val bundle = Bundle()
                bundle.putSerializable("exchange_model", exchangePointModel)
                Navigation.findNavController(it).navigate(R.id.exchangeToBankDetail, bundle)
            }
        }
    }

    private fun setCurrencyValues() {
        // fetching currency information from Shared Preferences.
        val selectedCurrency = RatePreference.RateCurrency.getAsObject()
        exchangeCurrencyImageView.setImageResource(selectedCurrency.drawableID)
        exchangeCurrencyTextView.text = selectedCurrency.name
    }

    private fun setCashValues() {
        // fetching currency information from Shared Preferences.
        val selectedCashType = RatePreference.CashType.getAsObject()
        cashTypeTextView.text =
            if (selectedCashType == CashType.CASH) context?.getStringFromResources(R.string.cash) else
                context?.getStringFromResources(R.string.non_cache)

    }

    private fun cashDialogAndActions(context: Context) {
        // Cash,Non-Cash Dialog And Actions
        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.exchange)
        builder.setCancelable(true)
        builder.setSingleChoiceItems(
            arrayOf(
                context.getStringFromResources(R.string.cash),
                context.getStringFromResources(R.string.non_cache)
            ),
            if (RatePreference.CashType.getAsObject() == CashType.CASH) 0 else 1
        ) { dialog, which ->
            val selectedCashType = if (which == 0) CashType.CASH else CashType.NON_CASH

            if (RatePreference.CashType.getAsObject() != selectedCashType) {
                RatePreference.CashType.set(selectedCashType)
                setCashValues()
                exchangeAdapter.notifyDataSetChanged()
                buySortingBool = false
                buyTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    null, null,
                    context.getDrawable(R.drawable.ic_down_arrow), null
                )
            }
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun currencyDialogAndActions(view: View, currencyAlertDialog: AlertDialog) {
        // Currency Dialog And Actions
        val adapter =
            ExchangeCurrencyDialogAdapter(Utils.getPreDefinedExchangeCurrencyList(context))
        view.currencyDialogRecyclerView?.initRecyclerView(
            context, adapter
        )
        adapter.onCurrencyDialogItemClick = {
            currencyAlertDialog.dismiss()

            if (RatePreference.RateCurrency.getAsObject() != it) {
                // Need update list and currency information
                RatePreference.RateCurrency.set(it)
                getExchangeRateData()
                setCurrencyValues()
                buySortingBool = false
                buyTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    null, null,
                    context?.getDrawable(R.drawable.ic_down_arrow), null
                )
            }

        }
    }

    override fun onNetworkChanged(state: Boolean) {
        if (state) {
            viewStubRootLayout?.hide()
            currencyRootLinearLayout.show()
            bankSellButRootLinearLayout.show()
            exchangeRecyclerView.show()
            getExchangeRateData()
        } else {
            noConnectionViewStub?.inflate()
            currencyRootLinearLayout.hide()
            exchangeRecyclerView.hide()
            bankSellButRootLinearLayout.hide()
        }
    }
}