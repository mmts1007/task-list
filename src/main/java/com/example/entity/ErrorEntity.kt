package com.example.entity

import org.springframework.http.HttpStatus
import java.util.*

data class ErrorEntity(val status: HttpStatus) {

    val details: MutableList<String> = ArrayList<String>()

    fun getStatus(): Int? {
        return status.value()
    }

    val message: String?
        get() = status.reasonPhrase

    fun addDetail(message: String) {
        details.add(message)
    }
}
