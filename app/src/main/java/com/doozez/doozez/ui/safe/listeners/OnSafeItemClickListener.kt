package com.doozez.doozez.ui.safe.listeners

import com.doozez.doozez.api.safe.SafeDetailResp

interface OnSafeItemClickListener {
    fun safeItemClicked(item: SafeDetailResp)
}