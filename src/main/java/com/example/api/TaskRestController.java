package com.example.api;

import com.example.api.exception.NotFoundException;
import com.example.domain.Task;
import com.example.form.TaskForm;
import com.example.service.TaskService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/tasks")
public class TaskRestController {
    @Autowired
    TaskService taskService;

    @GetMapping
    List<Task> getTasks() {
        List<Task> tasks = taskService.findAll();
        return tasks;
    }

    @GetMapping(path = "{id}")
    Task getTask(@PathVariable Integer id) {
        Task task = taskService.findOne(id);

        if (task == null) {
            throw new NotFoundException();
        }

        return task;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Task postTasks(@Validated @RequestBody TaskForm taskForm) {
        Task taskDomain = new Task();
        BeanUtils.copyProperties(taskForm, taskDomain);
        return taskService.create(taskDomain);
    }

    @PutMapping(path = "{id}")
    Task putTask(@PathVariable Integer id, @Validated @RequestBody TaskForm taskForm) {
        if (!taskService.exists(id)) {
            throw new NotFoundException();
        }

        Task taskDomain = new Task();
        BeanUtils.copyProperties(taskForm, taskDomain);
        taskDomain.setId(id);
        taskDomain.setStatus(taskForm.getStatusType());

        return taskService.update(taskDomain);
    }

    @DeleteMapping(path = "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteTask(@PathVariable Integer id) {
        try {
            taskService.delete(id);
        } catch (com.example.service.exception.NotFoundException e) {
            throw new NotFoundException();
        }
    }
}
