package com.doozez.doozez.ui.payment

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.doozez.doozez.R
import com.doozez.doozez.api.ApiClient
import com.doozez.doozez.api.enqueue
import com.doozez.doozez.api.payments.PaymentCreateReq
import com.doozez.doozez.databinding.FragmentPaymentMethodCreateBinding
import com.doozez.doozez.utils.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar

class PaymentMethodCreateFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentPaymentMethodCreateBinding? = null
    private val binding get() = _binding!!
    private var createMode: PaymentMethodCreateMode = PaymentMethodCreateMode.CREATE
    private var paymentMethodID: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            createMode = it.get(BundleKey.PAYMENT_METHOD_CREATE_MODE) as PaymentMethodCreateMode
            if (createMode == PaymentMethodCreateMode.EDIT) {
                paymentMethodID = it.getInt(BundleKey.PAYMENT_METHOD_ID)
            }
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
        _binding = FragmentPaymentMethodCreateBinding.inflate(inflater, container, false)
        addListeners()
        return binding.root
    }

//    private fun loadPaymentMethodDetail(details: PaymentDetailResp) {
//        binding.paymentMethodName.editText?.setText(details.name)
//    }

//    private fun applyChangesForMode() {
//        if (createMode == PaymentMethodCreateMode.DETAILS) {
//            binding.paymentMethodCreateCreate.visibility = View.GONE
//            binding.paymentMethodCreateEdit.visibility = View.VISIBLE
//        }
//    }

    private fun addListeners() {
//        binding.paymentMethodCreateEdit.setOnClickListener {
//            val body = PaymentCreateReq(binding.paymentMethodName.editText?.text.toString(), false)
//            val call = ApiClient.paymentService.
//        }
        binding.paymentMethodCreateCreate.setOnClickListener {
            val body = PaymentCreateReq(binding.paymentMethodName.editText?.text.toString(), false)
            val call = ApiClient.paymentService.createPayment(body)
            call.enqueue {
                onResponse = {
                    if(it.isSuccessful && it.body() != null) {
                        findNavController().navigate(
                            R.id.nav_payment_method_create_to_nav_payment_create_redirect,
                            bundleOf(
                                BundleKey.PAYMENT_METHOD_REDIRECT_URL to it.body().redirectURL,
                                BundleKey.PAYMENT_METHOD_TYPE to PaymentMethodType.DIRECT_DEBIT,
                                BundleKey.PAYMENT_METHOD_ID to it.body().id
                            )
                        )
                    } else {
                        Log.e("PaymentMethodCreateFragment", it.errorBody().string())
                        Snackbar.make(
                            binding.paymentMethodCreateContainer,
                            "Failed to create Payment method",
                            Snackbar.LENGTH_SHORT).show()
                    }
                }
                onFailure = {
                    Log.e("PaymentMethodCreateFragment", it?.stackTrace.toString())
                    Snackbar.make(
                        binding.paymentMethodCreateContainer,
                        "Unknown Error",
                        Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        setFragmentResultListener(ResultKey.PAYMENT_METHOD_CREATE_INITIATED) { _, bundle ->
            dismiss()
//            val resultOk = bundle.getBoolean(BundleKey.RESULT_OK)
//            if (resultOk) {
//                val paymentID = bundle.getInt(BundleKey.PAYMENT_METHOD_ID)
//                checkPaymentStatus(paymentID)
//
//            } else {
//                var reason = bundle.getString(BundleKey.FAIL_REASON)
//                if (reason == null) {
//                    reason = "unknown error"
//                }
//                Snackbar.make(
//                    binding.paymentMethodCreateContainer,
//                    reason,
//                    Snackbar.LENGTH_SHORT).show()
//            }
        }
    }

    private fun loadPaymentMethodDetails() {

    }

//    private fun checkPaymentStatus(id: Int) {
//        if(id > 0) {
//            val call = ApiClient.paymentService.getPaymentById(id)
//            call.enqueue {
//                onResponse = {
//                    if(it.isSuccessful && it.body() != null) {
//                        setFragmentResult(
//                            ResultKey.PAYMENT_METHOD_CREATE_INITIATED,
//                            bundleOf(
//                                BundleKey.RESULT_OK to true,
//                                BundleKey.PAYMENT_METHOD_ID to it.body().id
//                            )
//                        )
//                    } else {
//                        Log.e("PaymentMethodCreateFragment", it.errorBody().string())
//                        Snackbar.make(
//                            binding.paymentMethodCreateContainer,
//                            "Failed to create Payment method",
//                            Snackbar.LENGTH_SHORT).show()
//                    }
//                }
//                onFailure = {
//                    Log.e("PaymentMethodCreateFragment", it?.stackTrace.toString())
//                    Snackbar.make(
//                        binding.paymentMethodCreateContainer,
//                        "Unknown Error",
//                        Snackbar.LENGTH_SHORT).show()
//                }
//            }
//        } else {
//            Snackbar.make(
//                binding.paymentMethodCreateContainer,
//                "Invalid Payment selected",
//                Snackbar.LENGTH_SHORT).show()
//        }
//    }
}