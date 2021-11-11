package com.doozez.doozez.ui.invitation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.doozez.doozez.R
import com.doozez.doozez.api.invitation.InviteDetailResp
import com.doozez.doozez.databinding.FragmentInvitationItemBinding
import com.doozez.doozez.ui.invitation.listeners.OnInviteActionClickListener
import com.doozez.doozez.utils.InvitationStatus
import com.google.android.material.button.MaterialButton

class InvitationListAdapter(
    private val values: MutableList<InviteDetailResp>,
    private val userId: Int, private val listener: OnInviteActionClickListener,
    private val ctx: Context
) : RecyclerView.Adapter<InvitationListAdapter.InvitationViewHolder>() {

    fun addItems(items: List<InviteDetailResp>) {
        with(values) {
            clear()
            addAll(items)
        }.also { notifyDataSetChanged() }
    }

    fun itemStatusChanged(itemId: Int, status: InvitationStatus) {
        values.forEachIndexed { index, it ->
            if(it.id == itemId) {
                it.status = status.code
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
        var actionMsg = "Invited by "
        val item = values[position]
        val status = InvitationStatus.fromCode(item.status)
        holder.name.text = item.safe.name
        holder.monthlyPayment.text = item.safe.monthlyPayment.toString()
        addListeners(holder, item)
        if (userId != item.recipient.id) {
            (holder.acceptBtn.parent as? ViewGroup)?.removeView(holder.acceptBtn)
            (holder.declineBtn.parent as? ViewGroup)?.removeView(holder.declineBtn)
            if (status == InvitationStatus.PENDING || status == InvitationStatus.CANCELLED) {
                holder.cancelBtn.visibility = View.VISIBLE
                if (status == InvitationStatus.CANCELLED) {
                    holder.cancelBtn.isEnabled = false
                }
            }
            actionMsg = "Invitation sent to ${item.recipient.firstName} ${item.recipient.lastName}"
        } else {
            actionMsg += "${item.sender.firstName} ${item.sender.lastName}"
            if (status != InvitationStatus.PENDING) {
                holder.declineBtn.isEnabled = false
                holder.acceptBtn.isEnabled = false
            }
        }
        holder.actionMessage.text = actionMsg
        holder.status.text = status.description
        holder.status.setTextColor(ContextCompat.getColor(ctx, status.colorId))
    }

    override fun getItemCount(): Int = values.size

    private fun addListeners(holder: InvitationViewHolder, item: InviteDetailResp) {
        val status = InvitationStatus.fromCode(item.status)
        if (status == InvitationStatus.PENDING) {
            holder.acceptBtn.setOnClickListener {
                listener.inviteAccepted(item)
            }
            holder.declineBtn.setOnClickListener {
                listener.inviteDeclined(item)
            }
            holder.cancelBtn.setOnClickListener {
                listener.inviteCancelled(item)
            }
        }
        holder.safeBtn.setOnClickListener {
            listener.inviteSafeClicked(item)
        }
    }

    inner class InvitationViewHolder(binding: FragmentInvitationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val name: TextView = binding.inviteDetailName
        val actionMessage: TextView = binding.inviteDetailAction
        val monthlyPayment: TextView = binding.inviteDetailMonthlyPayment
        val acceptBtn : MaterialButton = binding.inviteDetailAccept
        val declineBtn : MaterialButton = binding.inviteDetailDecline
        val cancelBtn : MaterialButton = binding.inviteDetailCancel
        val safeBtn : MaterialButton = binding.inviteDetailSafe
        val status : TextView = binding.inviteDetailStatus
    }
}