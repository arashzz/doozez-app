package com.doozez.doozez.ui.safe.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.doozez.doozez.api.safe.SafeDetailResponse
import com.doozez.doozez.databinding.FragmentSafeItemBinding
import com.doozez.doozez.ui.safe.listeners.OnSafeItemClickListener

class SafeListAdapter(
    private val values: List<SafeDetailResponse>, onClickListener: OnSafeItemClickListener
) : RecyclerView.Adapter<SafeListAdapter.SafeViewHolder>() {

    private val onClickListener: OnSafeItemClickListener = onClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SafeViewHolder {

        return SafeViewHolder(
            FragmentSafeItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: SafeViewHolder, position: Int) {
        val item = values[position]
        holder.name.text = item.name
        holder.status.text = item.status
        holder.cardview.setOnClickListener {
            onClickListener.safeItemClicked(item)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class SafeViewHolder(binding: FragmentSafeItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val cardview: CardView = binding.safeCard
        val name: TextView = binding.safeItemName
        val status: TextView = binding.safeItemStatus

        override fun toString(): String {
            return super.toString() + " '"
        }
    }

}