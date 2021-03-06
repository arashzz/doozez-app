package com.doozez.doozez.ui.paymentmethod

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.doozez.doozez.R
import com.doozez.doozez.api.ApiClient
import com.doozez.doozez.api.enqueue
import com.doozez.doozez.api.paymentMethod.PaymentMethodDetailResp
import com.doozez.doozez.databinding.FragmentPaymentMethodSelectBinding
import com.doozez.doozez.ui.paymentmethod.adapters.PaymentMethodSelectAdapter
import com.doozez.doozez.ui.paymentmethod.listeners.PaymentMethodItemListener
import com.doozez.doozez.enums.BundleKey
import com.doozez.doozez.enums.PaymentMethodStatus
import com.doozez.doozez.enums.ResultKey
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar

class PaymentMethodSelectFragment : BottomSheetDialogFragment(), PaymentMethodItemListener {
    private var _binding: FragmentPaymentMethodSelectBinding? = null
    private val binding get() = _binding!!
    private var adapter: PaymentMethodSelectAdapter? = null
    private var inviteId = -1
    private var selectedPaymentMethodId = -1
    private var selectedPaymentMethod: PaymentMethodDetailResp? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            inviteId = it.getInt(BundleKey.INVITE_ID.name, -1)
            selectedPaymentMethodId = it.getInt(BundleKey.PAYMENT_METHOD_ID.name, -1)
        }
        adapter = PaymentMethodSelectAdapter(mutableListOf(), this, selectedPaymentMethodId)
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
        _binding = FragmentPaymentMethodSelectBinding.inflate(inflater, container, false)
        with(binding.paymentListList) {
            layoutManager = LinearLayoutManager(context)
            adapter = this@PaymentMethodSelectFragment.adapter
        }
        loadPayments()
        addListeners()
        return binding.root
    }

    private fun loadPayments() {
        val call = ApiClient.PAYMENT_METHOD_SERVICE.getPayments()
        call.enqueue {
            onResponse = {
                if(it.isSuccessful && it.body() != null) {
                    val eligibleMethods = it.body().results.filter { pd ->
                        pd.status == PaymentMethodStatus.EXTERNALLY_ACTIVATED.code
                    }
                    if(eligibleMethods.isNotEmpty()) {
                        binding.paymentListNoDataText.visibility = View.GONE
                        binding.paymentListNoDataImage.visibility = View.GONE
                        binding.paymentListList.visibility = View.VISIBLE
                        binding.paymentListProceed.isEnabled = true
                        binding.paymentListProceed.setOnClickListener {
                            if (selectedPaymentMethod != null && selectedPaymentMethod!!.id > 0) {
                                returnSelectedPayment()
                                dismiss()
                            } else {
                                Snackbar.make(binding.paymentListContainer,
                                    "A payment method should be selected before accepting an invite",
                                    Snackbar.LENGTH_SHORT).show()

                            }
                        }
                        adapter?.addItems(eligibleMethods)
                    }
                }
            }
            onFailure = {
                Log.e("SafeListFragment", it?.stackTrace.toString())
                Snackbar.make(binding.paymentListContainer, "Failed to get payment methods", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun addListeners() {
        binding.paymentListAdd.setOnClickListener {
            dismiss()
            findNavController().navigate(R.id.action_nav_payment_method_select_to_nav_payment_methods, bundleOf(
                "ACTION" to "CREATE"
            ))
        }
        binding.paymentListCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun returnSelectedPayment() {
        setFragmentResult(
            ResultKey.PAYMENT_METHOD_SELECTED.name,
            bundleOf(
                BundleKey.RESULT_OK.name to true,
                BundleKey.INVITE_ID.name to inviteId,
                BundleKey.PAYMENT_METHOD_ID.name to selectedPaymentMethod!!.id,
                BundleKey.PAYMENT_METHOD_NAME.name  to selectedPaymentMethod!!.name
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun paymentMethodClicked(paymentMethod: PaymentMethodDetailResp) {
        selectedPaymentMethod = paymentMethod
    }
}