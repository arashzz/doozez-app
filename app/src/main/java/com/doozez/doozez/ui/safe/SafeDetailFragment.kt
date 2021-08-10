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
import com.doozez.doozez.api.SharedPrefManager
import com.doozez.doozez.api.enqueue
import com.doozez.doozez.api.payments.PaymentDetailResp
import com.doozez.doozez.api.safe.SafeDetailResp
import com.doozez.doozez.databinding.FragmentSafeDetailBinding
import com.doozez.doozez.ui.safe.adapters.SafeDetailPagerAdapter
import com.doozez.doozez.utils.BundleKey
import com.doozez.doozez.utils.PaymentType
import com.doozez.doozez.utils.SafeStatus
import com.doozez.doozez.utils.SharedPrerfKey
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator

class SafeDetailFragment : Fragment() {
    private var safeId: Int = 0
    private var userId: Int = 0
    private var _binding: FragmentSafeDetailBinding? = null
    private val binding get() = _binding!!
    private var isInitiator = false
    private var viewPagerAdapter: SafeDetailPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            safeId = it.getInt(BundleKey.SAFE_ID)
        }
        userId = SharedPrefManager.getInt(SharedPrerfKey.USER_ID)
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

    private fun populateSafeDetails(detail: SafeDetailResp) {
        isInitiator = detail.initiator == userId
        binding.safeDetailName.text = detail.name
        binding.safeDetailMonthlyPayment.text = detail.monthlyPayment.toString()
        binding.safeDetailStatus.text = SafeStatus.getStatusTextForCode(detail.status!!)
        applyUserRoleRules()
    }

    private fun populateTabs() {
        with(binding.pager) {
            adapter = viewPagerAdapter
            isUserInputEnabled = false
        }

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
            binding.safeDetailLeave.visibility = View.VISIBLE
            binding.safeDetailLeave.setOnClickListener {
                Snackbar.make(binding.safeDetailContainer, "dummy leave", Snackbar.LENGTH_SHORT).show()
            }
        } else {
            binding.safeDetailStart.visibility = View.VISIBLE
            binding.safeDetailAddInvite.visibility = View.VISIBLE
            binding.safeDetailCancel.visibility = View.VISIBLE
            binding.safeDetailCancel.setOnClickListener {
                Snackbar.make(binding.safeDetailContainer, "dummy cancel", Snackbar.LENGTH_SHORT).show()
            }
            binding.safeDetailStart.setOnClickListener {
                Snackbar.make(binding.safeDetailContainer, "dummy start", Snackbar.LENGTH_SHORT).show()
            }

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

    private fun loadPaymentMethodForParticipant(participationId: Int) {
        val pCall = ApiClient.participationService.getParticipationByID(participationId)
        pCall.enqueue {
            onResponse = {
                if (it.isSuccessful && it.body() != null) {
                    populatePaymentMethod(it.body().paymentMethod)
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
        binding.safeDetailPaymentMethodName.isEnabled = true
        binding.safeDetailPaymentMethodName.text = PaymentType.getPaymentName(PaymentType.DIRECT_DEBIT)
        binding.safeDetailPaymentMethodContainer.setOnClickListener {
            Snackbar.make(binding.safeDetailContainer, "change payment method", Snackbar.LENGTH_SHORT).show()
        }
    }
}