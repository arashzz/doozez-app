package com.doozez.doozez.ui.safe

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.doozez.doozez.R
import com.doozez.doozez.api.ApiClient
import com.doozez.doozez.api.safe.SafeDetailResponse
import com.doozez.doozez.databinding.FragmentSafesListBinding
import com.doozez.doozez.ui.safe.adapters.SafeListAdapter
import com.doozez.doozez.ui.safe.listeners.OnSafeCreatedListener
import com.doozez.doozez.ui.safe.listeners.OnSafeItemClickListener
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A fragment representing a list of Items.
 */
class SafeListFragment : Fragment(), OnSafeItemClickListener, OnSafeCreatedListener {
    private var _binding: FragmentSafesListBinding? = null
    private val binding get() = _binding!!
    private val _this: SafeListFragment = this

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSafesListBinding.inflate(inflater, container, false)
        val view = binding.root
        val call = ApiClient.safeService.getSafesForUser()
        call.enqueue(object : Callback<List<SafeDetailResponse>> {
            override fun onResponse(call: Call<List<SafeDetailResponse>>, response: Response<List<SafeDetailResponse>>) {
                if (response.isSuccessful && response.body() != null) {
                    var safes = response.body().toMutableList()
                    // Set the adapter
                    with(binding.safesRecyclerView) {
                        layoutManager = LinearLayoutManager(context)
                        adapter = SafeListAdapter(safes, _this)
                    }
                }
            }
            override fun onFailure(call: Call<List<SafeDetailResponse>>, t: Throwable) {
                //TODO: do something
            }
        })
        binding.addSafe.setOnClickListener {
            //val navController = findNavController()
//            navController.navigate(R.id.action_nav_safe_to_nav_safe_create)
            SafeCreateFragment(_this).show(childFragmentManager, "")
        }
        return view
    }

    override fun safeItemClicked(item: SafeDetailResponse) {
        var navController = findNavController()
        val bundle = bundleOf("safeId" to item.id)
        navController.navigate(R.id.action_nav_safe_to_nav_safe_detail, bundle)
    }

    @SuppressLint("ResourceAsColor")
    override fun onSuccessSafeCreate(msg: String) {
        var sb = Snackbar.make(binding.safeListContainer, msg, Snackbar.LENGTH_SHORT)
//        sb.view.setBackgroundColor(R.color.light_blue_600)
        sb.show()
    }

    @SuppressLint("ResourceAsColor")
    override fun onFailureSafeCreate(msg: String) {
        var sb = Snackbar.make(binding.safeListContainer, msg, Snackbar.LENGTH_SHORT)
//        sb.view.setBackgroundColor(R.color.design_default_color_error)
        sb.show()
    }
}