package com.doozez.doozez.ui.user

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.doozez.doozez.api.safe.SafeDetailResponse
import com.doozez.doozez.api.user.UserItemResponse
import com.doozez.doozez.databinding.FragmentSafeItemBinding
import com.doozez.doozez.databinding.FragmentUserSearchItemBinding
import com.doozez.doozez.ui.safe.SafesRecyclerViewAdapter
import com.doozez.doozez.ui.safe.listeners.OnSafeItemClickListener
import com.doozez.doozez.ui.user.listeners.OnUserSearchItemClickListener
import com.google.android.material.button.MaterialButton

class UserSearchRecyclerViewAdapter(
    private val values: List<UserItemResponse>, onClickListener: OnUserSearchItemClickListener
): RecyclerView.Adapter<UserSearchRecyclerViewAdapter.UserViewHolder>() {

    private val onClickListener: OnUserSearchItemClickListener = onClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserSearchRecyclerViewAdapter.UserViewHolder {

        return UserViewHolder(
            FragmentUserSearchItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = values.size

    override fun onBindViewHolder(holder: UserSearchRecyclerViewAdapter.UserViewHolder, position: Int) {
        val item = values[position]
        holder.name.text = item.firstName + " " + item.lastName
        holder.email.text = item.email
        holder.addInvite.setOnClickListener {
            onClickListener.userItemClicked(item)
        }

    }

    inner class UserViewHolder(binding: FragmentUserSearchItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val container: LinearLayout = binding.userSearchItemContainer
        val name: TextView = binding.userSearchItemName
        val email: TextView = binding.userSearchItemEmail
        val addInvite: MaterialButton = binding.userSearchItemAdd

        override fun toString(): String {
            return super.toString() + " '"
        }
    }
}