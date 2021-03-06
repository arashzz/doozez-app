package com.doozez.doozez.ui.invitation

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.doozez.doozez.R
import com.doozez.doozez.api.ApiClient
import com.doozez.doozez.api.SharedPrefManager
import com.doozez.doozez.api.enqueue
import com.doozez.doozez.api.invitation.InviteActionReq
import com.doozez.doozez.api.invitation.InviteDetailResp
import com.doozez.doozez.databinding.FragmentInvitationListBinding
import com.doozez.doozez.enums.*
import com.doozez.doozez.ui.invitation.adapters.InvitationListAdapter
import com.doozez.doozez.ui.invitation.listeners.OnInviteActionClickListener
import com.doozez.doozez.utils.*
import com.google.android.material.snackbar.Snackbar

class InvitationListFragment : Fragment(), OnInviteActionClickListener {
    private var _binding: FragmentInvitationListBinding? = null
    private val binding get() = _binding!!
    private var adapter: InvitationListAdapter? = null
    private var ctx: Context? = null
    private var userId: Int = 0
    private var selectedInviteId: Int = 0

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

        userId = SharedPrefManager.getInt(SharedPrerfKey.USER_ID.name)
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
        setFragmentResultListener(ResultKey.PAYMENT_METHOD_SELECTED.name) { _, bundle ->
            val resultOk = bundle.getBoolean(BundleKey.RESULT_OK.name)
            if (resultOk) {
                val paymentID = bundle.getInt(BundleKey.PAYMENT_METHOD_ID.name)
                acceptInvite(selectedInviteId, paymentID)
            } else {
                var reason = bundle.getString(BundleKey.FAIL_REASON.name)
                if (reason == null) {
                    reason = "unknown error"
                }
                Snackbar.make(
                    binding.inviteListContainer,
                    reason,
                    Snackbar.LENGTH_SHORT).show()
            }
        }
//        setFragmentResultListener(ResultKey.PAYMENT_METHOD_CREATE_INITIATED) { _, bundle ->
//            val resultOk = bundle.getBoolean(BundleKey.RESULT_OK)
//            if (resultOk) {
//                val paymentID = bundle.getInt(BundleKey.PAYMENT_METHOD_ID)
//                val inviteID = bundle.getInt(BundleKey.INVITE_ID)
//                checkPaymentStatus(paymentID, inviteID)
//
//            } else {
//                var reason = bundle.getString(BundleKey.FAIL_REASON)
//                if (reason == null) {
//                    reason = "unknown error"
//                }
//                Snackbar.make(
//                    binding.inviteListContainer,
//                    reason,
//                    Snackbar.LENGTH_SHORT).show()
//            }
//        }
    }

//    private fun checkPaymentStatus(paymentId: Int, inviteId: Int) {
//        if(paymentId > 0) {
//            val call = ApiClient.paymentService.getPaymentById(paymentId)
//            call.enqueue {
//                onResponse = {
////                    if(it.isSuccessful &&
////                        it.body() != null &&
////                        it.body().status == PaymentStatus.EXTERNAL_APPROVAL_SUCCESS) {
////                        updateInvite(inviteId, paymentId, InvitationAction.ACCEPT)
////                    } else {
////                        Snackbar.make(
////                            binding.inviteListContainer,
////                            "Payment method successfully added",
////                            Snackbar.LENGTH_SHORT).show()
////                    }
//                }
//                onFailure = {
//                    Log.e("SafeListFragment", it?.stackTrace.toString())
//
//                }
//            }
//        } else {
//            Snackbar.make(
//                binding.inviteListContainer,
//                "Invalid Payment selected",
//                Snackbar.LENGTH_SHORT).show()
//        }
//    }

    private fun getInvites() {
        val call = ApiClient.invitationService.getInvitations()
        call.enqueue {
            onResponse = {
                if (it.isSuccessful && it.body() != null) {
                    if(it.body().results.isNotEmpty()) {
                        binding.inviteListNoDataText.visibility = View.GONE
                        binding.inviteListNoDataImage.visibility = View.GONE
                        binding.inviteList.visibility = View.VISIBLE
                    }
                    adapter?.addItems(it.body().results.sortedByDescending { sit -> sit.status })
                }
            }
            onFailure = {
                Log.e(TAG, it?.stackTrace.toString())
                Snackbar.make(binding.inviteListContainer, "failed...", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun inviteAccepted(invite: InviteDetailResp) {
        AlertDialog.Builder(ctx)
            .setTitle("some title")
            .setMessage("Are you sure you want to accept this invitation?")
            .setPositiveButton("Yes") { dialog, _ ->
                selectedInviteId = invite.id
                findNavController().navigate(R.id.action_nav_invitation_to_nav_payment_method_select)
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    override fun inviteDeclined(invite: InviteDetailResp) {
        AlertDialog.Builder(ctx)
            .setTitle("some title")
            .setMessage("Are you sure you want to reject this invitation?")
            .setPositiveButton("Yes") { dialog, _ ->
                declineInvite(invite.id)
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

//    override fun inviteCancelled(invite: InviteDetailResp) {
//        AlertDialog.Builder(ctx)
//            .setTitle("some title")
//            .setMessage("Are you sure you want to cancel this invitation?")
//            .setPositiveButton("Yes") { dialog, _ ->
//                cancelInvite(invite.id)
//                dialog.dismiss()
//            }
//            .setNegativeButton("No") { dialog, _ ->
//                dialog.dismiss()
//            }.show()
//    }

    private fun acceptInvite(inviteId: Int, paymentId: Int) {
        updateInvite(inviteId, paymentId, InvitationAction.ACCEPT.name)
    }

    private fun declineInvite(inviteId: Int) {
        updateInvite(inviteId, 0, InvitationAction.DECLINE.name)
    }

//    private fun cancelInvite(inviteId: Int) {
//        updateInvite(inviteId, 0, InvitationAction.REMOVE.name)
//    }

    private fun updateInvite(inviteId: Int, paymentId: Int, action: String) {
        val call = ApiClient.invitationService.updateInvitationForAction(
            inviteId,
            InviteActionReq(action, paymentId))
        call.enqueue {
            onResponse = {
                if (it.isSuccessful && it.body() != null) {
                    adapter?.itemStatusChanged(
                        inviteId,
                        InvitationStatus.fromCode(it.body().status))
                    Snackbar.make(binding.inviteListContainer, "Invite updated", Snackbar.LENGTH_SHORT).show()
                }
            }
            onFailure = {
                Log.e(TAG, it?.stackTrace.toString())
                Snackbar.make(binding.inviteListContainer, "failed to update invite", Snackbar.LENGTH_SHORT).show()
            }
        }

    }

    override fun inviteSafeClicked(invite: InviteDetailResp) {
        val bundle = bundleOf(
            BundleKey.SAFE_ID.name to invite.safe.id,
            BundleKey.IS_INVITE_ACCEPTED.name to (invite.status == InvitationStatus.ACCEPTED.code)
        )
        findNavController().navigate(R.id.action_nav_invitation_to_nav_safe_detail, bundle)
    }
    companion object {
        private const val TAG = "InvitationListFragment"
    }
}