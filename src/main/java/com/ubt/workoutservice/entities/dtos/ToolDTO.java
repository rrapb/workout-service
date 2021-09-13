package com.ubt.workoutservice.entities.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToolDTO {

    private Long id;
    private String name;
    private String description;
    private boolean enabled;
    private Long categoryId;
    private String categoryName;
}