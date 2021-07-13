package com.doozez.doozez.ui.safe

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.doozez.doozez.api.ApiClient
import com.doozez.doozez.api.SharedPrefManager
import com.doozez.doozez.api.enqueue
import com.doozez.doozez.databinding.FragmentTabSafeParticipantsBinding
import com.doozez.doozez.ui.safe.adapters.SafeDetailInviteListAdapter
import com.doozez.doozez.ui.safe.adapters.SafeDetailParticipantListAdapter
import com.doozez.doozez.utils.BundleKey
import com.doozez.doozez.utils.SharedPrerfKey
import com.google.android.material.snackbar.Snackbar

class SafeParticipantsTabFragment() : Fragment() {
    private var safeId: Int = 0
    private var userId: Int = 0
    private var _binding: FragmentTabSafeParticipantsBinding? = null
    private val binding get() = _binding!!
    private val adapter = SafeDetailParticipantListAdapter(mutableListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            safeId = it.getInt(BundleKey.SAFE_ID)
        }
        userId = SharedPrefManager.getInt(SharedPrerfKey.USER_ID)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTabSafeParticipantsBinding.inflate(inflater, container, false)
        with(binding.safeDetailParticipantsList) {
            layoutManager = LinearLayoutManager(context)
            adapter = this@SafeParticipantsTabFragment.adapter
        }
        loadParticipants()
        return binding.root
    }

    private fun loadParticipants() {
        val call = ApiClient.participationService.getParticipationsForSafe(safeId)
        call.enqueue {
            onResponse = {
                if (it.isSuccessful && it.body() != null) {
                    adapter.addItems(it.body())
                    adapter.notifyDataSetChanged()
                }
            }
            onFailure = {
                Log.e("SafeDetailFragment-participation-list", it?.stackTrace.toString())
                Snackbar.make(
                    binding.safeDetailParticipantsList,
                    "Failed to load Participation list",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    companion object {
        fun newInstance(safeId: Int) = SafeParticipantsTabFragment().apply {
            arguments = bundleOf(BundleKey.SAFE_ID to safeId)
        }
    }
}