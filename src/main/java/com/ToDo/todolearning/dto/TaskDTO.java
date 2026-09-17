package com.ToDo.todolearning.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record TaskDTO(
        UUID userId,
        UUID taskId,
        String status,
        String title,
        String description,
        OffsetDateTime deadline,
        String priority
) {}
