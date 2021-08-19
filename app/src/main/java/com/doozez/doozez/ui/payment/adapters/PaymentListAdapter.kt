package com.doozez.doozez.ui.payment.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.doozez.doozez.api.payments.PaymentDetailResp
import com.doozez.doozez.databinding.FragmentPaymentListItemBinding

class PaymentListAdapter(private val values: MutableList<PaymentDetailResp>)
    : RecyclerView.Adapter<PaymentListAdapter.ViewHolder>() {

    private var selectedItem: PaymentDetailResp? = null

    fun getSelectedItem(): PaymentDetailResp? {
        return selectedItem
    }

    fun addItems(items: List<PaymentDetailResp>) {
        with(values) {
            clear()
            addAll(items)
        }
        notifyDataSetChanged()
    }

    fun addItem(item: PaymentDetailResp) {
        values.add(item)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentListAdapter.ViewHolder {

        return ViewHolder(
            FragmentPaymentListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val item = values[position]
//        holder.paymentNumber.text = item.cardNumber
//        if (selectedItem == null && item.isDefault) {
//            selectedItem = item
//        }
//        holder.selected.isChecked = item.id == selectedItem?.id
//        holder.container.setOnClickListener {
//            selectedItemChanged(item)
//        }
//        holder.selected.setOnClickListener {
//            selectedItemChanged(item)
//        }
    }

    override fun getItemCount(): Int = values.size

    private fun selectedItemChanged(item: PaymentDetailResp) {
        selectedItem = item
        notifyDataSetChanged()
    }

    inner class ViewHolder(_binding: FragmentPaymentListItemBinding) : RecyclerView.ViewHolder(_binding.root) {
        private val binding: FragmentPaymentListItemBinding = _binding
//        val container: RelativeLayout = binding.paymentListItemContainer
//        val paymentNumber: TextView = binding.paymentListItemNumber
//        val selected: RadioButton = binding.paymentListItemSelected
    }
}