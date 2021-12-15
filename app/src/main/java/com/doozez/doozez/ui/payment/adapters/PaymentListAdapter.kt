package com.doozez.doozez.ui.payment.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.doozez.doozez.api.invitation.InviteDetailResp
import com.doozez.doozez.api.payment.PaymentDetailResp
import com.doozez.doozez.databinding.FragmentInvitationItemBinding
import com.doozez.doozez.databinding.FragmentPaymentListItemBinding
import com.doozez.doozez.ui.invitation.listeners.OnInviteActionClickListener
import com.doozez.doozez.enums.InvitationStatus
import com.doozez.doozez.enums.PaymentStatus
import com.doozez.doozez.ui.payment.PaymentListFragment
import com.doozez.doozez.ui.payment.interfaces.PaymentListener
import com.doozez.doozez.utils.Common
import com.google.android.material.button.MaterialButton
import java.lang.Exception
import java.time.DateTimeException
import java.time.format.DateTimeParseException
import kotlin.math.abs

class PaymentListAdapter(
    private val values: MutableList<PaymentDetailResp>,
    private val listener: PaymentListener
) : RecyclerView.Adapter<PaymentListAdapter.PaymentViewHolder>() {

    fun addItems(items: List<PaymentDetailResp>) {
        with(values) {
            clear()
            addAll(items)
        }.also { notifyDataSetChanged() }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder {

        return PaymentViewHolder(
            FragmentPaymentListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
        val item = values[position]
        val status = PaymentStatus.fromCode(item.status)
        holder.status.text = status.description
        holder.amount.text = item.amount.toString()
        val chargeDateStr = getChargedDateText(item.chargeDate)
        holder.date.text = chargeDateStr
        holder.container.setOnClickListener {
            listener.onClickListener(item)
        }
    }

    override fun getItemCount(): Int = values.size

    private fun getChargedDateText(dateStr: String): String {
        val days = Common.calculateDays(dateStr)
        var result = Common.getDate(dateStr).toString()
        if (days == 0L) {
            result = "Today"
        } else if (days < 0L && days > -7L) {
            result = "In ${abs(days)} days"
        }
        return result
    }

    inner class PaymentViewHolder(binding: FragmentPaymentListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val container: CardView = binding.inviteDetailContainer
        val amount: TextView = binding.paymentAmount
        val status: TextView = binding.paymentStatus
        val date: TextView = binding.paymentDate
    }

    companion object {
        private const val TAG = "PaymentListAdapter"
    }
}