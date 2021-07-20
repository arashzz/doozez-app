package com.doozez.doozez.ui.safe

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.doozez.doozez.R
import com.doozez.doozez.api.ApiClient
import com.doozez.doozez.api.enqueue
import com.doozez.doozez.api.payments.PaymentCreateReq
import com.doozez.doozez.api.safe.SafeCreateRequest
import com.doozez.doozez.api.safe.SafeDetailResponse
import com.doozez.doozez.databinding.FragmentSafeCreateBinding
import com.doozez.doozez.ui.payment.PaymentCreateRedirectFragment
import com.doozez.doozez.utils.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar

class SafeCreateFragment : Fragment() {
    private var _binding: FragmentSafeCreateBinding? = null
    private val binding get() = _binding!!
    private var paymentID: Int? = null
    private var paymentCompleted = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSafeCreateBinding.inflate(inflater, container, false)
//        //TODO: implement for higher versions
//        // SOFT_INPUT_ADJUST_RESIZE is deprecated in version 30
//        // I'm using it for testing
//        when (Build.VERSION.SDK_INT) {
//            in 1..30 -> {
//                dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
//            } else -> {
//                Log.i("do nothing","do nothing")
//            }
//        }
        addListeners()
        return binding.root
    }

    private fun addListeners() {
        setFragmentResultListener(ResultKey.PAYMENT_METHOD_CREATE_INITIATED) { _, bundle ->
            val resultOk = bundle.getBoolean(BundleKey.RESULT_OK)
            if (resultOk) {
                val paymentID = bundle.getInt(BundleKey.PAYMENT_METHOD_ID)
                checkPaymentStatus(paymentID)

            } else {
                var reason = bundle.getString(BundleKey.FAIL_REASON)
                if (reason == null) {
                    reason = "unknown error"
                }
                Snackbar.make(
                    binding.safeCreateContainer,
                    reason,
                    Snackbar.LENGTH_SHORT).show()
            }
        }
        binding.safeCreatePaymentCreate.setOnClickListener {
            findNavController().navigate(
                R.id.action_nav_safe_create_to_nav_payment_create, bundleOf(
                    BundleKey.PAYMENT_METHOD_TYPE to PaymentType.DIRECT_DEBIT
                ))
        }
        binding.safeCreateCreate.setOnClickListener {
            if (validateInput()) {
                createSafe(
                    binding.safeCreateName.editText?.text.toString(),
                    binding.safeCreateMonthlyPayment.editText?.text.toString().toInt())
            }
        }
    }

    private fun checkPaymentStatus(id: Int) {
        if(id > 0) {
            val call = ApiClient.paymentService.getPaymentById(id)
            call.enqueue {
                onResponse = {
                    if(it.isSuccessful &&
                        it.body() != null &&
                        it.body().status == PaymentStatus.EXTERNAL_APPROVAL_SUCCESS) {
                        paymentCompleted = true
                        paymentID = id
                    } else {
                        Snackbar.make(
                            binding.safeCreateContainer,
                            "Payment method successfully added",
                            Snackbar.LENGTH_SHORT).show()
                    }
                }
                onFailure = {
                    Log.e("SafeListFragment", it?.stackTrace.toString())
                    returnNewSafe(false, null)
                }
            }
        } else {
            Snackbar.make(
                binding.safeCreateContainer,
                "Invalid Payment selected",
                Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun createSafe(safeName: String, monthlyPayment: Int) {
        var req = SafeCreateRequest(safeName, monthlyPayment, paymentID!!)

        val call = ApiClient.safeService.createSafeForUser(req)
        call.enqueue {
            onResponse = {
                returnNewSafe(it.isSuccessful, it.body())
            }
            onFailure = {
                Log.e("SafeListFragment", it?.stackTrace.toString())
                returnNewSafe(false, null)
            }
        }
    }

    private fun returnNewSafe(created: Boolean, safe: SafeDetailResponse?) {
        setFragmentResult(
            ResultKey.SAFE_ADDED,
            bundleOf(
                BundleKey.RESULT_OK to created,
                BundleKey.SAFE_OBJECT to safe
            )
        )
        findNavController().popBackStack()
    }

    //TODO: improvement needed
    private fun validateInput(): Boolean {
        var name = binding.safeCreateName.editText?.text.toString()
        var payment = binding.safeCreateMonthlyPayment.editText?.text.toString()
        var valid = true
        if (paymentID == null || !paymentCompleted) {
            valid = false
            Snackbar.make(
                binding.safeCreateContainer,
                "Payment method is not set",
                Snackbar.LENGTH_SHORT).show()
        }
        if(name.isNullOrBlank()) {
            binding.safeCreateName.error = "This field is required"
            valid = false
        }
        if(payment.isNullOrBlank()) {
            binding.safeCreateMonthlyPayment.error = "This field is required"
            valid = false
        }
        else if (!Utils.isInteger(payment)) {
            binding.safeCreateMonthlyPayment.error = "Only numbers are accepted"
            valid = false
        }
        return valid
    }
}