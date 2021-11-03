package com.doozez.doozez.ui.payment.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.doozez.doozez.R
import com.doozez.doozez.api.paymentMethod.PaymentMethodDetailResp
import com.doozez.doozez.databinding.FragmentPaymentMethodsItemBinding
import com.doozez.doozez.ui.payment.listeners.PaymentMethodItemListener
import com.doozez.doozez.utils.InvitationStatus
import com.doozez.doozez.utils.PaymentMethodStatus
import com.doozez.doozez.utils.PaymentMethodType

class PaymentMethodsAdapter
(
    private val values: MutableList<PaymentMethodDetailResp>,
    private val listener: PaymentMethodItemListener
) : RecyclerView.Adapter<PaymentMethodsAdapter.ViewHolder>() {

    private var selectedItem: PaymentMethodDetailResp? = null

    fun getSelectedItem(): PaymentMethodDetailResp? {
        return selectedItem
    }

    fun addItems(items: List<PaymentMethodDetailResp>) {
        with(values) {
            clear()
            addAll(items)
            notifyDataSetChanged()
        }
    }

    fun addItem(item: PaymentMethodDetailResp) {
        values.add(item)
        notifyItemChanged(values.size-1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentMethodsAdapter.ViewHolder {

        return ViewHolder(
            FragmentPaymentMethodsItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.name.text = item.name
        val type = PaymentMethodType.fromCode("DIRECT_DEBIT")
        holder.type.text = type.displayName
        holder.status.setImageResource(type.resId)
        holder.container.setOnClickListener {
            listener.paymentMethodClicked(item)
        }
    }

    override fun getItemCount(): Int = values.size

    private fun selectedItemChanged(item: PaymentMethodDetailResp) {
        selectedItem = item
    }

    inner class ViewHolder(_binding: FragmentPaymentMethodsItemBinding) : RecyclerView.ViewHolder(_binding.root) {
        private val binding: FragmentPaymentMethodsItemBinding = _binding
        val container: CardView = binding.paymentMethodsItemContainer
        val name: TextView = binding.paymentMethodsItemName
        val type: TextView = binding.paymentMethodsItemType
        val status: ImageView = binding.paymentMethodsItemStatus
    }
}