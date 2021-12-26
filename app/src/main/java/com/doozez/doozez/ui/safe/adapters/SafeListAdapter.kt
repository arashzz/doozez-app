package com.doozez.doozez.ui.safe.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.doozez.doozez.api.safe.SafeDetailResp
import com.doozez.doozez.databinding.FragmentSafeListItemBinding
import com.doozez.doozez.enums.SafeStatus
import com.doozez.doozez.ui.safe.listeners.OnSafeItemClickListener
import com.doozez.doozez.ui.view.SafeStatusCustomView

class SafeListAdapter(
    private val values: MutableList<SafeDetailResp>, onClickListener: OnSafeItemClickListener
) : RecyclerView.Adapter<SafeListAdapter.ViewHolder>() {

    private val onClickListener: OnSafeItemClickListener = onClickListener

    fun addItems(items: List<SafeDetailResp>) {
        with(values) {
            clear()
            addAll(items)
        }.also { notifyDataSetChanged() }

    }

    fun addItem(item: SafeDetailResp) {
        values.add(item)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentSafeListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.name.text = item.name
        val status = SafeStatus.fromCode(item.status)
        holder.status.changeStatus(status)
        holder.amount.text = item.monthlyPayment.toString()
        holder.container.setOnClickListener {
            onClickListener.safeItemClicked(item)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(_binding: FragmentSafeListItemBinding) :
        RecyclerView.ViewHolder(_binding.root) {
        private val binding: FragmentSafeListItemBinding = _binding
        val name: TextView = binding.safeListItemName
        val status: SafeStatusCustomView = binding.safeListItemStatus
        val container: RelativeLayout = binding.safeListItemContainer
        val amount: TextView = binding.safeListItemMonthlyPayment
    }
}