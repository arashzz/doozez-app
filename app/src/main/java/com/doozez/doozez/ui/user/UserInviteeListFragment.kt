package com.doozez.doozez.ui.user

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.doozez.doozez.R
import com.doozez.doozez.api.ApiClient
import com.doozez.doozez.api.enqueue
import com.doozez.doozez.api.invitation.InviteCreateReq
import com.doozez.doozez.api.paymentMethod.PaymentMethodDetailResp
import com.doozez.doozez.api.user.UserDetailResp
import com.doozez.doozez.databinding.FragmentPaymentMethodListBinding
import com.doozez.doozez.databinding.FragmentUserInviteeListBinding
import com.doozez.doozez.ui.payment.adapters.PaymentMethodListAdapter
import com.doozez.doozez.ui.payment.listeners.PaymentMethodItemListener
import com.doozez.doozez.ui.user.adapters.UserInviteeAdapter
import com.doozez.doozez.ui.user.listeners.UserInviteeListener
import com.doozez.doozez.utils.BundleKey
import com.doozez.doozez.utils.PaymentMethodStatus
import com.doozez.doozez.utils.ResultKey
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar

class UserInviteeListFragment : BottomSheetDialogFragment(), UserInviteeListener {
    private var _binding: FragmentUserInviteeListBinding? = null
    private val binding get() = _binding!!
    private var adapter: UserInviteeAdapter? = null
    private val inviteeVM: InviteeListViewModel by navGraphViewModels(R.id.nav_user_search_invitee)
    private var safeId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = UserInviteeAdapter(mutableListOf(), this)
        arguments?.let {
            safeId = it.getInt(BundleKey.SAFE_ID)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //TODO: implement for higher versions
        // SOFT_INPUT_ADJUST_RESIZE is deprecated in version 30
        // I'm using it for testing
        when (Build.VERSION.SDK_INT) {
            in 1..30 -> {
                dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            } else -> {
            Log.i("do nothing","do nothing")
            }
        }
        _binding = FragmentUserInviteeListBinding.inflate(inflater, container, false)
        with(binding.inviteeListList) {
            layoutManager = LinearLayoutManager(context)
            adapter = this@UserInviteeListFragment.adapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        addListeners()
        return binding.root
    }

    private fun addListeners() {
        inviteeVM.listLiveData.observe(viewLifecycleOwner, Observer {
            showHideInviteeList(it != null && it.isNotEmpty())
            with(adapter!!) {
                addItems(it)
                notifyDataSetChanged()
            }
        })

        binding.inviteeListCancel.setOnClickListener {
            dismiss()
        }

        binding.inviteeListInvite.setOnClickListener {
            inviteNextInvitee()
        }
    }

    private fun inviteNextInvitee() {
        with(inviteeVM.listLiveData.value!!) {
            if(size > 0) {
                sendInvite(get(0))
            }
        }
    }

    private fun sendInvite(user: UserDetailResp) {
        val body = InviteCreateReq()
        body.recipientId = user.id
        body.safeId = safeId
        val call = ApiClient.invitationService.createInvitation(body)
        call.enqueue {
            onResponse = {
                if (it.isSuccessful) {
                    removeInvitee(user.id)
                    inviteNextInvitee()
                } else {
                    if (it.errorBody() != null) {
                        Log.e(TAG, it.errorBody().string())
                    }
                }
            }
            onFailure = {
                Log.e(TAG, it?.stackTrace.toString())
            }
        }
    }

    private fun showHideInviteeList(show: Boolean) {
        if(show) {
            binding.inviteeListNoDataImage.visibility = View.GONE
            binding.inviteeListNoDataText.visibility = View.GONE
            binding.inviteeListList.visibility = View.VISIBLE
            binding.inviteeListInvite.isEnabled = true
        } else {
            binding.inviteeListNoDataImage.visibility = View.VISIBLE
            binding.inviteeListNoDataText.visibility = View.VISIBLE
            binding.inviteeListList.visibility = View.GONE
            binding.inviteeListInvite.isEnabled = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun inviteeRemoved(user: UserDetailResp) {
        removeInvitee(user.id)
    }

    private fun removeInvitee(inviteeID: Int) {
        inviteeVM.remove(inviteeID)
    }

    companion object {
        private const val TAG = "UserInviteeListFragment"
    }
}