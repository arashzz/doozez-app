package com.doozez.doozez.ui.task

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.doozez.doozez.api.ApiClient
import com.doozez.doozez.api.enqueue
import com.doozez.doozez.databinding.FragmentTaskListBinding
import com.doozez.doozez.ui.task.adapters.TaskListAdapter
import com.doozez.doozez.enums.BundleKey
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar


class TaskListFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!
    private var adapter: TaskListAdapter? = null
    private var safeId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = TaskListAdapter(mutableListOf())
        arguments?.let {
            safeId = it.getInt(BundleKey.SAFE_ID.name)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        with(binding.taskListList) {
            layoutManager = LinearLayoutManager(context)
            adapter = this@TaskListFragment.adapter
        }
        loadTasks()
        return binding.root
    }

    private fun loadTasks() {
        val call = ApiClient.jobService.getJobBySafe(safeId)
        call.enqueue {
            onResponse = {
                if(it.isSuccessful && it.body() != null) {
                    if(it.body()!!.isNotEmpty()) {
                        adapter?.addItems(it.body()!![0].tasks)
                    }

                }
            }

            onFailure = {
                Log.e("TaskListFragment", it?.stackTrace.toString())
                Snackbar.make(
                    binding.taskListList,
                    "Unknown Error",
                    Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}