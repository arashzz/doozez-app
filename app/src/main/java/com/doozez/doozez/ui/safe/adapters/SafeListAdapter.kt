package com.doozez.doozez.ui.safe.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.doozez.doozez.api.safe.SafeDetailResp
import com.doozez.doozez.databinding.FragmentSafeItemBinding
import com.doozez.doozez.ui.safe.listeners.OnSafeItemClickListener

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
            FragmentSafeItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.name.text = item.name
        holder.status.text = item.status
        holder.cardview.setOnClickListener {
            onClickListener.safeItemClicked(item)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(_binding: FragmentSafeItemBinding) : RecyclerView.ViewHolder(_binding.root) {
        private val binding: FragmentSafeItemBinding = _binding
        val cardview: CardView = binding.safeCard
        val name: TextView = binding.safeItemName
        val status: TextView = binding.safeItemStatus

        override fun toString(): String {
            return super.toString() + " '"
        }
    }
}