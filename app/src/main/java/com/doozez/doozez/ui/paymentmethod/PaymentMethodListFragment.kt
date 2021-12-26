package com.doozez.doozez.ui.paymentmethod

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.doozez.doozez.R
import com.doozez.doozez.api.ApiClient
import com.doozez.doozez.api.enqueue
import com.doozez.doozez.api.paymentMethod.PaymentMethodDetailResp
import com.doozez.doozez.databinding.FragmentPaymentMethodListBinding
import com.doozez.doozez.enums.BundleKey
import com.doozez.doozez.enums.PaymentMethodCreateMode
import com.doozez.doozez.ui.paymentmethod.adapters.PaymentMethodListAdapter
import com.doozez.doozez.ui.paymentmethod.listeners.PaymentMethodItemListener
import com.doozez.doozez.utils.*
import com.google.android.material.snackbar.Snackbar

class PaymentMethodListFragment : Fragment(), PaymentMethodItemListener {

    private var _binding: FragmentPaymentMethodListBinding? = null
    private val binding get() = _binding!!
    private var adapter: PaymentMethodListAdapter? = null
    private var action: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = PaymentMethodListAdapter(mutableListOf(), this)
        arguments?.let {
            action = it.getString("ACTION")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentMethodListBinding.inflate(inflater, container, false)
        with(binding.paymentMethodList) {
            layoutManager = LinearLayoutManager(context)
            adapter = this@PaymentMethodListFragment.adapter
        }
        loadPayments()
        addListeners()
        if(!action.isNullOrBlank() && action == "CREATE") {
            navigateToCreate()
        }
        return binding.root
    }

    private fun addListeners() {
        binding.addPaymentMethod.setOnClickListener {
            navigateToCreate()
        }
    }

    private fun loadPayments() {
        val call = ApiClient.PAYMENT_METHOD_SERVICE.getPayments()
        call.enqueue {
            onResponse = {
                if(it.isSuccessful && it.body() != null) {
                    if(it.body().results.isNotEmpty()) {
                        binding.paymentMethodListNoDataText.visibility = View.GONE
                        binding.paymentMethodListNoDataImage.visibility = View.GONE
                        binding.paymentMethodList.visibility = View.VISIBLE
                    }
                    adapter?.addItems(it.body().results)
                }
            }
            onFailure = {
                Log.e("PaymentMethodsFragment", it?.stackTrace.toString())
                Snackbar.make(binding.paymentMethodsContainer, "Failed to get payment methods", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun paymentMethodClicked(paymentMethod: PaymentMethodDetailResp) {
        findNavController().navigate(R.id.nav_payment_method_list_to_nav_payment_create, bundleOf(
            BundleKey.PAYMENT_METHOD_CREATE_MODE.name to PaymentMethodCreateMode.EDIT,
            BundleKey.PAYMENT_METHOD_ID.name to paymentMethod.id
        ))
    }

    private fun navigateToCreate() {
        findNavController().navigate(R.id.nav_payment_method_list_to_nav_payment_create, bundleOf(
            BundleKey.PAYMENT_METHOD_CREATE_MODE.name to PaymentMethodCreateMode.CREATE
        ))
    }
}