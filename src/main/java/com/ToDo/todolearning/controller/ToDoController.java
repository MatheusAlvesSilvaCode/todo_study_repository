package com.ToDo.todolearning.controller;

import com.ToDo.todolearning.dto.CreateTaskDTO;
import com.ToDo.todolearning.entity.TaskEntity;
import com.ToDo.todolearning.repository.TaskRepository;
import com.ToDo.todolearning.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("todo/api/task")
public class ToDoController {

    private TaskService taskService;

    @GetMapping("/{id}")
    public String getTask(@PathVariable String id){
        return "Buscando tarefa com ID: " + id;
    };

    @PostMapping
    public CreateTaskDTO createTask(@RequestBody CreateTaskDTO dto){
       return dto;
    }
}
