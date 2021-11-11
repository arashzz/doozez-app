package com.doozez.doozez.ui.user

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.doozez.doozez.R
import com.doozez.doozez.api.ApiClient
import com.doozez.doozez.api.SharedPrefManager
import com.doozez.doozez.api.enqueue
import com.doozez.doozez.api.invitation.InviteCreateReq
import com.doozez.doozez.api.user.UserDetailResp
import com.doozez.doozez.databinding.FragmentUserSearchBinding
import com.doozez.doozez.ui.user.adapters.UserSearchAdapter
import com.doozez.doozez.ui.user.listeners.OnUserSearchItemClickListener
import com.doozez.doozez.utils.BundleKey
import com.doozez.doozez.utils.ContextExtensions.hideKeyboard
import com.doozez.doozez.utils.SharedPrerfKey
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.snackbar.Snackbar


class UserSearchFragment : Fragment(), OnUserSearchItemClickListener, SearchView.OnQueryTextListener {
    private var safeId: Int = 0
    private var userId: Int = 0
    private var _binding: FragmentUserSearchBinding? = null
    private val binding get() = _binding!!
    private var ctx: Context? = null
    private val inviteeVM: InviteeListViewModel by navGraphViewModels(R.id.nav_user_search_invitee)

    private var searchView: SearchView? = null

    private val userSearchAdapter = UserSearchAdapter(mutableListOf(), this)
    //private val inviteeAdapter = UserInviteeAdapter(mutableListOf(), this)

    var badgeDrawable: BadgeDrawable? = null


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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.user_search_menu, menu)
        val searchItem = menu.findItem(R.id.action_user_search)
        val searchManager = requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        if(searchItem != null) {
            searchView = searchItem.actionView as SearchView
        }
        searchView?.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        searchView?.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_user_search -> {
                false
            } else -> {
                searchView?.setOnQueryTextListener(this)
                return super.onOptionsItemSelected(item)
            }
        }


    }

    @SuppressLint("UnsafeExperimentalUsageError")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserSearchBinding.inflate(inflater, container, false)
        badgeDrawable = BadgeDrawable.create(this@UserSearchFragment.ctx!!)
        with(binding.userSearchList) {
            layoutManager = LinearLayoutManager(context)
            adapter = this@UserSearchFragment.userSearchAdapter
        }
        setFabDisplayText(0)
        addListeners()
        return  binding.root
    }

    private fun addListeners() {
        binding.inviteesEfab.setOnClickListener {
            hideKeyboard()
            findNavController().navigate(R.id.action_nav_user_search_to_nav_user_invitee_list, bundleOf(
                BundleKey.SAFE_ID to safeId
            ))
        }
        inviteeVM.listLiveData.observe(viewLifecycleOwner, Observer {
            setFabDisplayText(it.size)
        })
    }

    private fun searchAndLoadUsers(query: String) {
        val call = ApiClient.userService.searchUsers(query)
        call.enqueue {
            onResponse = {
                if (it.isSuccessful && it.body() != null) {
                    val users = it.body().toMutableList()
                    users.find { u -> u.id == userId }.apply {
                        if(this != null) {
                            users.remove(this)
                        }
                    }
                    userSearchAdapter.addItems(users)
                    userSearchAdapter.notifyDataSetChanged()
                }
            }
            onFailure = {
                Log.e(TAG, it?.stackTrace.toString())
                Snackbar.make(
                    binding.safeListUseSearchContainer,
                    "failed...",
                    Snackbar.LENGTH_SHORT)
                    .setAction("Dismiss") {}
                    .show()
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
                    removeSelectedUser(user)
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

    override fun userItemClicked(user: UserDetailResp) {
        addSelectedUser(user)
        hideKeyboard()
    }

    private fun addSelectedUser(user: UserDetailResp) {
        val found = inviteeVM.get(user.id)
        if(found == null) {
            inviteeVM.add(user)
            val msg = "${user.firstName} is added to the list of invitee(s)"
            Snackbar.make(
                binding.safeListUseSearchContainer,
                msg,
                Snackbar.LENGTH_SHORT)
                .setAction("Dismiss") {}
                .show()
        } else {
            Snackbar.make(
                binding.safeListUseSearchContainer,
                "This user is already added for invite",
                Snackbar.LENGTH_SHORT)
                .setAction("Dismiss") {}
                .show()
        }
    }

    private fun removeSelectedUser(user: UserDetailResp) {
        inviteeVM.remove(user.id)
    }

    private fun setFabDisplayText(size: Int) {
        val fabText = "$size Invitee(s)"
        binding.inviteesEfab.text = fabText
        binding.inviteesEfab.extend()
    }

    companion object {
        private const val TAG = "UserSearchFragment"
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        var lastChange: Long = 0
        if (!newText.isNullOrBlank() && newText.length > 3) {
            Handler(Looper.getMainLooper()).postDelayed({
                if (System.currentTimeMillis() - lastChange >= 300) {
                    searchAndLoadUsers(newText.toString())
                }
            }, 300)
            lastChange = System.currentTimeMillis()
        }
        return true
    }
}