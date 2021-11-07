package com.doozez.doozez.ui.safe

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.doozez.doozez.R
import com.doozez.doozez.api.ApiClient
import com.doozez.doozez.api.enqueue
import com.doozez.doozez.api.safe.SafeCreateReq
import com.doozez.doozez.api.safe.SafeDetailResp
import com.doozez.doozez.databinding.FragmentSafeCreateBinding
import com.doozez.doozez.utils.*
import com.google.android.material.snackbar.Snackbar

class SafeCreateFragment : Fragment() {
    private var _binding: FragmentSafeCreateBinding? = null
    private val binding get() = _binding!!
    private var paymentID = 0
    private val TAG = "SafeCreateFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        setFragmentResultListener(ResultKey.PAYMENT_METHOD_SELECTED) { _, bundle ->
            val resultOk = bundle.getBoolean(BundleKey.RESULT_OK)
            if (resultOk) {
                paymentID = bundle.getInt(BundleKey.PAYMENT_METHOD_ID)
                val paymentMethodName = bundle.getString(BundleKey.PAYMENT_METHOD_NAME)
                if (!paymentMethodName.isNullOrBlank()) {
                    binding.safeCreatePaymentSelect.text = paymentMethodName
                }
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
        binding.safeCreatePaymentSelect.setOnClickListener {
            findNavController().navigate(R.id.action_nav_safe_to_nav_payment_method_list)
        }
        binding.safeCreateCreate.setOnClickListener {
            if (validateInput()) {
                createSafe(
                    binding.safeCreateName.editText?.text.toString(),
                    binding.safeCreateMonthlyPayment.editText?.text.toString().toInt())
            }
        }
    }

    private fun createSafe(safeName: String, monthlyPayment: Int) {
        val call = ApiClient.safeService.createSafeForUser(SafeCreateReq(safeName, monthlyPayment, paymentID!!))
        call.enqueue {
            onResponse = {
                if(it.isSuccessful) {
                    findNavController().popBackStack()
                } else {
                    Log.e(TAG, it.errorBody().string())
                    Snackbar.make(
                        binding.safeCreateContainer,
                        "Failed to create Safe",
                        Snackbar.LENGTH_SHORT).show()
                }

            }
            onFailure = {
                Log.e(TAG, it?.stackTrace.toString())
                Snackbar.make(
                    binding.safeCreateContainer,
                    "Failed to create Safe",
                    Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    //TODO: improvement needed
    private fun validateInput(): Boolean {
        val name = binding.safeCreateName.editText?.text.toString()
        val payment = binding.safeCreateMonthlyPayment.editText?.text.toString()
        var valid = true
        if (paymentID <= 0) {
            valid = false
            Snackbar.make(
                binding.safeCreateContainer,
                "Payment method is not set",
                Snackbar.LENGTH_SHORT).show()
        }
        if(name.isBlank()) {
            binding.safeCreateName.error = "This field is required"
            valid = false
        }
        if(payment.isBlank()) {
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