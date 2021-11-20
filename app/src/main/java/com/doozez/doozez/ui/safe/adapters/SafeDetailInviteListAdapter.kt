package com.doozez.doozez.ui.safe.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.doozez.doozez.R
import com.doozez.doozez.api.invitation.InviteDetailResp
import com.doozez.doozez.databinding.FragmentSafeDetailInviteItemBinding
import com.doozez.doozez.ui.safe.listeners.SafeInviteeListener
import com.doozez.doozez.utils.InvitationStatus

class SafeDetailInviteListAdapter(
    private val values: MutableList<InviteDetailResp>,
    private val listener: SafeInviteeListener):
        RecyclerView.Adapter<SafeDetailInviteListAdapter.ViewHolder>() {

    fun addItems(items: List<InviteDetailResp>) {
        values.clear()
        values.addAll(items)
    }

    fun removeItem(item: InviteDetailResp) {
        values.remove(item)
    }

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
        val name = "${item.recipient.firstName} ${item.recipient.lastName}"
        holder.name.text = name
        holder.email.text = item.recipient?.email
        val status = InvitationStatus.fromCode(item.status)
        holder.status.setImageResource(status.resId)
        if(status != InvitationStatus.DECLINED && status != InvitationStatus.CANCELLED) {
            holder.remove.visibility = View.VISIBLE
            holder.remove.setOnClickListener {
                listener.inviteeRemoved(item)
            }
        }

    }

    inner class ViewHolder(binding: FragmentSafeDetailInviteItemBinding) :  RecyclerView.ViewHolder(binding.root) {
        val container: RelativeLayout = binding.safeDetailInviteItemContainer
        val name: TextView = binding.safeDetailInviteItemName
        val email: TextView = binding.safeDetailInviteItemEmail
        val status: ImageView = binding.safeDetailInviteItemStatus
        val remove: ImageView = binding.safeDetailInviteItemRemove

        override fun toString(): String {
            return super.toString() + " '"
        }
    }
}