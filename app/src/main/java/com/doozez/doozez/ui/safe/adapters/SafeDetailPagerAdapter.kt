package com.doozez.doozez.ui.safe.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.doozez.doozez.ui.safe.SafeInvitationsFragment

class SafeDetailPagerAdapter(fragment: Fragment, var safeId: Long) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            TAB_DETAILS -> {
                fragment = SafeInvitationsFragment()
            }
            TAB_INVITATIONS -> {
                fragment = SafeInvitationsFragment.newInstance(safeId)
            }
            TAB_PARTICIPANTS -> {
                fragment = SafeInvitationsFragment()
            }
        }
//        val fragment = DemoObjectFragment()
//        fragment.arguments = Bundle().apply {
//            // Our object is just an integer :-P
//            putInt(ARG_OBJECT, position + 1)
//        }
        return fragment!!
    }
    companion object {
        const val TAB_DETAILS = 0
        const val TAB_INVITATIONS = 1
        const val TAB_PARTICIPANTS = 2
        const val TAB_DETAILS_NAME = "Details"
        const val TAB_INVITATIONS_NAME = "Invitations"
        const val TAB_PARTICIPANTS_NAME = "Participants"
    }
}