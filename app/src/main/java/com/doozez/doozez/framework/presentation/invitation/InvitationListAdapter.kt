package com.doozez.doozez.framework.presentation.invitation

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.doozez.doozez.api.invitation.InviteDetailResp
import com.doozez.doozez.business.domain.models.invitation.Invitation
import com.doozez.doozez.databinding.FragmentInvitationItemBinding
import com.doozez.doozez.enums.InvitationStatus
import com.google.android.material.button.MaterialButton

class InvitationListAdapter(
    private val values: MutableList<Invitation>,
    private val userId: Int, private val listener: OnInviteActionClickListener,
    private val ctx: Context
) : RecyclerView.Adapter<InvitationListAdapter.InvitationViewHolder>() {

    fun addItems(items: List<Invitation>) {
        with(values) {
            clear()
            addAll(items)
        }.also { notifyDataSetChanged() }
    }

//    fun itemStatusChanged(itemId: Int, status: InvitationStatus) {
//        values.forEachIndexed { index, it ->
//            if(it.id == itemId) {
//                it.status = status.code
//                values[index] = it
//                notifyItemChanged(index)
//            }
//        }
//    }

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
        var actionMsg = "Unknown name "
        val item = values[position]
        holder.name.text = item.safe.name
        holder.monthlyPayment.text = item.safe.monthlyPayment.toString()
        addListeners(holder, item)
        if (userId == item.recipient.id) {
            if(item.status == InvitationStatus.PENDING) {
                holder.acceptBtn.isEnabled = true
                holder.declineBtn.isEnabled = true
            }
            actionMsg = "Invited by ${item.sender.firstName} ${item.sender.lastName}"
        } else if (userId == item.sender.id){
            actionMsg = "Invitation sent to ${item.recipient.firstName} ${item.recipient.lastName}"
        }
        holder.actionMessage.text = actionMsg
        holder.status.text = item.status.description
        holder.status.setTextColor(ContextCompat.getColor(ctx, item.status.colorId))
    }

    override fun getItemCount(): Int = values.size

    private fun addListeners(holder: InvitationViewHolder, item: Invitation) {
//        holder.cancelBtn.setOnClickListener {
//            listener.inviteCancelled(item)
//        }
        holder.acceptBtn.setOnClickListener {
            listener.inviteAccepted(item)
        }
        holder.declineBtn.setOnClickListener {
            listener.inviteDeclined(item)
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
//        val cancelBtn : MaterialButton = binding.inviteDetailCancel
        val safeBtn : MaterialButton = binding.inviteDetailSafe
        val status : TextView = binding.inviteDetailStatus
    }
}