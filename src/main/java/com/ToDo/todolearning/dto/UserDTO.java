package com.ToDo.todolearning.dto;

//TODO: Verificar nomes de classes e etc.. talvez mudar
public record UserDTO(
        String name,
        String email,
        String phone,
        Boolean notifyBySms,
        Boolean notifyByEmail
) {}
