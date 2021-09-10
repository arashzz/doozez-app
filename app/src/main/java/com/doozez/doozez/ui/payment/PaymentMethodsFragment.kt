package com.doozez.doozez.ui.payment

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
import com.doozez.doozez.databinding.FragmentPaymentMethodsBinding
import com.doozez.doozez.ui.payment.adapters.PaymentMethodsAdapter
import com.doozez.doozez.ui.payment.listeners.PaymentMethodItemListener
import com.doozez.doozez.utils.*
import com.google.android.material.snackbar.Snackbar

class PaymentMethodsFragment : Fragment(), PaymentMethodItemListener {

    private var _binding: FragmentPaymentMethodsBinding? = null
    private val binding get() = _binding!!
    private var adapter: PaymentMethodsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = PaymentMethodsAdapter(mutableListOf(), this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentMethodsBinding.inflate(inflater, container, false)
        with(binding.paymentMethodList) {
            layoutManager = LinearLayoutManager(context)
            adapter = this@PaymentMethodsFragment.adapter
        }
        loadPayments()
        addListeners()
        return binding.root
    }

    private fun addListeners() {
        binding.addPaymentMethod.setOnClickListener {
            findNavController().navigate(R.id.nav_payment_methods_to_nav_payment_create, bundleOf(
                BundleKey.PAYMENT_METHOD_CREATE_MODE to PaymentMethodCreateMode.CREATE
            ))
        }
    }

    private fun loadPayments() {
        val call = ApiClient.PAYMENT_METHOD_SERVICE.getPayments()
        call.enqueue {
            onResponse = {
                if(it.isSuccessful && it.body() != null) {
                    adapter?.addItems(it.body())
                }
            }
            onFailure = {
                Log.e("PaymentMethodsFragment", it?.stackTrace.toString())
                Snackbar.make(binding.paymentMethodsContainer, "Failed to get payment methods", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun paymentMethodClicked(paymentMethod: PaymentMethodDetailResp) {
        findNavController().navigate(R.id.nav_payment_methods_to_nav_payment_create, bundleOf(
            BundleKey.PAYMENT_METHOD_CREATE_MODE to PaymentMethodCreateMode.EDIT,
            BundleKey.PAYMENT_METHOD_ID to paymentMethod.id
        ))
    }
}