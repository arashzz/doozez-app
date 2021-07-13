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
import com.doozez.doozez.api.enqueue
import com.doozez.doozez.api.invitation.InvitationDetailResponse
import com.doozez.doozez.databinding.FragmentSafeInvitationsBinding
import com.doozez.doozez.ui.safe.adapters.SafeDetailInviteListAdapter
import com.doozez.doozez.utils.BundleKey
import com.google.android.material.snackbar.Snackbar

class SafeInvitationsTabFragment() : Fragment() {
    private var safeId: Int = 0
    private var _binding: FragmentSafeInvitationsBinding? = null
    private val binding get() = _binding!!
    private val adapter = SafeDetailInviteListAdapter(mutableListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            safeId = it.getInt(BundleKey.SAFE_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSafeInvitationsBinding.inflate(inflater, container, false)
        with(binding.safeDetailInviteList) {
            layoutManager = LinearLayoutManager(context)
            adapter = this@SafeInvitationsTabFragment.adapter
        }
        loadInvites()
        return binding.root
    }

    private fun loadInvites() {
        val call = ApiClient.invitationService.getInvitationsForSafe(safeId)
        call.enqueue {
            onResponse = {
                if (it.isSuccessful && it.body() != null) {
                    adapter.addItems(it.body())
                    adapter.notifyDataSetChanged()
                }
            }
            onFailure = {
                Log.e("SafeDetailFragment-invitation-list", it?.stackTrace.toString())
                Snackbar.make(
                    binding.safeDetailInviteList,
                    "Failed to load Invitation list",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    companion object {
        fun newInstance(safeId: Int) = SafeInvitationsTabFragment().apply {
            arguments = bundleOf(BundleKey.SAFE_ID to safeId)
        }
    }
}