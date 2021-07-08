package com.doozez.doozez.ui.safe.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.doozez.doozez.R
import com.doozez.doozez.api.invitation.InvitationDetailResponse
import com.doozez.doozez.api.participation.ParticipationResp
import com.doozez.doozez.databinding.FragmentSafeDetailInviteItemBinding
import com.doozez.doozez.databinding.FragmentSafeDetailParticipantItemBinding
import com.doozez.doozez.utils.InvitationStatus

class SafeDetailParticipantListAdapter(
    private val values: MutableList<ParticipationResp>):
        RecyclerView.Adapter<SafeDetailParticipantListAdapter.ViewHolder>() {

    fun addItems(items: List<ParticipationResp>) {
        values.clear()
        values.addAll(items)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentSafeDetailParticipantItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = values.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]

        val name = "${item.user?.firstName} ${item.user?.lastName}"
        holder.name.text = name
        holder.email.text = item.user?.email
    }

    inner class ViewHolder(binding: FragmentSafeDetailParticipantItemBinding): RecyclerView.ViewHolder(binding.root) {
        val container: RelativeLayout = binding.safeDetailParticipantItemContainer
        val name: TextView = binding.safeDetailParticipantItemName
        val email: TextView = binding.safeDetailParticipantItemEmail

        override fun toString(): String {
            return super.toString() + " '"
        }
    }
}