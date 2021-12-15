package com.doozez.doozez.ui.safe

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.doozez.doozez.R
import com.doozez.doozez.api.ApiClient
import com.doozez.doozez.api.SharedPrefManager
import com.doozez.doozez.api.enqueue
import com.doozez.doozez.api.invitation.InviteActionReq
import com.doozez.doozez.api.invitation.InviteDetailResp
import com.doozez.doozez.api.participation.ParticipationActionReq
import com.doozez.doozez.api.participation.ParticipationResp
import com.doozez.doozez.api.safe.SafeActionReq
import com.doozez.doozez.api.safe.SafeDetailResp
import com.doozez.doozez.databinding.FragmentSafeDetailBinding
import com.doozez.doozez.enums.*
import com.doozez.doozez.ui.safe.adapters.SafeDetailInviteListAdapter
import com.doozez.doozez.ui.safe.adapters.SafeDetailParticipantListAdapter
import com.doozez.doozez.ui.safe.listeners.SafeInviteeListener
import com.doozez.doozez.utils.*
import com.google.android.material.snackbar.Snackbar

class SafeDetailFragment : Fragment(), SafeInviteeListener {
    private var userId: Int = 0
    private var participationId: Int = 0
    private var _binding: FragmentSafeDetailBinding? = null
    private val binding get() = _binding!!
    private var isInitiator = false
    private val inviteAdapter = SafeDetailInviteListAdapter(mutableListOf(), this)
    private val participantsAdapter = SafeDetailParticipantListAdapter(mutableListOf())
    private var ctx: Context? = null
    private var menu: Menu? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ctx = context
    }

    override fun onDetach() {
        super.onDetach()
        ctx = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        var safeId = 0
        arguments?.let {
            safeId = it.getInt(BundleKey.SAFE_ID.name)
        }
        userId = SharedPrefManager.getInt(SharedPrerfKey.USER_ID.name)
        setHasOptionsMenu(true)
        _binding = FragmentSafeDetailBinding.inflate(inflater, container, false)
        getSafeDetails(safeId)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        this.menu = menu
        inflater.inflate(R.menu.safe_detail_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var safeId = 0
        arguments?.let {
            safeId = it.getInt(BundleKey.SAFE_ID.name)
        }
        return when (item.itemId) {
            R.id.action_safe_history -> {
                findNavController().navigate(R.id.action_nav_safe_detail_to_nav_safe_history, bundleOf(
                    BundleKey.SAFE_ID.name to safeId
                ))
                true
            }
            R.id.action_safe_cancel -> {
                AlertDialog.Builder(ctx)
                    .setTitle("some title")
                    .setMessage("Are you sure you want to cancel this safe?")
                    .setPositiveButton("Yes") { dialog, _ ->
                        cancelSafe()
                        dialog.dismiss()
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }.show()
                true
            }
            R.id.action_safe_leave -> {
                AlertDialog.Builder(ctx)
                    .setTitle("some title")
                    .setMessage("Are you sure you want to leave this safe?")
                    .setPositiveButton("Yes") { dialog, _ ->
                        leaveSafe()
                        dialog.dismiss()
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }.show()
                true
            } else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getSafeDetails(safeId: Int) {
        val call = ApiClient.safeService.getSafeByIdForUser(safeId)
        call.enqueue {
            onResponse = {
                if (it.isSuccessful && it.body() != null) {
                    val safe = it.body()
                    updateSafeDetails(safe)
                    initAdapters(safe)
                    populateList(safe)
                } else {
                    Snackbar.make(
                        binding.safeDetailContainer,
                        "Failed get safe details",
                        Snackbar.LENGTH_SHORT).show()
                }
            }
            onFailure = {
                Log.e(TAG, it?.stackTrace.toString())
                Snackbar.make(binding.safeDetailContainer, "failed...", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateSafeDetails(safe: SafeDetailResp) {
        isInitiator = safe.initiator == userId
        binding.safeDetailName.text = safe.name
        binding.safeDetailMonthlyPayment.text = safe.monthlyPayment.toString()
        val status = SafeStatus.fromCode(safe.status)
        binding.safeDetailStatus.text = status.description
        binding.safeDetailStatus.setOnClickListener {
            findNavController().navigate(R.id.action_nav_safe_to_nav_task_list, bundleOf(
                BundleKey.SAFE_ID.name to safe.id
            ))
        }
        addGeneralListeners()
        applyUserRoleRules(safe)
    }

    private fun populateList(safe: SafeDetailResp) {
        val status = SafeStatus.fromCode(safe.status)
        if(status == SafeStatus.PENDING_PARTICIPANTS) {
            loadInvites(safe)
        }
        getParticipationDetails(safe)
    }

    private fun loadInvites(safe: SafeDetailResp) {
        val call = ApiClient.invitationService.getInvitationsForSafe(safe.id)
        call.enqueue {
            onResponse = { resp ->
                if (resp.isSuccessful && resp.body() != null) {
                    val results = resp.body().results
                    inviteAdapter.isInitiator = isInitiator
                    inviteAdapter.addItems(results)
                    inviteAdapter.notifyDataSetChanged()
                    val isInviteAccepted = results.filter {
                        it.recipient.id == userId && it.status == InvitationStatus.ACCEPTED.code
                    }.size == 1
                    if(isInviteAccepted) {
                        //TODO:do we need this?
                    }
                }
            }
            onFailure = {
                Log.e(TAG, it?.stackTrace.toString())
                Snackbar.make(
                    binding.safeDetailUserList,
                    "Failed to load Invitation list",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun getParticipationDetails(safe: SafeDetailResp) {
        val status = SafeStatus.fromCode(safe.status)
        val call = ApiClient.participationService.getParticipationsForSafe(safe.id)
        call.enqueue {
            onResponse = {
                if (it.isSuccessful && it.body() != null) {
                    val participationList = it.body().sortedBy { p -> p.winSequence }
                    if(status != SafeStatus.PENDING_PARTICIPANTS) {
                        participantsAdapter.addItems(participationList)
                        participantsAdapter.notifyDataSetChanged()
                    }
                    val participation = participationList.find { x -> x.user.id == userId }
                    if (participation != null) {
                        participationId = participation.id
                        loadPaymentMethodForParticipant()
                    } else {
                        applyNonParticipantRules()
                    }
                }
            }
            onFailure = {
                Log.e(TAG, it?.stackTrace.toString())
                Snackbar.make(
                    binding.safeDetailUserList,
                    "Failed to load Participation list",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun initAdapters(safe: SafeDetailResp) {
        val status = SafeStatus.fromCode(safe.status)
        if(status == SafeStatus.PENDING_PARTICIPANTS) {
            with(binding.safeDetailUserList) {
                layoutManager = LinearLayoutManager(context)
                adapter = this@SafeDetailFragment.inviteAdapter
                addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
            }
        } else {
            with(binding.safeDetailUserList) {
                layoutManager = LinearLayoutManager(context)
                adapter = this@SafeDetailFragment.participantsAdapter
                addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
            }
        }
    }

    private fun addGeneralListeners() {
        setFragmentResultListener(ResultKey.PAYMENT_METHOD_SELECTED.name) { _, bundle ->
            val resultOk = bundle.getBoolean(BundleKey.RESULT_OK.name)
            if (resultOk) {
                val paymentMethodID = bundle.getInt(BundleKey.PAYMENT_METHOD_ID.name)
                if(paymentMethodID > 0) {
                    addPaymentMethodListener(paymentMethodID)
                } else {
                    Snackbar.make(
                        binding.safeDetailContainer,
                        "Selected payment method is invalid",
                        Snackbar.LENGTH_SHORT).show()
                }
            } else {
                var reason = bundle.getString(BundleKey.FAIL_REASON.name)
                if (reason == null) {
                    reason = "unknown error"
                }
                Snackbar.make(
                    binding.safeDetailContainer,
                    reason,
                    Snackbar.LENGTH_SHORT).show()
            }
        }

    }

    private fun addPaymentMethodListener(paymentMethodID: Int) {
        binding.safeDetailPaymentMethod.setOnClickListener {
            findNavController().navigate(R.id.action_nav_safe_detail_to_nav_payment_method_select, bundleOf(
                BundleKey.PAYMENT_METHOD_ID.name to paymentMethodID
            ))
        }
    }

    private fun applyNonParticipantRules() {
        val menuItemLeave = this.menu!!.findItem(R.id.action_safe_leave)
        menuItemLeave.isVisible = false
        binding.safeDetailPaymentMethod.visibility = View.GONE
    }

    private fun applyUserRoleRules(safe: SafeDetailResp) {
        val status = SafeStatus.fromCode(safe.status)
        val menuItemLeave = this.menu!!.findItem(R.id.action_safe_leave)
        val menuItemCancel = this.menu!!.findItem(R.id.action_safe_cancel)

        binding.safeDetailPaymentMethod.visibility = View.VISIBLE
        if (!isInitiator && status == SafeStatus.PENDING_PARTICIPANTS) {
            menuItemLeave.isVisible = true
        } else if (status == SafeStatus.PENDING_PARTICIPANTS) {
            binding.safeDetailStart.visibility = View.VISIBLE
            binding.safeDetailAddInvite.visibility = View.VISIBLE
            menuItemCancel.isVisible = true
            binding.safeDetailStart.setOnClickListener {
                AlertDialog.Builder(ctx)
                    .setTitle("some title")
                    .setMessage("Do you want to start this safe?")
                    .setPositiveButton("Yes") { dialog, _ ->
                        startSafe(safe)
                        dialog.dismiss()
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }.show()
            }

            binding.safeDetailAddInvite.setOnClickListener {
                findNavController().navigate(
                    R.id.action_nav_safe_detail_to_nav_user_search, bundleOf(
                        BundleKey.SAFE_ID.name to safe.id
                    )
                )
            }
        }
    }

    private fun loadPaymentMethodForParticipant() {
        val pCall = ApiClient.participationService.getParticipationByID(participationId)
        pCall.enqueue {
            onResponse = {
                if (it.isSuccessful && it.body() != null) {
                    addPaymentMethodListener(it.body().paymentMethod.id)
                } else {
                    Snackbar.make(
                        binding.safeDetailContainer,
                        "Failed to get payment method for user",
                        Snackbar.LENGTH_SHORT).show()
                }
            }
            onFailure = {
                Log.e(TAG, it?.stackTrace.toString())
                Snackbar.make(
                    binding.safeDetailContainer,
                    "Failed to get payment method for user",
                    Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun leaveSafe() {
        val call = ApiClient.participationService.updateParticipationForAction(participationId, ParticipationActionReq(
            ParticipationAction.LEAVE.name))
        call.enqueue {
            onResponse = {
                if (it.isSuccessful) {
                    findNavController().navigate(SafeDetailFragmentDirections.actionNavSafeDetailToNavHome())
                } else {
                    Snackbar.make(binding.safeDetailContainer, "Failed to leave Safe", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun startSafe(safe: SafeDetailResp) {
        val body = SafeActionReq(SafeAction.START.name, true)
        val call = ApiClient.safeService.updateSafeForAction(safe.id, body)
        call.enqueue {
            onResponse = {
                if (it.isSuccessful && it.body() != null) {
                    updateSafeDetails(it.body())
                    populateList(it.body())
                    Snackbar.make(
                        binding.safeDetailContainer,
                        "Safe started successfully",
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else {
                    Snackbar.make(
                        binding.safeDetailContainer,
                        "Failed to start safe",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
            onFailure = {
                Log.e(TAG, it?.stackTrace.toString())
                Snackbar.make(
                    binding.safeDetailContainer,
                    "Failed start safe",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun cancelSafe() {
        Snackbar.make(
            binding.safeDetailContainer,
            "dummy cancel",
            Snackbar.LENGTH_SHORT
        ).show()
    }

    override fun inviteeRemoved(invitation: InviteDetailResp) {
        AlertDialog.Builder(ctx)
            .setTitle("some title")
            .setMessage("Are you sure you want to remove this invitee?")
            .setPositiveButton("Yes") { dialog, _ ->
                removeInvitee(invitation)
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private fun removeInvitee(invitation: InviteDetailResp) {
        val body = InviteActionReq(InvitationAction.REMOVE.name, 0)
        val call = ApiClient.invitationService.updateInvitationForAction(invitation.id, body)
        call.enqueue {
            onResponse = {
                if(it.isSuccessful) {
                    with(inviteAdapter) {
                        removeItem(invitation)
                        notifyDataSetChanged()
                    }
                } else {
                    Log.e(SafeInvitationsTabFragment.TAG, it.message())
                    Snackbar.make(
                        binding.safeDetailContainer,
                        "Failed to remove invitee from safe",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
            onFailure = {
                Log.e(SafeInvitationsTabFragment.TAG, it?.stackTrace.toString())
                Snackbar.make(
                    binding.safeDetailContainer,
                    "Failed to load Invitation list",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    companion object {
        private const val TAG = "SafeDetailFragment"
    }
}