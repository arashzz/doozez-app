package com.doozez.doozez.ui.payment

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import com.doozez.doozez.api.ApiClient
import com.doozez.doozez.api.enqueue
import com.doozez.doozez.api.payments.PaymentDetailResp
import com.doozez.doozez.databinding.FragmentPaymentListBinding
import com.doozez.doozez.ui.payment.adapters.PaymentListAdapter
import com.doozez.doozez.utils.BundleKey
import com.doozez.doozez.utils.ResultKey
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar

class PaymentListFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentPaymentListBinding? = null
    private val binding get() = _binding!!
    private var adapter: PaymentListAdapter? = null
    private var inviteId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = PaymentListAdapter(mutableListOf())
        arguments?.let {
            inviteId = it.getLong(BundleKey.INVITE_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //TODO: implement for higher versions
        // SOFT_INPUT_ADJUST_RESIZE is deprecated in version 30
        // I'm using it for testing
        when (Build.VERSION.SDK_INT) {
            in 1..30 -> {
                dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            } else -> {
            Log.i("do nothing","do nothing")
        }
        }
        _binding = FragmentPaymentListBinding.inflate(inflater, container, false)
        with(binding.paymentListList) {
            layoutManager = LinearLayoutManager(context)
            adapter = this@PaymentListFragment.adapter
        }
        loadPayments()
        addListeners()
        return binding.root
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
                Log.e("SafeListFragment", it?.stackTrace.toString())
                Snackbar.make(binding.paymentListContainer, "Failed to get payment methods", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun addListeners() {
        binding.paymentListAccept.setOnClickListener {
            var selectedPayment = adapter?.getSelectedItem()
            if (selectedPayment != null) {
                returnSelectedPayment(true, selectedPayment)
                dismiss()
            } else {
                Snackbar.make(binding.paymentListContainer,
                    "A payment method should be selected before accepting an invite",
                    Snackbar.LENGTH_SHORT).show()

            }
        }
        binding.paymentListCancel.setOnClickListener {
            returnSelectedPayment(false, null)
            dismiss()
        }
    }

    private fun returnSelectedPayment(selected: Boolean, payment: PaymentDetailResp?) {
        setFragmentResult(
            ResultKey.PAYMENT_METHOD_SELECTED,
            bundleOf(
                BundleKey.RESULT_OK to selected,
                BundleKey.INVITE_ID to inviteId,
                BundleKey.PAYMENT_METHOD_ID to payment?.id
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}