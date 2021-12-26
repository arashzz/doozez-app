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
import com.google.android.material.shape.RoundedCornerTreatment


class InvitationStatusCustomView
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0,
): androidx.appcompat.widget.AppCompatTextView(context, attrs, defStyleAttr) {

    init {
        setBackgroundResource(R.drawable.shape_status_tv)
    }

    fun changeStatus(value: InvitationStatus) {
        text = value.description
        when(value) {
            InvitationStatus.PENDING ->
                redraw(R.color.status_yellow_background, R.color.status_yellow_text)
            InvitationStatus.ACCEPTED ->
                redraw(R.color.status_green_background, R.color.status_green_text)
            InvitationStatus.DECLINED, InvitationStatus.CANCELLED, InvitationStatus.UNKNOWN ->
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