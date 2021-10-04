package com.doozez.doozez.api.job

import com.doozez.doozez.utils.TaskStatus
import com.google.gson.annotations.SerializedName

class TaskDetail (
    @SerializedName("id")
    var id: Int,

    @SerializedName("status")
    var status: TaskStatus,

    @SerializedName("task_type")
    var type: String
)