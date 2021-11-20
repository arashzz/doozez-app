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
import com.doozez.doozez.api.safe.SafeActionReq
import com.doozez.doozez.api.safe.SafeDetailResp
import com.doozez.doozez.databinding.FragmentSafeDetailBinding
import com.doozez.doozez.ui.safe.adapters.SafeDetailInviteListAdapter
import com.doozez.doozez.ui.safe.adapters.SafeDetailParticipantListAdapter
import com.doozez.doozez.ui.safe.listeners.SafeInviteeListener
import com.doozez.doozez.utils.*
import com.google.android.material.snackbar.Snackbar

class SafeDetailFragment : Fragment(), SafeInviteeListener {
    private var safeId: Int = 0
    private var safe: SafeDetailResp? = null
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            safeId = it.getInt(BundleKey.SAFE_ID)
        }
        userId = SharedPrefManager.getInt(SharedPrerfKey.USER_ID)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        _binding = FragmentSafeDetailBinding.inflate(inflater, container, false)
        getSafeDetails()
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        this.menu = menu
        inflater.inflate(R.menu.safe_detail_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_safe_history -> {
                findNavController().navigate(R.id.action_nav_safe_detail_to_nav_safe_history, bundleOf(
                    BundleKey.SAFE_ID to safeId
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

    private fun getSafeDetails() {
        val call = ApiClient.safeService.getSafeByIdForUser(safeId)
        call.enqueue {
            onResponse = {
                if (it.isSuccessful && it.body() != null) {
                    safe = it.body()
                    updateSafeDetails(it.body())
                    populateList(it.body())
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
        binding.safeDetailStatus.text = SafeStatus.getStatusForCode(safe.status).description
        binding.safeDetailStatus.setOnClickListener {
            findNavController().navigate(R.id.action_nav_safe_to_nav_task_list, bundleOf(
                BundleKey.SAFE_ID to safeId
            ))
        }
        addGeneralListeners()
        applyUserRoleRules()
    }

    private fun populateList(safe: SafeDetailResp) {
        if(safe.status == SafeStatus.PENDING_PARTICIPANTS.code) {
            populateInviteList(safe.id)
        } else {
            populateParticipantList(safe.id)
        }
    }

    private fun populateInviteList(safeId: Int) {
        with(binding.safeDetailUserList) {
            layoutManager = LinearLayoutManager(context)
            adapter = this@SafeDetailFragment.inviteAdapter
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }
//        val covertConfig = Covert.Config(
//            iconRes = R.drawable.ic_star_border_black_24dp, // The icon to show
//            iconDefaultColorRes = R.color.black,            // The color of the icon
//            actionColorRes = R.color.colorPrimary           // The color of the background
//        )
        loadInvites(safeId)
    }

    private fun populateParticipantList(safeId: Int) {
        with(binding.safeDetailUserList) {
            layoutManager = LinearLayoutManager(context)
            adapter = this@SafeDetailFragment.participantsAdapter
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }
        loadParticipants(safeId)
    }

    private fun loadInvites(safeId: Int) {
        val call = ApiClient.invitationService.getInvitationsForSafe(safeId)
        call.enqueue {
            onResponse = { resp ->
                if (resp.isSuccessful && resp.body() != null) {
                    val results = resp.body().results
                    inviteAdapter.addItems(results)
                    inviteAdapter.notifyDataSetChanged()
                    val isInviteAccepted = results.filter {
                        it.recipient.id == userId && it.status == InvitationStatus.ACCEPTED.code
                    }.size == 1
                    if(isInviteAccepted) {
                        getPaymentMethod()
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

    private fun loadParticipants(safeId: Int) {
        val call = ApiClient.participationService.getParticipationsForSafe(safeId)
        call.enqueue {
            onResponse = {
                if (it.isSuccessful && it.body() != null) {
                    participantsAdapter.addItems(it.body())
                    participantsAdapter.notifyDataSetChanged()
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


//    private fun populateTabs() {
//        with(binding.pager) {
//            adapter = viewPagerAdapter
//            isUserInputEnabled = false
//        }
//
//        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
//            var tabName = ""
//            when(position) {
//                //SafeDetailPagerAdapter.TAB_DETAILS -> tabName = SafeDetailPagerAdapter.TAB_DETAILS_NAME
//                SafeDetailPagerAdapter.TAB_INVITATIONS -> tabName = SafeDetailPagerAdapter.TAB_INVITATIONS_NAME
//                SafeDetailPagerAdapter.TAB_PARTICIPANTS -> tabName = SafeDetailPagerAdapter.TAB_PARTICIPANTS_NAME
//            }
//            tab.text = tabName
//        }.attach()
//    }


    private fun addGeneralListeners() {
        setFragmentResultListener(ResultKey.PAYMENT_METHOD_SELECTED) { _, bundle ->
            val resultOk = bundle.getBoolean(BundleKey.RESULT_OK)
            if (resultOk) {
                val paymentMethodID = bundle.getInt(BundleKey.PAYMENT_METHOD_ID)
//                val paymentMethodName = bundle.getString(BundleKey.PAYMENT_METHOD_NAME)
//                if (!paymentMethodName.isNullOrBlank()) {
//                    binding.safeDetailPaymentMethodName.text = paymentMethodName
//                }
                if(paymentMethodID > 0) {
                    updatePaymentMethod(paymentMethodID)
                } else {
                    Snackbar.make(
                        binding.safeDetailContainer,
                        "Selected payment method is invalid",
                        Snackbar.LENGTH_SHORT).show()
                }
            } else {
                var reason = bundle.getString(BundleKey.FAIL_REASON)
                if (reason == null) {
                    reason = "unknown error"
                }
                Snackbar.make(
                    binding.safeDetailContainer,
                    reason,
                    Snackbar.LENGTH_SHORT).show()
            }
        }
        binding.safeDetailPaymentMethod.setOnClickListener {
            findNavController().navigate(R.id.action_nav_safe_detail_to_nav_payment_method_list)
        }
    }

    private fun applyUserRoleRules() {
        val menuItemLeave = this.menu!!.findItem(R.id.action_safe_leave)
        val menuItemCancel = this.menu!!.findItem(R.id.action_safe_cancel)

        binding.safeDetailPaymentMethod.visibility = View.VISIBLE
        if (!isInitiator && safe!!.status == SafeStatus.PENDING_PARTICIPANTS.code) {
            menuItemLeave.isVisible = true
        } else if (safe!!.status == SafeStatus.PENDING_PARTICIPANTS.code) {
            binding.safeDetailStart.visibility = View.VISIBLE
            binding.safeDetailAddInvite.visibility = View.VISIBLE
            menuItemCancel.isVisible = true
            binding.safeDetailStart.setOnClickListener {
                AlertDialog.Builder(ctx)
                    .setTitle("some title")
                    .setMessage("Do you want to start this safe?")
                    .setPositiveButton("Yes") { dialog, _ ->
                        startSafe()
                        dialog.dismiss()
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }.show()
            }

            binding.safeDetailAddInvite.setOnClickListener {
                findNavController().navigate(
                    R.id.action_nav_safe_detail_to_nav_user_search, bundleOf(
                        BundleKey.SAFE_ID to safeId
                    )
                )
            }
        }

    }

    private fun getPaymentMethod() {
        val call = ApiClient.participationService.getParticipationsForSafe(safeId)
        call.enqueue {
            onResponse = { it ->
                if (it.isSuccessful && it.body() != null) {
                    val participation = it.body().find { it.user.id == userId }
                    if (participation != null) {
                        participationId = participation.id
                        //binding.safeDetailLeave.isEnabled = true
                        //loadPaymentMethodForParticipant(participation.id)
                    } else {
                        Snackbar.make(
                            binding.safeDetailContainer,
                            "Failed get to participants for safe",
                            Snackbar.LENGTH_SHORT).show()
                    }
                } else {
                    Snackbar.make(
                        binding.safeDetailContainer,
                        "Failed get to participants for safe",
                        Snackbar.LENGTH_SHORT).show()
                }
            }
            onFailure = {
                Log.e(TAG, it?.stackTrace.toString())
                Snackbar.make(
                    binding.safeDetailContainer,
                    "Failed get to participants for safe",
                    Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun updatePaymentMethod(paymentMethodId: Int) {
        Snackbar.make(
            binding.safeDetailContainer,
            "Dummy participant update",
            Snackbar.LENGTH_SHORT).show()
    }

//    private fun loadPaymentMethodForParticipant(participationId: Int) {
//        val pCall = ApiClient.participationService.getParticipationByID(participationId)
//        pCall.enqueue {
//            onResponse = {
//                if (it.isSuccessful && it.body() != null) {
//                    populatePaymentMethod(it.body().paymentMethod)
//                } else {
//                    Snackbar.make(
//                        binding.safeDetailContainer,
//                        "Failed to get payment method for user",
//                        Snackbar.LENGTH_SHORT).show()
//                }
//            }
//            onFailure = {
//                Log.e(TAG, it?.stackTrace.toString())
//                Snackbar.make(
//                    binding.safeDetailContainer,
//                    "Failed to get payment method for user",
//                    Snackbar.LENGTH_SHORT).show()
//            }
//        }
//    }

//    private fun populatePaymentMethod(item: PaymentMethodDetailResp) {
//        binding.safeDetailPaymentMethodContainer.visibility = View.VISIBLE
//        binding.safeDetailPaymentMethodName.isEnabled = true
//        binding.safeDetailPaymentMethodName.text = item.name
//    }

    private fun leaveSafe() {
        val call = ApiClient.participationService.updateParticipationForAction(participationId, ParticipationActionReq(ParticipationAction.LEAVE))
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

    private fun startSafe() {
        val body = SafeActionReq(SafeAction.START.name, true)
        val call = ApiClient.safeService.updateSafeForAction(safeId, body)
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
        val body = InviteActionReq(InvitationAction.REMOVE, 0)
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

//    private fun triggerOverlay() {
//        var visibility = View.GONE
//        if (binding.overlayLoader.progressView.visibility != View.VISIBLE) {
//            visibility = View.VISIBLE
//        }
//        binding.overlayLoader.progressView.visibility = visibility
//    }

    companion object {
        private const val TAG = "SafeDetailFragment"
    }
}