package com.doozez.doozez.ui.invitation

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.doozez.doozez.R
import com.doozez.doozez.api.ApiClient
import com.doozez.doozez.api.invitation.InvitationDetailResponse
import com.doozez.doozez.databinding.FragmentInvitationListBinding
import com.doozez.doozez.databinding.FragmentSafeCreateBinding
import com.doozez.doozez.ui.invitation.adapters.InvitationListAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A fragment representing a list of Items.
 */
class InvitationListFragment : Fragment() {
    private var _binding: FragmentInvitationListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInvitationListBinding.inflate(inflater, container, false)
        getInvites()
        return binding.root
    }

    private fun getInvites() {
        val call = ApiClient.invitationService.getInvitationsForUser("1")
        call.enqueue(object : Callback<List<InvitationDetailResponse>> {
            override fun onResponse(call: Call<List<InvitationDetailResponse>>, response: Response<List<InvitationDetailResponse>>) {
                if (response.isSuccessful && response.body() != null) {
                    with(binding.inviteList) {
                        layoutManager = LinearLayoutManager(context)
                        adapter = InvitationListAdapter(response.body().toMutableList())
                    }
                }
            }
            override fun onFailure(call: Call<List<InvitationDetailResponse>>, t: Throwable) {
                //TODO: do something
            }
        })
    }
}