package com.doozez.doozez.ui.payment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.doozez.doozez.api.ApiClient
import com.doozez.doozez.api.enqueue
import com.doozez.doozez.api.payments.PaymentCreateReq
import com.doozez.doozez.databinding.FragmentPaymentCreateBinding
import com.google.android.material.snackbar.Snackbar


class PaymentCreateFragment : Fragment() {
    private var _binding: FragmentPaymentCreateBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentCreateBinding.inflate(inflater, container, false)
        initiatePaymentCreation()
        return binding.root
    }

    private fun initiatePaymentCreation() {
        val call = ApiClient.paymentService.createPayment(PaymentCreateReq(true))
        call.enqueue {
            onResponse = {
                if (it.isSuccessful && it.body() != null) {
                    val webView: WebView = binding.paymentCreateView
                    val webSettings: WebSettings = webView.settings
                    webSettings.javaScriptEnabled = true
                    webView.webViewClient = object : WebViewClient() {
                        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                            view.loadUrl(url)
                            return false
                        }
                    }
                    webView.loadUrl(it.body().redirectURL!!)
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

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PaymentCreateFragment().apply {

            }
    }
}