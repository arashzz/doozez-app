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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            safeId = it.getLong(BundleKey.SAFE_ID)
            userId = it.getLong(BundleKey.USER_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        _binding = FragmentSafeDetailBinding.inflate(inflater, container, false)
        val view = binding.root
        populateTabs()
        getSafeDetails()
        return view
    }

    private fun addListeners(isInitiator: Boolean) {
        if (isInitiator) {
            binding.safeDetailAddInvite.setOnClickListener {
                val navController = findNavController()
                navController.navigate(
                    R.id.action_nav_safe_to_nav_user_search, bundleOf(
                        BundleKey.SAFE_ID to safeId
                    )
                )
            }
        }
//        setFragmentResultListener(ResultKey.SEARCHED_USER_SELECTED) { _, bundle ->
//            var user = bundle.getParcelable<UserDetailResponse>(BundleKey.MODEL_OBJECT)
//            addInvite(user?.id!!, user?.firstName!!)
//        }
    }

//    private fun addInvite(recipientId: Long, recipientFirstName: String) {
//        var body = InvitationCreateRequest()
//        body.recipientId = recipientId
//        body.safeId = safeId
//        val call = ApiClient.invitationService.createInvitation(body)
//        call.enqueue {
//            onResponse = {
//                Snackbar.make(binding.safeDetailContainer,
//                    "Successfully invited $recipientFirstName", Snackbar.LENGTH_SHORT).show()
//            }
//            onFailure = {
//                Log.e("SafeDetailFragment-add-invite", it?.stackTrace.toString())
//                Snackbar.make(binding.safeDetailContainer,
//                    "Failed to invite $recipientFirstName", Snackbar.LENGTH_SHORT).show()
//            }
//        }
//    }

    private fun getSafeDetails() {
        val call = ApiClient.safeService.getSafeByIdForUser(safeId)
        call.enqueue {
            onResponse = {
                if (it.isSuccessful && it.body() != null) {
                    populateSafeDetails(it.body())
                }
            }
            onFailure = {
                Log.e("SafeDetailFragment", it?.stackTrace.toString())
                Snackbar.make(binding.safeDetailContainer, "failed...", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun populateSafeDetails(detail: SafeDetailResponse) {
        binding.safeDetailName.text = detail.name
        binding.safeDetailMonthlyPayment.text = detail.monthlyPayment.toString()
        binding.safeDetailStatus.text = SafeStatus.getStatusTextForCode(detail.status!!)
        val isInitiator = detail.initiator != userId
        if(!isInitiator) {
            (binding.safeDetailAddInvite.parent as? ViewGroup)?.removeView(binding.safeDetailAddInvite)
        }
        addListeners(isInitiator)
    }

    private fun populateTabs() {
        binding.pager.adapter = SafeDetailPagerAdapter(this, safeId)
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            var tabName = ""
            when(position) {
                SafeDetailPagerAdapter.TAB_DETAILS -> tabName = SafeDetailPagerAdapter.TAB_DETAILS_NAME
                SafeDetailPagerAdapter.TAB_INVITATIONS -> tabName = SafeDetailPagerAdapter.TAB_INVITATIONS_NAME
                SafeDetailPagerAdapter.TAB_PARTICIPANTS -> tabName = SafeDetailPagerAdapter.TAB_PARTICIPANTS_NAME
            }
            tab.text = tabName
        }.attach()
    }
}