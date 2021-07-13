package com.doozez.doozez.ui.safe.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.doozez.doozez.ui.safe.SafeInvitationsTabFragment
import com.doozez.doozez.ui.safe.SafeParticipantsTabFragment

class SafeDetailPagerAdapter(fragment: Fragment,
        var safeId: Int, var userId: Int)
    : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
//            TAB_DETAILS -> {
//                fragment = SafeDetailsTabFragment.newInstance(safeId, userId)
//            }
            TAB_INVITATIONS -> {
                fragment = SafeInvitationsTabFragment.newInstance(safeId)
            }
            TAB_PARTICIPANTS -> {
                fragment = SafeParticipantsTabFragment.newInstance(safeId)
            }
        }
        return fragment!!
    }

    companion object {
        const val TAB_DETAILS = 0
        const val TAB_INVITATIONS = 1
        const val TAB_PARTICIPANTS = 0
        const val TAB_DETAILS_NAME = "Details"
        const val TAB_INVITATIONS_NAME = "Invitations"
        const val TAB_PARTICIPANTS_NAME = "Participants"
    }
}