package com.doozez.doozez.ui.user

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.doozez.doozez.R
import com.doozez.doozez.api.ApiClient
import com.doozez.doozez.api.safe.SafeDetailResponse
import com.doozez.doozez.api.user.UserItemResponse
import com.doozez.doozez.databinding.FragmentSafeDetailBinding
import com.doozez.doozez.databinding.FragmentUserSearchBinding
import com.doozez.doozez.ui.safe.SafeListFragment
import com.doozez.doozez.ui.safe.SafesRecyclerViewAdapter
import com.doozez.doozez.ui.user.listeners.OnUserSearchItemClickListener
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserSearchFragment : Fragment(), OnUserSearchItemClickListener {
    private var _binding: FragmentUserSearchBinding? = null
    private val binding get() = _binding!!
    private val _this: UserSearchFragment = this

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserSearchBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.userSearchEmail.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if(count >= 3) {
                    val call = ApiClient.userService.searchUsers(s.toString())
                    call.enqueue(object : Callback<List<UserItemResponse>> {
                        override fun onResponse(
                            call: Call<List<UserItemResponse>>,
                            response: Response<List<UserItemResponse>>
                        ) {
                            if (response.isSuccessful && response.body() != null) {
                                var users = response.body().toMutableList()
                                // Set the adapter
                                with(binding.userSearchList) {
                                    layoutManager = LinearLayoutManager(context)
                                    adapter = UserSearchRecyclerViewAdapter(users, _this)
                                }
                            }
                        }

                        override fun onFailure(call: Call<List<UserItemResponse>>, t: Throwable) {
                            var s = ""
                            var d = s
                        }
                    })
                }
            }
        })
        return view
    }

    override fun userItemClicked(item: UserItemResponse) {
        Snackbar.make(binding.safeListContainer, "dummy invite added", Snackbar.LENGTH_SHORT).show()
    }
}