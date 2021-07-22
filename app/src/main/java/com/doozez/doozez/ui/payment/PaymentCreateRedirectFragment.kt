package com.doozez.doozez.ui.payment

import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.doozez.doozez.api.ApiClient
import com.doozez.doozez.api.enqueue
import com.doozez.doozez.api.payments.PaymentCreateReq
import com.doozez.doozez.databinding.FragmentPaymentCreateBinding
import com.doozez.doozez.utils.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar


class PaymentCreateRedirectFragment : Fragment() {
    private var _binding: FragmentPaymentCreateBinding? = null
    private val binding get() = _binding!!
    private var creationInitiated = false
    private var type: PaymentType? = null
    private var paymentID: Int? = null
    private var safeId: Int? = null
    private var inviteId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = it.get(BundleKey.PAYMENT_METHOD_TYPE) as PaymentType
            safeId = it.getInt(BundleKey.SAFE_ID)
            inviteId = it.getInt(BundleKey.INVITE_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        FragmentPaymentCreateBinding.inflate(inflater, container, false).also { _binding = it }

        getRedirectURL()
        return binding.root
    }

    private fun getRedirectURL() {
        if (type == null) {
            Log.e(BundleReason.INVALID_PAYMENT_METHOD_ID.toString(),
                "Payment method type cannot be null")
            setResult(bundleOf(
                BundleKey.RESULT_OK to false,
                BundleKey.FAIL_REASON to BundleReason.INVALID_PAYMENT_METHOD_TYPE
            ))
        }
        //TODO: have a loader
        creationInitiated = true
        val call = ApiClient.paymentService.createPayment(PaymentCreateReq(true))
        call.enqueue {
            onResponse = {
                if (it.isSuccessful && it.body() != null) {
                    paymentID = it.body().id
                    initiatePaymentCreation(it.body().redirectURL)
                }
            }
            onFailure = {
                Log.e("PaymentCreateFragment", it?.stackTrace.toString())
                Snackbar.make(binding.paymentCreateContainer,
                    "Failed create payment method",
                    Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun initiatePaymentCreation(url: String) {

        loadRedirectView(url)
        setResult(bundleOf(
            BundleKey.RESULT_OK to true,
            BundleKey.SAFE_ID to safeId,
            BundleKey.INVITE_ID to inviteId,
            BundleKey.PAYMENT_METHOD_ID to paymentID,
            BundleKey.PAYMENT_METHOD_TYPE to type
        ))
    }

    private fun loadRedirectView(url: String) {
        val webView: WebView = binding.paymentCreateView
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return false
            }
        }
        webView.loadUrl(url)
    }

    private fun setResult(bundle: Bundle) {
        setFragmentResult(
            ResultKey.PAYMENT_METHOD_CREATE_INITIATED,
            bundle
        )
    }

    companion object {
        fun newInstance(type: PaymentType, safeId: Int?) = PaymentCreateRedirectFragment().apply {
            arguments = bundleOf(
                BundleKey.PAYMENT_METHOD_TYPE to type,
                BundleKey.SAFE_ID to safeId
            )
        }

     }
}