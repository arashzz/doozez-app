package com.doozez.doozez.ui.safe.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.doozez.doozez.api.invitation.InvitationDetailResponse
import com.doozez.doozez.databinding.FragmentSafeDetailInviteItemBinding

class SafeDetailInviteListAdapter(
    private val values: List<InvitationDetailResponse>):
        RecyclerView.Adapter<SafeDetailInviteListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentSafeDetailInviteItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = values.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.name.text = item.recipient?.firstName + " " + item.recipient?.lastName
        holder.email.text = item.recipient?.email
    }

    inner class ViewHolder(binding: FragmentSafeDetailInviteItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val container: LinearLayout = binding.safeDetailInviteItemContainer
        val name: TextView = binding.safeDetailInviteItemName
        val email: TextView = binding.safeDetailInviteItemEmail

        override fun toString(): String {
            return super.toString() + " '"
        }
    }
}