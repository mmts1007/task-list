package com.example.service;

import com.example.domain.Task;
import com.example.repository.TaskRepository;
import com.example.service.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TaskService {
    @Autowired
    TaskRepository taskRepository;

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public Task findOne(Integer id) {
        return taskRepository.findOne(id);
    }

    public Task create(Task task) {
        return taskRepository.save(task);
    }

    public Task update(Task task) {
        return taskRepository.save(task);
    }

    public void delete(Integer id) {
        if (!this.exists(id)) {
            throw new NotFoundException();
        }

        taskRepository.delete(id);
    }

    public boolean exists(Integer id) {
        return taskRepository.exists(id);
    }
}
