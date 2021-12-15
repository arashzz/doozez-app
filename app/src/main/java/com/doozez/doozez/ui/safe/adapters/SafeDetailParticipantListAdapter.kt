package com.doozez.doozez.ui.safe.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.doozez.doozez.api.participation.ParticipationResp
import com.doozez.doozez.databinding.FragmentSafeDetailParticipantItemBinding
import com.doozez.doozez.enums.ParticipationStatus

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

        val name = "${item.user.firstName} ${item.user.lastName}"
        holder.name.text = name
        val sequence = "${item.winSequence}."
        holder.sequence.text = sequence
        var status = ParticipationStatus.ACTIVE
        if(item.id == 2) {
            status = ParticipationStatus.COMPLETE
        }
        if(status == ParticipationStatus.COMPLETE) {
            holder.winner.visibility = View.VISIBLE
        }
    }

    inner class ViewHolder(binding: FragmentSafeDetailParticipantItemBinding): RecyclerView.ViewHolder(binding.root) {
        val container: RelativeLayout = binding.safeDetailParticipantItemContainer
        val name: TextView = binding.safeDetailParticipantItemName
        val sequence: TextView = binding.safeDetailParticipantItemSequence
        val winner: ImageView = binding.safeDetailParticipantItemWinner
    }
}