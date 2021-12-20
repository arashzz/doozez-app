package com.doozez.doozez.ui.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.LinearLayout
import com.doozez.doozez.R
import kotlinx.android.synthetic.main.summary_card_view.view.*

class SummaryCardView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    init {
        inflate(context, R.layout.summary_card_view, this)
        attrs?.let {
            val styledAttributes =
                context.obtainStyledAttributes(it, R.styleable.SummaryCardView, 0, 0)
            val textValue = styledAttributes.getString(R.styleable.SummaryCardView_summaryText)
            val iconValue = styledAttributes.getDrawable(R.styleable.SummaryCardView_summaryIcon)

            setIcon(iconValue)
            setText(textValue)
        }
    }
    fun setIcon(value: Drawable?){
        img_summary_icon.setImageDrawable(value)
    }
    fun setText(value: String?) {
        if(!value.isNullOrBlank()){
            text_summary.text=value.toString()
        }
    }

}