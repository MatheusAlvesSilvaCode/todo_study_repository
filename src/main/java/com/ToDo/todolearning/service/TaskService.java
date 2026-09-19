package com.ToDo.todolearning.service;

import com.ToDo.todolearning.dto.TaskDTO;
import com.ToDo.todolearning.entity.TaskEntity;
import com.ToDo.todolearning.enums.statusTaskEnum;
import com.ToDo.todolearning.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class TaskService {

    TaskRepository taskRepository;


    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskEntity createTask(TaskDTO dto) {
       TaskEntity task = new TaskEntity();
       task.setStatus(dto.status());
       task.setTitle(dto.title());
       task.setUserId(dto.userId());
       task.setPriority(dto.priority());
       task.setDeadline(dto.deadline());
       task.setDescription(dto.description());
       task = this.taskRepository.saveAndFlush(task);
       log.info("Task created successfully. Title: {} | User ID: {}", task.getTitle(), task.getUserId());
       return task;
    }

    public TaskEntity updateTask(UUID id, TaskDTO dto) {
        TaskEntity existingTask = this.taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found ID: " + id));

        if (dto.title() != null) {
            existingTask.setTitle(dto.title());
            log.info("Title updated successfully. Title: {}", existingTask.getTitle());
        }
        if (dto.description() != null) {
            existingTask.setDescription(dto.description());
            log.info("Description updated successfully. Description: {}", existingTask.getDescription());
        }
        if (dto.status() != null) {
            existingTask.setStatus(dto.status());
            log.info("Status updated successfully. Status: {}", existingTask.getStatus());
        }
        if (dto.priority() != null) {
            existingTask.setPriority(dto.priority());
            log.info("Priority updated successfully. Priority: {}", existingTask.getPriority());
        }
        if (dto.deadline() != null) {
            existingTask.setDeadline(dto.deadline());
            log.info("Deadline updated successfully. Deadline: {}", existingTask.getDeadline());
        }
        this.findAll();
        return this.taskRepository.save(existingTask);
    }

    public TaskEntity taskDone(UUID id) {
        TaskEntity existingTask = this.taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found ID: " + id));

        if(!statusTaskEnum.COMPLETED.name().equals(existingTask.getStatus())) {
            existingTask.setStatus(String.valueOf(statusTaskEnum.COMPLETED));
            log.info("Task status updated to COMPLETED. ID: {}", id);
            return this.taskRepository.save(existingTask);
        }
        log.info("Task ID: {} is already COMPLETED. No update needed.", id);
        return existingTask;
    }

    public void deleteTask(UUID id) {
        TaskEntity existingTask = this.taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found ID: " + id));

        this.taskRepository.delete(existingTask);
        log.info("Task deleted successfully. ID: {} Name Task :: {} ", id, existingTask.getTitle());
    }

    public List<TaskEntity> listAllDoneTasks(){
        log.info("Listing all done tasks :: {}", this.taskRepository.findAll());
        return this.taskRepository.findByStatus(statusTaskEnum.COMPLETED.name());
    }

    public List<TaskEntity> listAllProgressTasks(){
        log.info("Listing all done tasks :: {}", this.taskRepository.findAll());
        return this.taskRepository.findByStatus(statusTaskEnum.PENDING.name());
    }

    public List<TaskEntity> listAllNotDoneTasks(){
        log.info("Listing all done tasks :: {}", this.taskRepository.findAll());
        return this.taskRepository.findByStatus(statusTaskEnum.CANCELLED.name());
    }


    public List<TaskEntity> findAll() {
        List<TaskEntity> taskslist = this.taskRepository.findAll();
        log.info("Task find all :: {}", taskslist.size() );
        return this.taskRepository.findAll();
    }
}
