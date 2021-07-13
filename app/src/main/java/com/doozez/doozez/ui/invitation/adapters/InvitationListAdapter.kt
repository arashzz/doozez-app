package com.doozez.doozez.ui.invitation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.doozez.doozez.R
import com.doozez.doozez.api.invitation.InvitationDetailResponse
import com.doozez.doozez.databinding.FragmentInvitationItemBinding
import com.doozez.doozez.ui.invitation.listeners.OnInviteActionClickListener
import com.doozez.doozez.utils.InvitationStatus
import com.google.android.material.button.MaterialButton

class InvitationListAdapter(
    private val values: MutableList<InvitationDetailResponse>,
    private val userId: Int, private val listener: OnInviteActionClickListener,
    private val ctx: Context
) : RecyclerView.Adapter<InvitationListAdapter.InvitationViewHolder>() {

    fun addItems(items: List<InvitationDetailResponse>) {
        values.addAll(items)
    }

    fun itemStatusChanged(itemId: Int, status: String) {
        values.forEachIndexed { index, it ->
            if(it.id == itemId) {
                it.status = status
                values[index] = it
                notifyItemChanged(index)
            }
        }
    }

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
        var senderMsg = "Invited by"
        val item = values[position]
        holder.name.text = item.safe?.name
        holder.monthlyPayment.text = item.safe?.monthlyPayment.toString()
        addListeners(holder, item)
        if (userId != item.recipient.id) {
            (holder.acceptBtn.parent as? ViewGroup)?.removeView(holder.acceptBtn)
            (holder.declineBtn.parent as? ViewGroup)?.removeView(holder.declineBtn)
            senderMsg = "Invite sent to"
        } else {
            if (item.status != InvitationStatus.PENDING) {
                holder.declineBtn.isEnabled = false
                holder.acceptBtn.isEnabled = false
            }
        }
        holder.sender.text = senderMsg + " ${item.initiator?.firstName} ${item.initiator?.lastName}"
        var statusTxt = "Pending"
        var statusColor = ContextCompat.getColor(ctx, R.color.yellow)
        when(item.status) {
            InvitationStatus.ACCEPTED -> {
                statusTxt = "Accepted"
                statusColor = ContextCompat.getColor(ctx, R.color.green)
            }
            InvitationStatus.DECLINED -> {
                statusTxt = "Declined"
                statusColor = ContextCompat.getColor(ctx, R.color.red)
            }
        }
        holder.status.text = statusTxt
        holder.status.setTextColor(statusColor)
    }

    override fun getItemCount(): Int = values.size

    private fun addListeners(holder: InvitationViewHolder, item: InvitationDetailResponse) {
        if (item.status == InvitationStatus.PENDING) {
            holder.acceptBtn.setOnClickListener {
                listener.inviteAccepted(item)
            }
            holder.declineBtn.setOnClickListener {
                listener.inviteDeclined(item)
            }
        }
        holder.safeBtn.setOnClickListener {
            listener.inviteSafeClicked(item)
        }
    }

    inner class InvitationViewHolder(binding: FragmentInvitationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val name: TextView = binding.inviteDetailName
        val sender: TextView = binding.inviteDetailSender
        val monthlyPayment: TextView = binding.inviteDetailMonthlyPayment
        val acceptBtn : MaterialButton = binding.inviteDetailAccept
        val declineBtn : MaterialButton = binding.inviteDetailDecline
        val safeBtn : MaterialButton = binding.inviteDetailSafe
        val status : TextView = binding.inviteDetailStatus
    }
}