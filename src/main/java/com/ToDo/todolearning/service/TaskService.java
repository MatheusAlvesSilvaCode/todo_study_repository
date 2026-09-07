package com.ToDo.todolearning.service;

import com.ToDo.todolearning.dto.CreateTaskDTO;
import com.ToDo.todolearning.entity.TaskEntity;
import com.ToDo.todolearning.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TaskService {

    TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskEntity createTask(CreateTaskDTO dto) {
       TaskEntity task = new TaskEntity();
       task.setStatus(dto.status());
       task.setTitle(dto.title());
       task.setDescription(dto.status());
       task.setUserId(dto.userId());
       task.setPriority(dto.priority());
       task.setDeadline(dto.deadline());
       task.setDescription(dto.description());
       task = this.taskRepository.saveAndFlush(task);
       return task;
    }

    public List<TaskEntity> findAll() {
        return this.taskRepository.findAll();
    }
}
