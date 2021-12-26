package com.doozez.doozez.ui.view

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import com.doozez.doozez.R
import com.doozez.doozez.enums.InvitationStatus
import com.doozez.doozez.enums.PaymentMethodStatus
import com.doozez.doozez.enums.SafeStatus
import com.google.android.material.shape.RoundedCornerTreatment


class PaymentMethodStatusCustomView
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0,
): androidx.appcompat.widget.AppCompatTextView(context, attrs, defStyleAttr) {

    init {
        setBackgroundResource(R.drawable.shape_status_tv)
    }

    fun changeStatus(value: PaymentMethodStatus) {
        text = value.description
        when(value) {
            PaymentMethodStatus.EXTERNALLY_ACTIVATED ->
                redraw(R.color.status_green_background, R.color.status_green_text)
            PaymentMethodStatus.PENDING_EXTERNAL_APPROVAL, PaymentMethodStatus.EXTERNALLY_CREATED,
            PaymentMethodStatus.EXTERNALLY_SUBMITTED, PaymentMethodStatus.EXTERNAL_APPROVAL_SUCCESSFUL ->
                redraw(R.color.status_yellow_background, R.color.status_yellow_text)
            PaymentMethodStatus.UNKNOWN, PaymentMethodStatus.EXTERNAL_APPROVAL_FAILED ->
                redraw(R.color.status_red_background, R.color.status_red_text)
        }
    }

    private fun redraw(bgColorId: Int, txtColorId: Int) {
        val drawable = background as GradientDrawable
        drawable.setColor(context.getColor(bgColorId))
        setTextColor(context.getColor(txtColorId))
        invalidate()
        requestLayout()
    }

}