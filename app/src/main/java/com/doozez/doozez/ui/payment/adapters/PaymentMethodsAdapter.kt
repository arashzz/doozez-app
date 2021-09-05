package com.doozez.doozez.ui.payment.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.doozez.doozez.api.payments.PaymentDetailResp
import com.doozez.doozez.databinding.FragmentPaymentMethodsItemBinding
import com.doozez.doozez.ui.payment.listeners.PaymentMethodItemListener
import com.doozez.doozez.utils.PaymentMethodStatus
import com.doozez.doozez.utils.PaymentMethodType

class PaymentMethodsAdapter
(
    private val values: MutableList<PaymentDetailResp>,
    private val listener: PaymentMethodItemListener
)
    : RecyclerView.Adapter<PaymentMethodsAdapter.ViewHolder>() {

    private var selectedItem: PaymentDetailResp? = null

    fun getSelectedItem(): PaymentDetailResp? {
        return selectedItem
    }

    fun addItems(items: List<PaymentDetailResp>) {
        with(values) {
            clear()
            addAll(items)
            notifyDataSetChanged()
        }
    }

    fun addItem(item: PaymentDetailResp) {
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
//        holder.type.text = PaymentType.getPaymentName(item.type)
        holder.type.text = PaymentMethodType.getPaymentName("DIRECT_DEBIT")
        holder.status.text = PaymentMethodStatus.getPaymentStatus(item.status)
        holder.container.setOnClickListener {
            listener.paymentMethodClicked(item)
        }
    }

    override fun getItemCount(): Int = values.size

    private fun selectedItemChanged(item: PaymentDetailResp) {
        selectedItem = item
    }

    inner class ViewHolder(_binding: FragmentPaymentMethodsItemBinding) : RecyclerView.ViewHolder(_binding.root) {
        private val binding: FragmentPaymentMethodsItemBinding = _binding
        val container: CardView = binding.paymentMethodsItemContainer
        val name: TextView = binding.paymentMethodsItemName
        val type: TextView = binding.paymentMethodsItemType
        val status: TextView = binding.paymentMethodsItemStatus
    }
}