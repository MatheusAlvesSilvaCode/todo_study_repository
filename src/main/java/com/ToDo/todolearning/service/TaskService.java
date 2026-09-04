package com.ToDo.todolearning.service;

import com.ToDo.todolearning.repository.TaskRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

}
