package com.doozez.doozez.ui.paymentmethod

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.doozez.doozez.databinding.FragmentPaymentCreateRedirectBinding
import com.doozez.doozez.enums.BundleKey
import com.doozez.doozez.enums.PaymentMethodType


class PaymentMethodRedirectFragment : Fragment() {
    private var _binding: FragmentPaymentCreateRedirectBinding? = null
    private val binding get() = _binding!!
    private var methodType: PaymentMethodType? = null
    private var paymentMethodID: Int? = null
    private var redirectURL: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            methodType = it.get(BundleKey.PAYMENT_METHOD_TYPE.name) as PaymentMethodType
            paymentMethodID = it.getInt(BundleKey.PAYMENT_METHOD_ID.name)
            redirectURL = it.getString(BundleKey.PAYMENT_METHOD_REDIRECT_URL.name)
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        FragmentPaymentCreateRedirectBinding.inflate(inflater, container, false).also { _binding = it }
        triggerOverlay()
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

            override fun onPageFinished(view: WebView?, url: String?) {
                //triggerOverlay()
            }
        }
        webView.loadUrl(redirectURL!!)
    }



    private fun triggerOverlay() {
        var visibility = View.GONE
        if (binding.overlayLoader.progressView.visibility != View.VISIBLE) {
            visibility = View.VISIBLE
        }
        binding.overlayLoader.progressView.visibility = visibility
    }
}