package com.doozez.doozez.ui.safe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.doozez.doozez.api.ApiClient
import com.doozez.doozez.api.safe.SafeCreateRequest
import com.doozez.doozez.databinding.FragmentSafeCreateBinding
import com.doozez.doozez.ui.safe.listeners.OnSafeCreatedListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 * Use the [SafeCreateFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SafeCreateFragment(listener: OnSafeCreatedListener) : DialogFragment() {
    private var _binding: FragmentSafeCreateBinding? = null
    private val binding get() = _binding!!
    private val listener: OnSafeCreatedListener = listener

    fun SafeCreateFragment(){ }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSafeCreateBinding.inflate(inflater, container, false)
        addListeners()
        return binding.root
    }

    private fun addListeners() {
        binding.safeCreateCancel.setOnClickListener {
            dismiss()
        }
        binding.safeCreateCreate.setOnClickListener {
            var req = SafeCreateRequest()
            req.userId = 1
            req.name = binding.safeCreateName.text.toString()
            req.monthlyPayment = binding.safeCreateMonthlyPayment.text.toString().toLong()

            val call = ApiClient.safeService.createSafeForUser(req)
            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        listener.onSuccessSafeCreate("Safe created successfully")
                        //TODO: redirect to safe detail page
                    }
                    else {
                        listener.onFailureSafeCreate("Failed to create Safe")
                    }
                    dismiss()
                }
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    listener.onFailureSafeCreate("ops something happened")
                    dismiss()
                }
            })
        }
    }
}