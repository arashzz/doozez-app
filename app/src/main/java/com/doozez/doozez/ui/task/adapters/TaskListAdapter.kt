package com.doozez.doozez.ui.task.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.doozez.doozez.R
import com.doozez.doozez.api.job.TaskDetail
import com.doozez.doozez.databinding.FragmentTaskListItemBinding
import com.doozez.doozez.enums.TaskStatus

class TaskListAdapter
    (private val values: MutableList<TaskDetail>)
    : RecyclerView.Adapter<TaskListAdapter.ViewHolder>() {

    fun addItems(items: List<TaskDetail>) {
        with(values) {
            clear()
            addAll(items)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskListAdapter.ViewHolder {

        return ViewHolder(
            FragmentTaskListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.type.text = item.type
        if(item.status == TaskStatus.PND) {
            holder.icon.setBackgroundResource(R.drawable.ic_baseline_arrow_forward_24)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(_binding: FragmentTaskListItemBinding) : RecyclerView.ViewHolder(_binding.root) {
        private val binding: FragmentTaskListItemBinding = _binding
//        val container: RelativeLayout = binding.taskListItemContainer
        val type: TextView = binding.taskListItemType
        val icon: ImageView = binding.taskListItemIcon
    }
}