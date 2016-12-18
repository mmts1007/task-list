package com.example.form

import com.example.domain.Task
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

data class TaskForm(
        @field:NotNull @field:Size(max = 32) var title: String? = null,
        @field:Size(max = 255) var description: String? = null,
        @field:NotNull @field:Pattern(regexp = "OPEN|CLOSE|CANCEL") var status: String? = null
) {
    fun getStatusType(): Task.Status? {
        return Task.Status.getStatus(status)
    }
}
