package com.doozez.doozez.ui.user.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.doozez.doozez.api.user.UserDetailResp
import com.doozez.doozez.databinding.FragmentUserSearchItemBinding
import com.doozez.doozez.ui.user.listeners.OnUserSearchItemClickListener
import com.google.android.material.button.MaterialButton

class UserSearchAdapter(
    private val values: MutableList<UserDetailResp>, private val listener: OnUserSearchItemClickListener
): RecyclerView.Adapter<UserSearchAdapter.UserViewHolder>() {

    fun addItems(items: List<UserDetailResp>) {
        values.clear()
        values.addAll(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {

        return UserViewHolder(
            FragmentUserSearchItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = values.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = values[position]
        val fullName = "${item.firstName} ${item.lastName}"
        holder.name.text =  fullName
//        holder.email.text = item.email
        holder.addInvite.setOnClickListener {
            listener.userItemClicked(item)
        }

    }

    inner class UserViewHolder(binding: FragmentUserSearchItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val name: TextView = binding.userSearchItemName
//        val email: TextView = binding.userSearchItemEmail
        val addInvite: MaterialButton = binding.userSearchItemAdd

        override fun toString(): String {
            return super.toString() + " '"
        }
    }
}