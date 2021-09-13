package com.ubt.workoutservice.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ubt.workoutservice.entities.PlanProgram;

@Repository
public interface PlanProgramRepository extends JpaRepository<PlanProgram, Long> {

    List<PlanProgram> findAllByPersonIdAndEnabled(Long personId, boolean enabled);

    @Query("select p.personId from PlanProgram p where p.enabled = true")
    List<Long> findAllPersonsWithPlanProgramEnabled();
}
