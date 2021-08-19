package com.doozez.doozez.ui.payment

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.doozez.doozez.R
import com.doozez.doozez.api.ApiClient
import com.doozez.doozez.api.enqueue
import com.doozez.doozez.api.payments.PaymentCreateReq
import com.doozez.doozez.databinding.FragmentPaymentListBinding
import com.doozez.doozez.databinding.FragmentPaymentMethodCreateBinding
import com.doozez.doozez.utils.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar

class PaymentMethodCreateFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentPaymentMethodCreateBinding? = null
    private val binding get() = _binding!!

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

    private fun addListeners() {
        binding.paymentMethodCreate.setOnClickListener {
            val body = PaymentCreateReq(binding.paymentMethodName.editText?.text.toString(), false)
            val call = ApiClient.paymentService.createPayment(body)
            call.enqueue {
                onResponse = {
                    if(it.isSuccessful && it.body() != null) {
                        findNavController().navigate(
                            R.id.nav_payment_method_create_to_nav_payment_create_redirect,
                            bundleOf(
                                BundleKey.PAYMENT_METHOD_REDIRECT_URL to it.body().redirectURL
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