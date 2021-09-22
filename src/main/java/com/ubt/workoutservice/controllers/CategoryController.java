package com.ubt.workoutservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ubt.workoutservice.configurations.exceptions.DatabaseException;
import com.ubt.workoutservice.entities.dtos.CategoryDTO;
import com.ubt.workoutservice.services.CategoryService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/categories")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@GetMapping("/all")
	public ResponseEntity categories() {
		return ResponseEntity.ok(categoryService.prepareCategoryDTOList(categoryService.getAll()));
	}

	@GetMapping("/enabled")
	public ResponseEntity enabledCategories() {
		return ResponseEntity.ok(categoryService.prepareCategoryDTOList(categoryService.getAllEnabled()));
	}

	@GetMapping("/disabled")
	public ResponseEntity disabledCategories() {
		return ResponseEntity.ok(categoryService.prepareCategoryDTOList(categoryService.getAllDisabled()));
	}

	@GetMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity getById(@PathVariable Long id) {
		return ResponseEntity.ok(categoryService.prepareCategoryDTO(categoryService.getById(id).getId()));
	}

	@PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity addCategory(@RequestBody CategoryDTO categoryDTO) {
		try {
			boolean created = categoryService.save(categoryDTO);
			if(created) {
				return ResponseEntity.ok(categoryDTO);
			}
		}
		catch (DatabaseException ex) {
			log.error("something went wrong while adding new category! ex: {}", ex.getMessage());
		}
		return ResponseEntity.status(HttpStatus.IM_USED).build();
	}

	@GetMapping(value = "/edit/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity editCategory(@PathVariable Long id) {
		CategoryDTO categoryDTO = categoryService.prepareCategoryDTO(id);
		if (categoryDTO != null) {
			return ResponseEntity.ok(categoryDTO);
		}
		return ResponseEntity.noContent().build();
	}

	@PutMapping(value = "/edit", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity editCategory(@RequestBody CategoryDTO categoryDTO) {
		try {
			boolean updated = categoryService.update(categoryDTO);
			if(updated) {
				return ResponseEntity.ok(categoryDTO);
			}
		}
		catch (DatabaseException ex) {
			log.error("something went wrong while updating existing category! ex: {}", ex.getMessage());
		}

		return ResponseEntity.status(HttpStatus.IM_USED).build();
	}

	@GetMapping("/disable/{id}")
	public ResponseEntity disableCategory(@PathVariable Long id) {
		boolean disabled = categoryService.disable(id);
		if(disabled) {
			return ResponseEntity.ok().build();
		}
		else {
			return ResponseEntity.noContent().build();
		}
	}

	@GetMapping("/enable/{id}")
	public ResponseEntity enableCategory(@PathVariable Long id) {
		boolean enabled = categoryService.enable(id);
		if(enabled) {
			return ResponseEntity.ok().build();
		}
		else {
			return ResponseEntity.noContent().build();
		}
	}
}
