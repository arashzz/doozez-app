package com.doozez.doozez.ui.safe

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.doozez.doozez.R
import com.doozez.doozez.api.ApiClient
import com.doozez.doozez.api.enqueue
import com.doozez.doozez.api.safe.SafeDetailResp
import com.doozez.doozez.databinding.FragmentSafesListBinding
import com.doozez.doozez.ui.safe.adapters.SafeListAdapter
import com.doozez.doozez.ui.safe.listeners.OnSafeItemClickListener
import com.doozez.doozez.utils.BundleKey
import com.doozez.doozez.utils.ResultKey
import com.google.android.material.snackbar.Snackbar

class SafeListFragment : Fragment(), OnSafeItemClickListener {
    private var _binding: FragmentSafesListBinding? = null
    private val binding get() = _binding!!
    private val adapter = SafeListAdapter(mutableListOf(), this)
    private var userId = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSafesListBinding.inflate(inflater, container, false)
        with(binding.safesRecyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = this@SafeListFragment.adapter
        }
        addListeners()
        loadSafes()
        return binding.root
    }

    override fun safeItemClicked(item: SafeDetailResp) {
        findNavController().navigate(R.id.action_nav_safe_to_nav_safe_detail, bundleOf(
            BundleKey.SAFE_ID to item.id,
            BundleKey.USER_ID to userId
        ))
    }

    private fun addListeners() {
        setFragmentResultListener(ResultKey.SAFE_ADDED) { _, bundle ->
            val resultOk = bundle.getBoolean(BundleKey.RESULT_OK)
            if (resultOk) {
                loadNewSafe(
                    bundle.getParcelable(BundleKey.SAFE_OBJECT)
                )
            }
        }
        binding.addSafe.setOnClickListener {
            findNavController().navigate(R.id.action_nav_safe_to_nav_safe_create)
        }
    }

    private fun loadNewSafe(safe: SafeDetailResp?) {
        safe?.let { adapter.addItem(it) }
    }

    private fun loadSafes() {
        val call = ApiClient.safeService.getSafesForUser()
        call.enqueue {
            onResponse = {
                if (it.isSuccessful && it.body() != null) {
                    adapter.addItems(it.body())
                }
            }
            onFailure = {
                Log.e("SafeListFragment", it?.stackTrace.toString())
                Snackbar.make(binding.safeListContainer, "failed...", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}