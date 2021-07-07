package com.doozez.doozez.ui.safe.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.doozez.doozez.R
import com.doozez.doozez.api.invitation.InvitationDetailResponse
import com.doozez.doozez.databinding.FragmentSafeDetailInviteItemBinding
import com.doozez.doozez.utils.InvitationStatus

class SafeDetailInviteListAdapter(
    private val values: MutableList<InvitationDetailResponse>):
        RecyclerView.Adapter<SafeDetailInviteListAdapter.ViewHolder>() {

    fun addItems(items: List<InvitationDetailResponse>) {
        values.clear()
        values.addAll(items)
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
        holder.name.text = item.recipient?.firstName + " " + item.recipient?.lastName
        holder.email.text = item.recipient?.email
        holder.status.setImageResource(getStatusIcon(item.status!!))
    }

    private fun getStatusIcon(code: String): Int {
        var id = R.drawable.ic_baseline_access_time_24
        if (code == InvitationStatus.ACCEPTED) {
            id = R.drawable.ic_round_check_24
        } else if (code == InvitationStatus.DECLINED) {
            id = R.drawable.ic_baseline_clear_24
        }
        return id
    }

    inner class ViewHolder(binding: FragmentSafeDetailInviteItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val container: RelativeLayout = binding.safeDetailInviteItemContainer
        val name: TextView = binding.safeDetailInviteItemName
        val email: TextView = binding.safeDetailInviteItemEmail
        val status: ImageView = binding.safeDetailInviteItemStatus

        override fun toString(): String {
            return super.toString() + " '"
        }
    }
}