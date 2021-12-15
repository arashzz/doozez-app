package com.doozez.doozez.ui.payment

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
import com.doozez.doozez.R
import com.doozez.doozez.api.ApiClient
import com.doozez.doozez.api.SharedPrefManager
import com.doozez.doozez.api.enqueue
import com.doozez.doozez.api.invitation.InviteActionReq
import com.doozez.doozez.api.invitation.InviteDetailResp
import com.doozez.doozez.api.payment.PaymentDetailResp
import com.doozez.doozez.databinding.FragmentInvitationListBinding
import com.doozez.doozez.databinding.FragmentPaymentListBinding
import com.doozez.doozez.enums.*
import com.doozez.doozez.ui.invitation.adapters.InvitationListAdapter
import com.doozez.doozez.ui.invitation.listeners.OnInviteActionClickListener
import com.doozez.doozez.ui.payment.adapters.PaymentListAdapter
import com.doozez.doozez.ui.payment.interfaces.PaymentListener
import com.doozez.doozez.utils.*
import com.google.android.material.snackbar.Snackbar

class PaymentListFragment : Fragment(), PaymentListener {
    private var _binding: FragmentPaymentListBinding? = null
    private val binding get() = _binding!!
    private var adapter: PaymentListAdapter? = null
    private var ctx: Context? = null
    private var userId: Int = 0

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
        adapter = PaymentListAdapter(mutableListOf(), this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentListBinding.inflate(inflater, container, false)
        with(binding.paymentList) {
            layoutManager = LinearLayoutManager(context)
            adapter = this@PaymentListFragment.adapter
        }
        getPayments()
        addListeners()
        return binding.root
    }

    private fun addListeners() {

    }

    private fun getPayments() {
        val call = ApiClient.paymentService.getPayments()
        call.enqueue {
            onResponse = {
                if (it.isSuccessful && it.body() != null) {
                    binding.paymentListNoDataText.visibility = View.GONE
                    binding.paymentListNoDataImage.visibility = View.GONE
                    binding.paymentList.visibility = View.VISIBLE
                    adapter?.addItems(it.body().results)
                }
            }
            onFailure = {
                Log.e(TAG, it?.stackTrace.toString())
                Snackbar.make(binding.paymentListContainer, "failed...", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onClickListener(payment: PaymentDetailResp) {
        TODO("Not yet implemented")
        //TODO: Redirect to payment details
    }

    companion object {
        private const val TAG = "PaymentListFragment"
    }
}