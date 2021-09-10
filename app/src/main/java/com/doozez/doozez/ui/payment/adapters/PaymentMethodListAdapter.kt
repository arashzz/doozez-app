package com.doozez.doozez.ui.payment.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.doozez.doozez.api.paymentMethod.PaymentMethodDetailResp
import com.doozez.doozez.databinding.FragmentPaymentMethodListItemBinding
import com.doozez.doozez.ui.payment.listeners.PaymentMethodItemListener
import com.doozez.doozez.utils.PaymentMethodType

class PaymentMethodListAdapter
    (private val values: MutableList<PaymentMethodDetailResp>,
     private val listener: PaymentMethodItemListener)
    : RecyclerView.Adapter<PaymentMethodListAdapter.ViewHolder>() {

    private var selectedItemIndex = -1

    fun addItems(items: List<PaymentMethodDetailResp>) {
        with(values) {
            clear()
            addAll(items)
        }
        notifyDataSetChanged()
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
        holder.type.text = PaymentMethodType.DIRECT_DEBIT.name
        holder.selected.isChecked = (position == selectedItemIndex)
        holder.selected.setOnClickListener {
            selectedItemIndex = holder.adapterPosition
            listener.paymentMethodClicked(values[selectedItemIndex])
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(_binding: FragmentPaymentMethodListItemBinding) : RecyclerView.ViewHolder(_binding.root) {
        private val binding: FragmentPaymentMethodListItemBinding = _binding
        val container: RelativeLayout = binding.paymentListItemContainer
        val name: TextView = binding.paymentMethodListItemName
        val type: TextView = binding.paymentMethodListItemType
        val selected: RadioButton = binding.paymentListItemSelected
    }
}