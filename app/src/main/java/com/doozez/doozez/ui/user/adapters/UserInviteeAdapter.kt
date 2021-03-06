package com.doozez.doozez.ui.user.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.doozez.doozez.api.user.UserDetailResp
import com.doozez.doozez.databinding.FragmentUserInviteeListItemBinding
import com.doozez.doozez.ui.user.listeners.OnUserSearchItemClickListener
import com.doozez.doozez.ui.user.listeners.UserInviteeListener
import com.google.android.material.button.MaterialButton

class UserInviteeAdapter(
    private val values: MutableList<UserDetailResp>, private val listener: UserInviteeListener
): RecyclerView.Adapter<UserInviteeAdapter.UserViewHolder>() {

    fun addItems(items: List<UserDetailResp>) {
        values.clear()
        values.addAll(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {

        return UserViewHolder(
            FragmentUserInviteeListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = values.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = values[position]
        val name = "${item.firstName} ${item.lastName}"
        holder.name.text = name
        holder.remove.setOnClickListener {
            listener.inviteeRemoved(item)
        }

    }

    inner class UserViewHolder(binding: FragmentUserInviteeListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val container: LinearLayout = binding.userSearchItemContainer
        val name: TextView = binding.inviteeName
        val remove: MaterialButton = binding.inviteeRemove

        override fun toString(): String {
            return super.toString() + " '"
        }
    }
}