package com.doozez.doozez.ui.user

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import com.doozez.doozez.api.ApiClient
import com.doozez.doozez.api.SharedPrefManager
import com.doozez.doozez.api.enqueue
import com.doozez.doozez.api.user.UserDetailResp
import com.doozez.doozez.databinding.FragmentProfileBinding
import com.doozez.doozez.enums.SharedPrerfKey
import com.google.android.material.snackbar.Snackbar


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private var userId: Int? = null
    private var editMode = false
    private var profile: UserDetailResp? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = SharedPrefManager.getInt(SharedPrerfKey.USER_ID.name)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        addListeners()
        getProfile()
        return binding.root
    }

    private fun addListeners() {
        binding.profileEdit.setOnClickListener {
            toggleEditMode()
        }
        binding.profileEditCancel.setOnClickListener {
            toggleEditMode()
        }
//        binding.profileUpdate.setOnClickListener {
//            if(validateForm()) {
//                val body = UserProfileReq(
//                    binding.profileFirstname.editText?.text.toString(),
//                    binding.profileLastname.editText?.text.toString()
//                )
//                val call = ApiClient.userService.updateUser(userId!!, body)
//                call.enqueue {
//                    onResponse = {
//                        if(it.isSuccessful && it.body() != null) {
//                            Snackbar.make(
//                                binding.profileContainer,
//                                "Profile updated successfully",
//                                Snackbar.LENGTH_SHORT).show()
//                            loadProfile(it.body())
//                        } else {
//                            Log.e(TAG, it.errorBody().toString())
//                            Snackbar.make(
//                                binding.profileContainer,
//                                "Failed to update profile",
//                                Snackbar.LENGTH_SHORT).show()
//                        }
//                    }
//
//                    onFailure = {
//                        Log.e(TAG, it?.stackTrace.toString())
//                        Snackbar.make(
//                            binding.profileContainer,
//                            "Failed to update profile",
//                            Snackbar.LENGTH_SHORT).show()
//                    }
//                }
//            }
//        }

//        binding.profileFirstname.editText?.doOnTextChanged { inputText, _, _, _ ->
//            if (inputText.isNullOrEmpty()) {
//                binding.profileFirstname.error = getString(R.string.error_validation_empty_first_name)
//            } else {
//                binding.profileFirstname.error = null
//            }
//        }
//
//        binding.profileLastname.editText?.doOnTextChanged { inputText, _, _, _ ->
//            if (inputText.isNullOrEmpty()) {
//                binding.profileLastname.error = getString(R.string.error_validation_empty_last_name)
//            } else {
//                binding.profileLastname.error = null
//            }
//        }
    }

//    private fun validateForm(): Boolean {
//        return binding.profileFirstname.error == null &&
//                binding.profileLastname.error == null
//    }

    private fun getProfile() {
        val call = ApiClient.userService.getUserProfile(userId!!)
        call.enqueue {
            onResponse = {
                if(it.isSuccessful && it.body() != null) {
                    loadProfile(it.body())
                }
            }
            onFailure = {
                Log.e(TAG, it?.stackTrace.toString())
                Snackbar.make(
                    binding.profileContainer,
                    "Failed to get user profile",
                    Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadProfile(profile: UserDetailResp) {
        this.profile = profile
        binding.profileEmailLabel.text = profile.email
        val fullName = "${profile.firstName} ${profile.lastName}"
        binding.profileName.text = fullName
    }

    private fun toggleEditMode() {
        if(editMode) {
            binding.profileEditContainer.visibility = View.GONE
            binding.profileEditActionContainer.visibility = View.GONE
            binding.profileReadonlyContainer.visibility = View.VISIBLE
            binding.profileReadonlyActionContainer.visibility = View.VISIBLE
        } else {
            binding.profileEditContainer.visibility = View.VISIBLE
            binding.profileEditActionContainer.visibility = View.VISIBLE
            binding.profileReadonlyContainer.visibility = View.GONE
            binding.profileReadonlyActionContainer.visibility = View.GONE

            binding.profileEditEmail.editText?.setText(this.profile?.email)
            binding.profileEditFirstName.editText?.setText(this.profile?.firstName)
            binding.profileEditLastName.editText?.setText(this.profile?.lastName)
        }
        editMode = !editMode
        addListeners()
    }

    companion object {
        private const val TAG = "ProfileFragment"
    }
}