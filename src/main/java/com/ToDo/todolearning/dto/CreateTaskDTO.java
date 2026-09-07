package com.ToDo.todolearning.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record CreateTaskDTO(
        UUID userId,
        String status,
        String title,
        String description,
        OffsetDateTime deadline,
        String priority
) {}
