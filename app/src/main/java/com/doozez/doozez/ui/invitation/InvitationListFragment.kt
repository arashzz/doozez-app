package com.doozez.doozez.ui.invitation

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.doozez.doozez.MainActivity
import com.doozez.doozez.R
import com.doozez.doozez.api.ApiClient
import com.doozez.doozez.api.enqueue
import com.doozez.doozez.api.invitation.InvitationActionReq
import com.doozez.doozez.api.invitation.InvitationDetailResponse
import com.doozez.doozez.databinding.FragmentInvitationListBinding
import com.doozez.doozez.ui.invitation.adapters.InvitationListAdapter
import com.doozez.doozez.ui.invitation.listeners.OnInviteActionClickListener
import com.doozez.doozez.utils.BundleKey
import com.doozez.doozez.utils.InvitationAction
import com.doozez.doozez.utils.InvitationStatus
import com.doozez.doozez.utils.ResultKey
import com.google.android.material.snackbar.Snackbar

class InvitationListFragment : Fragment(), OnInviteActionClickListener {
    private var _binding: FragmentInvitationListBinding? = null
    private val binding get() = _binding!!
    private var adapter: InvitationListAdapter? = null
    private var ctx: Context? = null
    private var userId: Long = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ctx = context
    }

    override fun onDetach() {
        super.onDetach()
        ctx = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = (activity as MainActivity).getUserId()
        arguments?.let {
            userId = it.getLong(BundleKey.USER_ID)
        }
        adapter = InvitationListAdapter(mutableListOf(), userId, this, ctx!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInvitationListBinding.inflate(inflater, container, false)
        with(binding.inviteList) {
            layoutManager = LinearLayoutManager(context)
            adapter = this@InvitationListFragment.adapter
        }
        getInvites()
        addListeners()
        return binding.root
    }

    private fun addListeners() {
        setFragmentResultListener(ResultKey.PAYMENT_METHOD_SELECTED) { _, bundle ->
            if (bundle.getBoolean(BundleKey.RESULT_OK)) {
                val inviteId = bundle.getLong(BundleKey.INVITE_ID)
                val paymentId = bundle.getLong(BundleKey.PAYMENT_METHOD_ID)
                if (inviteId > 0 && paymentId > 0) {
                    updateInvite(inviteId, paymentId, InvitationAction.ACCEPT)
                }
            }
        }
    }

    private fun getInvites() {
        val call = ApiClient.invitationService.getInvitations()
        call.enqueue {
            onResponse = {
                if (it.isSuccessful && it.body() != null) {
                    adapter?.addItems(it.body().sortedByDescending { sit -> sit.status })
                    adapter?.notifyDataSetChanged()
                }
            }
            onFailure = {
                Log.e("InvitationListFragment", it?.stackTrace.toString())
                Snackbar.make(binding.inviteListContainer, "failed...", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun inviteAccepted(invite: InvitationDetailResponse) {
        AlertDialog.Builder(ctx)
            .setTitle("some title")
            .setMessage("Are you sure you want to accept this invitation?")
            .setPositiveButton("Yes") { dialog, _ ->
                loadPaymentMethodsForInvite(invite.id!!)
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    override fun inviteDeclined(invite: InvitationDetailResponse) {
        AlertDialog.Builder(ctx)
            .setTitle("some title")
            .setMessage("Are you sure you want to reject this invitation?")
            .setPositiveButton("Yes") { dialog, _ ->
                updateInvite(invite.id!!, 0, InvitationAction.DECLINE)
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private fun loadPaymentMethodsForInvite(inviteId: Long) {
        findNavController().navigate(R.id.action_nav_invitation_to_nav_payment_list, bundleOf(
            BundleKey.INVITE_ID to inviteId
        ))
    }

    private fun updateInvite(inviteId: Long, paymentId: Long, action: String) {
        val call = ApiClient.invitationService.updateInvitationForAction(
            inviteId,
            InvitationActionReq(action, paymentId))
        call.enqueue {
            onResponse = {
                if (it.isSuccessful && it.body() != null) {
                    //adapter?.itemStatusChanged(inviteId, it.body().status)
                    adapter?.itemStatusChanged(inviteId, InvitationStatus.ACCEPTED)
                    Snackbar.make(binding.inviteListContainer, "Invite updated", Snackbar.LENGTH_SHORT).show()
                }
            }
            onFailure = {
                Log.e("InvitationListFragment", it?.stackTrace.toString())
                Snackbar.make(binding.inviteListContainer, "failed to update invite", Snackbar.LENGTH_SHORT).show()
            }
        }

    }

    override fun inviteSafeClicked(invite: InvitationDetailResponse) {
        val bundle = bundleOf(
            BundleKey.SAFE_ID to invite.safe?.id,
            BundleKey.USER_ID to userId
        )
        findNavController().navigate(R.id.action_nav_invitation_to_nav_safe_detail, bundle)
    }
}