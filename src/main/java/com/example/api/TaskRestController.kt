package com.example.api

import com.example.api.exception.NotFoundException
import com.example.domain.Task
import com.example.form.TaskForm
import com.example.service.TaskService
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/tasks")
class TaskRestController {
    @Autowired
    lateinit var taskService: TaskService

    @GetMapping
    internal fun getTasks(): List<Task> {
        return taskService.findAll()
    }

    @GetMapping(path = arrayOf("{id}"))
    internal fun getTask(@PathVariable id: Int): Task {
        return taskService.findOne(id) ?: throw NotFoundException()
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    internal fun postTasks(@Validated @RequestBody taskForm: TaskForm): Task {
        val taskDomain = Task()
        BeanUtils.copyProperties(taskForm, taskDomain)
        return taskService.create(taskDomain)
    }

    @PutMapping(path = arrayOf("{id}"))
    internal fun putTask(@PathVariable id: Int, @Validated @RequestBody taskForm: TaskForm): Task {
        if (!taskService.exists(id)) {
            throw NotFoundException()
        }

        val taskDomain = Task()
        BeanUtils.copyProperties(taskForm, taskDomain)
        taskDomain.id = id
        taskDomain.status = taskForm.getStatusType()

        return taskService.update(taskDomain)
    }

    @DeleteMapping(path = arrayOf("{id}"))
    @ResponseStatus(HttpStatus.NO_CONTENT)
    internal fun deleteTask(@PathVariable id: Int) {
        try {
            taskService.delete(id)
        } catch (e: com.example.service.exception.NotFoundException) {
            throw NotFoundException()
        }

    }
}
