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
import com.ubt.workoutservice.entities.dtos.ToolDTO;
import com.ubt.workoutservice.services.ToolService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/tools")
public class ToolController {

	@Autowired
	private ToolService toolService;

	@GetMapping("/all")
	public ResponseEntity tools() {
		return ResponseEntity.ok(toolService.prepareToolDTOList(toolService.getAll()));
	}

	@GetMapping("/enabled")
	public ResponseEntity enabledTools() {
		return ResponseEntity.ok(toolService.prepareToolDTOList(toolService.getAllEnabled()));
	}

	@GetMapping("/disabled")
	public ResponseEntity disabledTools() {
		return ResponseEntity.ok(toolService.prepareToolDTOList(toolService.getAllDisabled()));
	}

	@GetMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity getById(@PathVariable Long id) {
		return ResponseEntity.ok(toolService.prepareToolDTO(toolService.getById(id).getId()));
	}

	@PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity addTool(@RequestBody ToolDTO toolDTO) {
		try {
			boolean created = toolService.save(toolDTO);
			if(created) {
				return ResponseEntity.ok(toolDTO);
			}
		}
		catch (DatabaseException ex) {
			log.error("something went wrong while adding new tool! ex: {}", ex.getMessage());
		}

		return ResponseEntity.status(HttpStatus.IM_USED).build();
	}

	@GetMapping(value = "/edit/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity editTool(@PathVariable Long id) {
		ToolDTO toolDTO = toolService.prepareToolDTO(id);
		if (toolDTO != null) {
			return ResponseEntity.ok(toolDTO);
		}
		return ResponseEntity.noContent().build();
	}

	@PutMapping(value = "/edit", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity editTool(@RequestBody ToolDTO toolDTO) {
		try {
			boolean updated = toolService.update(toolDTO);
			if(updated) {
				return ResponseEntity.ok(toolDTO);
			}
		}
		catch (DatabaseException ex) {
			log.error("something went wrong while updating existing tool! ex: {}", ex.getMessage());
		}

		return ResponseEntity.status(HttpStatus.IM_USED).build();
	}

	@GetMapping("/disable/{id}")
	public ResponseEntity disableTool(@PathVariable Long id) {
		boolean disabled = toolService.disable(id);
		if(disabled) {
			return ResponseEntity.ok().build();
		}
		else {
			return ResponseEntity.noContent().build();
		}
	}

	@GetMapping("/enable/{id}")
	public ResponseEntity enableTool(@PathVariable Long id) {
		boolean enabled = toolService.enable(id);
		if(enabled) {
			return ResponseEntity.ok().build();
		}
		else {
			return ResponseEntity.noContent().build();
		}
	}
}
