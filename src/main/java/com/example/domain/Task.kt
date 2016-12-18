package com.example.domain

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "tasks")
data class Task(
        var title: String? = null,
        var description: String? = null,
        @Enumerated(EnumType.STRING) var status: Status? = Task.Status.OPEN
) {
    inner class IllegalStatusException : RuntimeException {
        constructor() : super() {
        }

        constructor(message: String) : super(message) {
        }
    }

    @Id
    @GeneratedValue
    var id: Int? = null

    constructor(id: Int, title: String, description: String, status: Status? = Task.Status.OPEN) : this(title, description, status) {
        this.id = id
    }

    enum class Status {
        OPEN, CLOSE, CANCEL;


        companion object {

            private val statusMap = HashMap<String, Status>()

            init {
                for (status in Status.values()) {
                    statusMap.put(status.name, status)
                }
            }

            fun getStatus(status: String?): Status? {
                if (status == null) {
                    return null
                }

                return statusMap[status]
            }
        }
    }
}
