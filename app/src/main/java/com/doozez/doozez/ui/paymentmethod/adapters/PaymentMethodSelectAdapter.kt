package com.doozez.doozez.ui.paymentmethod.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.doozez.doozez.api.paymentMethod.PaymentMethodDetailResp
import com.doozez.doozez.databinding.FragmentPaymentMethodSelectItemBinding
import com.doozez.doozez.ui.paymentmethod.listeners.PaymentMethodItemListener
import com.doozez.doozez.enums.PaymentMethodType

class PaymentMethodSelectAdapter
    (private val values: MutableList<PaymentMethodDetailResp>,
     private val listener: PaymentMethodItemListener,
     private val selectedId: Int)
    : RecyclerView.Adapter<PaymentMethodSelectAdapter.ViewHolder>() {

    private var selectedItemId = -1

    fun addItems(items: List<PaymentMethodDetailResp>) {
        with(values) {
            clear()
            addAll(items)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentMethodSelectAdapter.ViewHolder {
        selectedItemId = selectedId
        return ViewHolder(
            FragmentPaymentMethodSelectItemBinding.inflate(
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
        holder.selected.isChecked = (item.id == selectedItemId)
        holder.selected.setOnClickListener {
            selectedItemId = item.id
            listener.paymentMethodClicked(item)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(_binding: FragmentPaymentMethodSelectItemBinding) : RecyclerView.ViewHolder(_binding.root) {
        private val binding: FragmentPaymentMethodSelectItemBinding = _binding
        val container: RelativeLayout = binding.paymentListItemContainer
        val name: TextView = binding.paymentMethodListItemName
        val type: TextView = binding.paymentMethodListItemType
        val selected: RadioButton = binding.paymentListItemSelected
    }
}