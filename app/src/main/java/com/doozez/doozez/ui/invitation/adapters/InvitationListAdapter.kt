package com.doozez.doozez.ui.invitation.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.doozez.doozez.api.invitation.InvitationDetailResponse
import com.doozez.doozez.databinding.FragmentInvitationItemBinding

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class InvitationListAdapter(
    private val values: List<InvitationDetailResponse>
) : RecyclerView.Adapter<InvitationListAdapter.InvitationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvitationViewHolder {

        return InvitationViewHolder(
            FragmentInvitationItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: InvitationViewHolder, position: Int) {
        val item = values[position]
        holder.status.text = item.status
        holder.sender.text = "Invited by " + item.sender
        holder.monthlyPayment.text = item.monthlyPayment.toString()
    }

    override fun getItemCount(): Int = values.size

    inner class InvitationViewHolder(binding: FragmentInvitationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val status: TextView = binding.inviteDetailStatus
        val sender: TextView = binding.inviteDetailSender
        val monthlyPayment: TextView = binding.inviteDetailMonthlyPayment

        override fun toString(): String {
            return super.toString() + " '"
        }
    }

}