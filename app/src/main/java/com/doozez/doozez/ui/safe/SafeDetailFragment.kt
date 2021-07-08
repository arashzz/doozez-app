package com.doozez.doozez.ui.safe

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.doozez.doozez.R
import com.doozez.doozez.api.ApiClient
import com.doozez.doozez.api.enqueue
import com.doozez.doozez.api.payments.PaymentDetailResp
import com.doozez.doozez.api.safe.SafeDetailResponse
import com.doozez.doozez.databinding.FragmentSafeDetailBinding
import com.doozez.doozez.ui.safe.adapters.SafeDetailPagerAdapter
import com.doozez.doozez.utils.BundleKey
import com.doozez.doozez.utils.SafeStatus
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator

class SafeDetailFragment : Fragment() {
    private var safeId: Long = 0
    private var userId: Long = 0
    private var _binding: FragmentSafeDetailBinding? = null
    private val binding get() = _binding!!
    private var isInitiator = false
    private var viewPagerAdapter: SafeDetailPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            safeId = it.getLong(BundleKey.SAFE_ID)
            userId = it.getLong(BundleKey.USER_ID)
        }
        viewPagerAdapter = SafeDetailPagerAdapter(this, safeId, userId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        _binding = FragmentSafeDetailBinding.inflate(inflater, container, false)
        val view = binding.root
        populateTabs()
        getSafeDetails()
        getPaymentMethod()
        return view
    }

    private fun getSafeDetails() {
        val call = ApiClient.safeService.getSafeByIdForUser(safeId)
        call.enqueue {
            onResponse = {
                if (it.isSuccessful && it.body() != null) {
                    populateSafeDetails(it.body())
                } else {
                    Snackbar.make(
                        binding.safeDetailContainer,
                        "Failed get safe details",
                        Snackbar.LENGTH_SHORT).show()
                }
            }
            onFailure = {
                Log.e("SafeDetailFragment", it?.stackTrace.toString())
                Snackbar.make(binding.safeDetailContainer, "failed...", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun populateSafeDetails(detail: SafeDetailResponse) {
        isInitiator = detail.initiator == userId
        binding.safeDetailName.text = detail.name
        binding.safeDetailMonthlyPayment.text = detail.monthlyPayment.toString()
        binding.safeDetailStatus.text = SafeStatus.getStatusTextForCode(detail.status!!)
        applyUserRoleRules()
    }

    private fun populateTabs() {
        binding.pager.adapter = viewPagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            var tabName = ""
            when(position) {
                //SafeDetailPagerAdapter.TAB_DETAILS -> tabName = SafeDetailPagerAdapter.TAB_DETAILS_NAME
                SafeDetailPagerAdapter.TAB_INVITATIONS -> tabName = SafeDetailPagerAdapter.TAB_INVITATIONS_NAME
                SafeDetailPagerAdapter.TAB_PARTICIPANTS -> tabName = SafeDetailPagerAdapter.TAB_PARTICIPANTS_NAME
            }
            tab.text = tabName
        }.attach()
    }

    private fun applyUserRoleRules() {
        if(!isInitiator) {
            (binding.safeDetailAddInvite.parent as? ViewGroup)?.removeView(binding.safeDetailAddInvite)
        } else {
            binding.safeDetailAddInvite.setOnClickListener {
                val navController = findNavController()
                navController.navigate(
                    R.id.action_nav_safe_to_nav_user_search, bundleOf(
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
                    val participation = it.body().find { it.user?.id == userId }
                    if (participation != null) {
                        loadPaymentMethodForParticipant(participation.id!!)
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
                Log.e("SafeDetailFragment", it?.stackTrace.toString())
                Snackbar.make(
                    binding.safeDetailContainer,
                    "Failed get to participants for safe",
                    Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadPaymentMethodForParticipant(participationId: Long) {
        val pCall = ApiClient.participationService.getParticipationByID(participationId)
        pCall.enqueue {
            onResponse = {
                if (it.isSuccessful && it.body() != null && it.body().paymentMethod != null) {
                    populatePaymentMethod(it.body().paymentMethod!!)
                } else {
                    Snackbar.make(
                        binding.safeDetailContainer,
                        "Failed get to payment method for user",
                        Snackbar.LENGTH_SHORT).show()
                }
            }
            onFailure = {
                Log.e("SafeDetailFragment", it?.stackTrace.toString())
                Snackbar.make(
                    binding.safeDetailContainer,
                    "Failed get to participation for user",
                    Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun populatePaymentMethod(item: PaymentDetailResp) {
        binding.safeDetailPaymentMethodContainer.visibility = View.VISIBLE
        binding.safeDetailPaymentMethodNumber.isEnabled = true
        binding.safeDetailPaymentMethodEdit.isEnabled = true
        binding.safeDetailPaymentMethodNumber.text = item.cardNumber
    }
}