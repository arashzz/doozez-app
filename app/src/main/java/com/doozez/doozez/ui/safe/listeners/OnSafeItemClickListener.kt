package com.doozez.doozez.ui.safe.listeners

import com.doozez.doozez.api.safe.SafeDetailResponse

interface OnSafeItemClickListener {
    fun safeItemClicked(item: SafeDetailResponse)
}