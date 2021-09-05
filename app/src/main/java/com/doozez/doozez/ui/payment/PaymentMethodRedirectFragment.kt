package com.doozez.doozez.ui.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.doozez.doozez.databinding.FragmentPaymentCreateRedirectBinding
import com.doozez.doozez.utils.BundleKey
import com.doozez.doozez.utils.PaymentMethodType


class PaymentMethodRedirectFragment : Fragment() {
    private var _binding: FragmentPaymentCreateRedirectBinding? = null
    private val binding get() = _binding!!
    private var methodType: PaymentMethodType? = null
    private var paymentMethodID: Int? = null
    private var redirectURL: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            methodType = it.get(BundleKey.PAYMENT_METHOD_TYPE) as PaymentMethodType
            paymentMethodID = it.getInt(BundleKey.PAYMENT_METHOD_ID)
            redirectURL = it.getString(BundleKey.PAYMENT_METHOD_REDIRECT_URL)
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        FragmentPaymentCreateRedirectBinding.inflate(inflater, container, false).also { _binding = it }
        loadRedirectView()
        return binding.root
    }

    private fun loadRedirectView() {
        val webView: WebView = binding.paymentCreateView
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return false
            }
        }
        webView.loadUrl(redirectURL!!)
    }
}