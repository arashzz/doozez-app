package com.doozez.doozez.ui.safe

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.doozez.doozez.R
import com.doozez.doozez.api.ApiClient
import com.doozez.doozez.api.safe.SafeDetailResponse
import com.doozez.doozez.databinding.FragmentSafeDetailBinding
import com.doozez.doozez.ui.safe.adapters.SafeDetailInviteListAdapter
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_SAFE_ID = "safeId"

class SafeDetailFragment : Fragment() {
    private var safeId: Long = 0
    private var _binding: FragmentSafeDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            safeId = it.getLong(ARG_SAFE_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        _binding = FragmentSafeDetailBinding.inflate(inflater, container, false)
        val view = binding.root
        getSafeDetails()
        addListeners()
        return view
    }

    private fun addListeners() {
        binding.safeDetailAddInvite.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.action_nav_safe_to_nav_user_search)
        }
    }

    private fun getSafeDetails() {
        val call = ApiClient.safeService.getSafeByIdForUser(safeId)
        call.enqueue(
            object : Callback<SafeDetailResponse> {
                override fun onResponse(
                    call: Call<SafeDetailResponse>,
                    response: Response<SafeDetailResponse>,
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        populateSafeDetails(response.body())
                    }
                }

                override fun onFailure(call: Call<SafeDetailResponse>, t: Throwable) {
                    Log.e("SafeDetailFragment", t.stackTrace.toString())
                    Snackbar.make(binding.safeDetailContainer, "failed...", Snackbar.LENGTH_SHORT).show()
                }
            },
        )
    }

    private fun populateSafeDetails(detail: SafeDetailResponse) {
        binding.safeDetailName.text = detail.name
        binding.safeDetailMonthlyPayment.text = detail.monthlyPayment.toString()
        binding.safeDetailStatus.text = detail.status
        binding.safeDetailTotalInvites.text = detail.invitations.size.toString() + " participants"

        with(binding.safeDetailInviteList) {
            layoutManager = LinearLayoutManager(context)
            adapter = SafeDetailInviteListAdapter(detail.invitations)
        }

    }
}