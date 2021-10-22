package com.doozez.doozez.ui.user

import android.R
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
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
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.doozez.doozez.api.ApiClient
import com.doozez.doozez.api.enqueue
import com.doozez.doozez.api.invitation.InviteCreateReq
import com.doozez.doozez.api.user.UserDetailResp
import com.doozez.doozez.databinding.FragmentUserSearchBinding
import com.doozez.doozez.ui.user.adapters.UserSearchAdapter
import com.doozez.doozez.ui.user.listeners.OnUserSearchItemClickListener
import com.doozez.doozez.utils.BundleKey
import com.google.android.material.snackbar.Snackbar
import android.text.style.UnderlineSpan

import android.text.SpannableString
import android.util.TypedValue
import android.view.Gravity
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.transition.TransitionManager
import com.doozez.doozez.utils.ContextExtensions.hideKeyboard
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.card.MaterialCardView
import com.google.android.material.transition.MaterialContainerTransform
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.EditText
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.transition.PathMotion
import com.doozez.doozez.api.SharedPrefManager
import com.doozez.doozez.ui.user.adapters.InviteeAdapter
import com.doozez.doozez.utils.SharedPrerfKey
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.transition.MaterialArcMotion


class UserSearchFragment : Fragment(), OnUserSearchItemClickListener {
    private var safeId: Int = 0
    private var userId: Int = 0
    private var _binding: FragmentUserSearchBinding? = null
    private val binding get() = _binding!!
    private var ctx: Context? = null

    private val userSearchAdapter = UserSearchAdapter(mutableListOf(), this)
    private val inviteeAdapter = InviteeAdapter(mutableListOf(), this)

    private val selectedUsers: MutableList<UserDetailResp> = mutableListOf()
    private val selectedUsersLiveData: MutableLiveData<List<UserDetailResp>> by lazy {
        MutableLiveData<List<UserDetailResp>>()
    }

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
        selectedUsersLiveData.value = selectedUsers
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
        with(binding.userSearchInviteeList) {
            layoutManager = LinearLayoutManager(context)
            adapter = this@UserSearchFragment.inviteeAdapter
            addItemDecoration(DividerItemDecoration(this@UserSearchFragment.ctx!!, LinearLayoutManager.VERTICAL))
        }
        addListeners()
        configBadgeOnFab()
        return  binding.root
    }

    private fun addListeners() {
        binding.fab.setOnClickListener {
            hideKeyboard()
            if(selectedUsers.size > 0) {
                showEndView(true)
                addInviteeListActionListeners()
            } else {
                Snackbar.make(
                    binding.safeListUseSearchContainer,
                    "There is no one added to invite list",
                    Snackbar.LENGTH_SHORT)
                    .setAction("Dismiss") {}
                    .show()
            }

        }
        selectedUsersLiveData.observe(viewLifecycleOwner, Observer {
            badgeDrawable!!.number = selectedUsers.size
            inviteeAdapter.addItems(it)
            inviteeAdapter.notifyDataSetChanged()
            if(selectedUsers.size > 0) {
                adjustInviteeListHeight(selectedUsers.size)
            }
        })

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

    private fun addInviteeListActionListeners() {
        binding.userSearchInviteeCancel.setOnClickListener {
            hideKeyboard()
            showEndView(false)
        }
        binding.userSearchInviteeInvite.setOnClickListener {
            if(selectedUsers.size > 0) {
                selectedUsers.forEach {
                    sendInvite(it)
                }
            } else {
                Snackbar.make(
                    binding.safeListUseSearchContainer,
                    "There is no one to invite",
                    Snackbar.LENGTH_SHORT)
                    .setAction("Dismiss") {}
                    .show()
            }
        }
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

    override fun selectedUserRemoved(user: UserDetailResp) {
        removeSelectedUser(user)
        if(selectedUsers.size == 0) {
            showEndView(false)
        }
    }

    private fun addSelectedUser(user: UserDetailResp) {
        val found = selectedUsers.find { it.id == user.id } != null
        if(!found) {
            selectedUsers.add(user)
            selectedUsersLiveData.value = selectedUsers
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
        selectedUsers.remove(user)
        selectedUsersLiveData.value = selectedUsers
    }

    private fun configBadgeOnFab() {
        badgeDrawable!!.horizontalOffset = 30
        badgeDrawable!!.verticalOffset = 20
        binding.fab.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            @SuppressLint("UnsafeExperimentalUsageError")
            override fun onGlobalLayout() {
                BadgeUtils.attachBadgeDrawable(badgeDrawable!!, binding.fab, null)
                binding.fab.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    private fun adjustInviteeListHeight(rowCount: Int) {
        val rowHeight = 50f
        var height = rowCount * rowHeight
        if(rowCount > 5) height = 5 * rowHeight
        height += rowHeight
        val heightPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, resources.displayMetrics).toInt()
        val marginPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50f, resources.displayMetrics).toInt()
        val params = CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.WRAP_CONTENT, heightPx)
        params.setMargins(marginPx,0, marginPx, marginPx+marginPx)
        params.anchorId = binding.fab.id
        params.anchorGravity = Gravity.START
        binding.endCard.layoutParams = params
    }

    private fun showEndView(open: Boolean) {
        val transform = MaterialContainerTransform().apply {
            if(open) {
                startView = binding.fab
                endView = binding.endCard
                addTarget(endView as MaterialCardView)
            } else {
                startView = binding.endCard
                endView = binding.fab
                addTarget(endView as FloatingActionButton)
            }
            setPathMotion(MaterialArcMotion())
            scrimColor = Color.TRANSPARENT
        }
        TransitionManager.beginDelayedTransition(binding.root, transform)
        if(open) {
            binding.fab.visibility = View.GONE
            binding.endCard.visibility = View.VISIBLE
        } else {
            binding.endCard.visibility = View.GONE
            binding.fab.visibility = View.VISIBLE
        }
    }

    companion object {
        private const val TAG = "UserSearchFragment"
    }
}