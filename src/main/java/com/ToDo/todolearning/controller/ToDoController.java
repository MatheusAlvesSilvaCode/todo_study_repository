package com.ToDo.todolearning.controller;

import com.ToDo.todolearning.dto.TaskDTO;
import com.ToDo.todolearning.dto.UserDTO;
import com.ToDo.todolearning.entity.TaskEntity;
import com.ToDo.todolearning.entity.UserEntity;
import com.ToDo.todolearning.service.TaskService;
import com.ToDo.todolearning.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("todo/api")
public class ToDoController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;
    // TODO: Organizar urls de acordo com boas praticas
    @PostMapping("/createtask")
    public TaskEntity createTask(@RequestBody TaskDTO dto) {
        return this.taskService.createTask(dto);
    }

    @PostMapping("/updatetask/{id}")
    public TaskEntity updateTask(@PathVariable UUID id, @RequestBody TaskDTO dto) {
        return this.taskService.updateTask(id, dto);
    }

    @PostMapping("/taskdone/{id}")
    public TaskEntity taskDone(@PathVariable UUID id) {
        return this.taskService.taskDone(id);
    }

    @PostMapping("/deletetask/{id}")
    public TaskEntity deleteTask(@PathVariable UUID id) {
        this.taskService.deleteTask(id);
        return null;
    }

    //TODO: CONCERTAR ISSO, ESTÁ DANDO ERRO NO POSTMAN DE 500. FALTA CONCERTAR O PARAMETRO.
    @PostMapping("/alldone")
    public TaskEntity listAllDoneTasks(@PathVariable UUID id, @RequestBody TaskDTO dto) {
        return (TaskEntity) this.taskService.listAllDoneTasks();
    }

    @PostMapping("/user")
    public UserEntity createUser(@RequestBody UserDTO dto) { return  this.userService.CreateUser(dto);}

    @GetMapping("/listall")
    public List<TaskEntity> findAll() {
    return this.taskService.findAll();
    }
}

