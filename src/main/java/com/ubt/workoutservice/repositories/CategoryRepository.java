package com.ubt.workoutservice.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ubt.workoutservice.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAllByEnabled(boolean enabled);

}
