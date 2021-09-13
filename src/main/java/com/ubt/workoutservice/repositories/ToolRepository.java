package com.ubt.workoutservice.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ubt.workoutservice.entities.Tool;

@Repository
public interface ToolRepository extends JpaRepository<Tool, Long> {

    List<Tool> findAllByEnabled(boolean enabled);
}
