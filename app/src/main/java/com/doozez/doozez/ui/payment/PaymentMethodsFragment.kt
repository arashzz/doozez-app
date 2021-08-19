package com.doozez.doozez.ui.payment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.doozez.doozez.R
import com.doozez.doozez.api.ApiClient
import com.doozez.doozez.api.enqueue
import com.doozez.doozez.api.payments.PaymentDetailResp
import com.doozez.doozez.databinding.FragmentPaymentListBinding
import com.doozez.doozez.databinding.FragmentPaymentMethodsBinding
import com.doozez.doozez.ui.payment.adapters.PaymentListAdapter
import com.doozez.doozez.ui.payment.adapters.PaymentMethodsAdapter
import com.doozez.doozez.ui.payment.listeners.PaymentMethodItemListener
import com.doozez.doozez.utils.BundleKey
import com.doozez.doozez.utils.PaymentStatus
import com.doozez.doozez.utils.PaymentType
import com.doozez.doozez.utils.ResultKey
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
            findNavController().navigate(R.id.nav_payment_methods_to_nav_payment_create)
        }
    }

    private fun loadPayments() {
        val call = ApiClient.paymentService.getPayments()
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

    override fun paymentMethodClicked(paymentMethod: PaymentDetailResp) {
        TODO("Not yet implemented")
    }
}