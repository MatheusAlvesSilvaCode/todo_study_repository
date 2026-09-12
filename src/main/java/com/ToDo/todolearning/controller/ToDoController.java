package com.ToDo.todolearning.controller;

import com.ToDo.todolearning.dto.CreateTaskDTO;
import com.ToDo.todolearning.dto.CreateUserDTO;
import com.ToDo.todolearning.entity.TaskEntity;
import com.ToDo.todolearning.entity.UserEntity;
import com.ToDo.todolearning.repository.TaskRepository;
import com.ToDo.todolearning.service.TaskService;
import com.ToDo.todolearning.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.Task;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("todo/api")
public class ToDoController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;
    // TODO: Organizar urls de acordo com boas praticas
    @PostMapping("/createtask")
    public TaskEntity createTask(@RequestBody CreateTaskDTO dto) {
        return this.taskService.createTask(dto);
    }

    @PostMapping("/user")
    public UserEntity createUser(@RequestBody CreateUserDTO dto) { return  this.userService.CreateUser(dto);}

    @GetMapping("/listall")
    public List<TaskEntity> findAll() {
    return this.taskService.findAll();
    }
}

