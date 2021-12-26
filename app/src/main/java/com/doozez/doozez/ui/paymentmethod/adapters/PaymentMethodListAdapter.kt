package com.doozez.doozez.ui.paymentmethod.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.doozez.doozez.R
import com.doozez.doozez.api.paymentMethod.PaymentMethodDetailResp
import com.doozez.doozez.databinding.FragmentPaymentMethodListItemBinding
import com.doozez.doozez.ui.paymentmethod.listeners.PaymentMethodItemListener
import com.doozez.doozez.enums.PaymentMethodStatus
import com.doozez.doozez.enums.PaymentMethodType
import com.doozez.doozez.ui.view.PaymentMethodStatusCustomView

class PaymentMethodListAdapter
(
    private val values: MutableList<PaymentMethodDetailResp>,
    private val listener: PaymentMethodItemListener
) : RecyclerView.Adapter<PaymentMethodListAdapter.ViewHolder>() {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentMethodListAdapter.ViewHolder {

        return ViewHolder(
            FragmentPaymentMethodListItemBinding.inflate(
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
        val status = PaymentMethodStatus.fromCode(item.status)
        holder.status.changeStatus(status)
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentPaymentMethodListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val container: RelativeLayout = binding.paymentMethodsItemContainer
        val name: TextView = binding.paymentMethodsItemName
        val type: TextView = binding.paymentMethodsItemType
        val status: PaymentMethodStatusCustomView = binding.paymentMethodsItemStatus
    }
}