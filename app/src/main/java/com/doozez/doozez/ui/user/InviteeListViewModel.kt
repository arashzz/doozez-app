package com.doozez.doozez.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doozez.doozez.api.user.UserDetailResp

class InviteeListViewModel: ViewModel() {
    val listLiveData = MutableLiveData<List<UserDetailResp>>()
    private val list = ArrayList<UserDetailResp>()

    fun add(invitee: UserDetailResp) {
        list.add(invitee)
        listLiveData.value = list
    }

    fun get(inviteeID: Int): UserDetailResp? {
        return list.find { it.id == inviteeID }
    }

    fun remove(inviteeID: Int) {
        list.removeIf {it.id == inviteeID}
        listLiveData.value = list
    }
}