package com.example

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class TaskListApplication {}

fun main(args: Array<String>) {
    SpringApplication.run(TaskListApplication::class.java, *args)
}
