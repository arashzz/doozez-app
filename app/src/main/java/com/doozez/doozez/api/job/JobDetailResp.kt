package com.doozez.doozez.api.job

import com.doozez.doozez.utils.JobStatus
import com.doozez.doozez.utils.TaskStatus
import com.google.gson.annotations.SerializedName

class JobDetailResp (
    @SerializedName("id")
    var id: Int,

    @SerializedName("status")
    var status: JobStatus,

    @SerializedName("jobs_tasks")
    var tasks: List<TaskDetail>
    )