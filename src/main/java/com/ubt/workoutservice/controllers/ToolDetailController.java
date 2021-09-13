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
import com.ubt.workoutservice.entities.dtos.ToolDetailDTO;
import com.ubt.workoutservice.services.ToolDetailService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/toolDetails")
public class ToolDetailController {

	@Autowired
	private ToolDetailService toolDetailService;

	@GetMapping("/all/{id}")
	public ResponseEntity toolDetails(@PathVariable Long id) {
		return ResponseEntity.ok(toolDetailService.prepareToolDetailDTOList(toolDetailService.getAllByTool(id)));
	}

	@GetMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity getById(@PathVariable Long id) {
		return ResponseEntity.ok(toolDetailService.prepareToolDetailDTO(toolDetailService.getById(id).getId()));
	}

	@PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity addToolDetail(@RequestBody ToolDetailDTO toolDetailDTO) {
		try {
			boolean created = toolDetailService.save(toolDetailDTO);
			if(created) {
				return ResponseEntity.ok(toolDetailDTO);
			}
		}
		catch (DatabaseException ex) {
			log.error("something went wrong while adding new tool detail! ex: {}", ex.getMessage());
		}

		return ResponseEntity.status(HttpStatus.IM_USED).build();
	}

	@GetMapping(value = "/edit/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity editToolDetail(@PathVariable Long id)
	{
		ToolDetailDTO toolDTO = toolDetailService.prepareToolDetailDTO(id);
		if (toolDTO != null) {
			return ResponseEntity.ok(toolDTO);
		}
		return ResponseEntity.noContent().build();
	}

	@PutMapping(value = "/edit", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity editToolDetail(@RequestBody ToolDetailDTO toolDetailDTO) {
		try {
			boolean updated = toolDetailService.update(toolDetailDTO);
			if(updated) {
				return ResponseEntity.ok(toolDetailDTO);
			}
		}
		catch (DatabaseException ex) {
			log.error("something went wrong while updating existing tool detail! ex: {}", ex.getMessage());
		}

		return ResponseEntity.status(HttpStatus.IM_USED).build();
	}

	@GetMapping("/delete/{id}")
	public ResponseEntity deleteToolDetail(@PathVariable Long id) {
		boolean deleted = toolDetailService.delete(id);
		if(deleted) {
			return ResponseEntity.ok().build();
		}
		else {
			return ResponseEntity.noContent().build();
		}
	}
}
