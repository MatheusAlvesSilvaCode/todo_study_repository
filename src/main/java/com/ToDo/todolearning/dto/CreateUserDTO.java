package com.ToDo.todolearning.dto;

import java.util.UUID;
//TODO: Verificar nomes de classes e etc.. talvez mudar
public record CreateUserDTO(
        String name,
        String email,
        String phone,
        Boolean notifyBySms,
        Boolean notifyByEmail
) {}
