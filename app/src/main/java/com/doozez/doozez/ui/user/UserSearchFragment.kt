package com.doozez.doozez.ui.user

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.doozez.doozez.api.ApiClient
import com.doozez.doozez.api.enqueue
import com.doozez.doozez.api.invitation.InvitationCreateRequest
import com.doozez.doozez.api.user.UserDetailResponse
import com.doozez.doozez.databinding.FragmentUserSearchBinding
import com.doozez.doozez.ui.user.adapters.UserSearchAdapter
import com.doozez.doozez.ui.user.listeners.OnUserSearchItemClickListener
import com.doozez.doozez.utils.BundleKey
import com.google.android.material.snackbar.Snackbar

class UserSearchFragment : Fragment(), OnUserSearchItemClickListener {
    private var safeId: Long = 0
    private var _binding: FragmentUserSearchBinding? = null
    private val binding get() = _binding!!
    private val adapter = UserSearchAdapter(mutableListOf(), this)
    private val searchThreshold = 3
    //private val invitedUsers: MutableList<Long> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            safeId = it.getLong(BundleKey.SAFE_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserSearchBinding.inflate(inflater, container, false)
        with(binding.userSearchList) {
            layoutManager = LinearLayoutManager(context)
            adapter = this@UserSearchFragment.adapter
        }
        addListeners()
        return  binding.root
    }

    private fun addListeners() {
        binding.userSearchEmail.addTextChangedListener(object : TextWatcher {
            var lastChange: Long = 0
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if (s.length > 3) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        if (System.currentTimeMillis() - lastChange >= 300) {
                            searchAndLoadUsers(s.toString())
                        }
                    }, 300)
                    lastChange = System.currentTimeMillis()
                }
            }
        })
    }

    private fun searchAndLoadUsers(query: String) {
        val call = ApiClient.userService.searchUsers(query)
        call.enqueue {
            onResponse = {
                if (it.isSuccessful && it.body() != null) {
                    adapter.addItems(it.body())
                    adapter.notifyDataSetChanged()
                }
            }
            onFailure = {
                Log.e("UserSearchFragment", it?.stackTrace.toString())
                Snackbar.make(binding.safeListUseSearchContainer, "failed...", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun addInvite(user: UserDetailResponse) {
        var body = InvitationCreateRequest()
        body.recipientId = user.id
        body.safeId = safeId
        val call = ApiClient.invitationService.createInvitation(body)
        call.enqueue {
            onResponse = {
                var msg = "Successfully invited ${user.firstName}"
                if (!it.isSuccessful) {
                    msg = "Failed to invite ${user.firstName}"
                    if (it.errorBody() != null) {
                        Log.e("SafeDetailFragment-add-invite", it.errorBody().string())
                    }
                }
                Snackbar.make(
                    binding.safeListUseSearchContainer,
                    msg, Snackbar.LENGTH_SHORT
                ).show()
            }
            onFailure = {
                Log.e("SafeDetailFragment-add-invite", it?.stackTrace.toString())
                Snackbar.make(
                    binding.safeListUseSearchContainer,
                    "Failed to invite ${user.firstName}", Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun userItemClicked(user: UserDetailResponse) {
        addInvite(user)
    }
}