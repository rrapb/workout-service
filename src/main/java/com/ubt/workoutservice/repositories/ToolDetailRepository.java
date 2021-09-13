package com.ubt.workoutservice.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ubt.workoutservice.entities.Tool;
import com.ubt.workoutservice.entities.ToolDetail;

@Repository
public interface ToolDetailRepository extends JpaRepository<ToolDetail, Long> {

    List<ToolDetail> findAllByTool(Tool tool);
}
