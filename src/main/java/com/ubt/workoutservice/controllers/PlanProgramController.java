package com.ubt.workoutservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ubt.workoutservice.configurations.exceptions.DatabaseException;
import com.ubt.workoutservice.entities.dtos.PlanProgramDTO;
import com.ubt.workoutservice.services.PlanProgramService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/planPrograms")
public class PlanProgramController {

    @Autowired
    private PlanProgramService planProgramService;

    @GetMapping("/all")
    public ResponseEntity planPrograms(){
        return ResponseEntity.ok(planProgramService.preparePlanProgramDTOList(planProgramService.getAll()));
    }

    @GetMapping("/enabled")
    public ResponseEntity enabledPlanPrograms() {
        return ResponseEntity.ok(planProgramService.preparePlanProgramDTOList(planProgramService.getAllEnabled()));
    }

    @GetMapping("/disabled")
    public ResponseEntity disabledPlanPrograms() {
        return ResponseEntity.ok(planProgramService.preparePlanProgramDTOList(planProgramService.getAllDisabled()));
    }

    @GetMapping("/persons")
    public ResponseEntity personsWithEnabledPlanProgram(){
        return ResponseEntity.ok(planProgramService.getAllPersonsWithPlanProgramEnabled());
    }

    @GetMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getById(@PathVariable Long id) {
        return ResponseEntity.ok(planProgramService.preparePlanProgramDTO(planProgramService.getById(id).getId()));
    }

    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addPlanProgram(@RequestBody PlanProgramDTO planProgramDTO) {
        try {
            boolean created = planProgramService.save(planProgramDTO);
            if(created) {
                return ResponseEntity.ok(planProgramDTO);
            }
        }
        catch (DatabaseException ex) {
            log.error("something went wrong while adding new plan program! ex: {}", ex.getMessage());
        }

        return ResponseEntity.status(HttpStatus.IM_USED).build();
    }

    @GetMapping(value = "/edit/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity editPlanProgram(@PathVariable Long id) {
        PlanProgramDTO planProgramDTO = planProgramService.preparePlanProgramDTO(id);
        if (planProgramDTO != null) {
            return ResponseEntity.ok(planProgramDTO);
        }
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/edit", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity editPlanProgram(@RequestBody PlanProgramDTO planProgramDTO) {
        try {
            boolean updated = planProgramService.update(planProgramDTO);
            if(updated) {
                return ResponseEntity.ok(planProgramDTO);
            }
        }
        catch (DatabaseException ex) {
            log.error("something went wrong while updating existing plan program! ex: {}", ex.getMessage());
        }

        return ResponseEntity.status(HttpStatus.IM_USED).build();
    }

    @GetMapping("/disable/{id}")
    public ResponseEntity disablePlanProgram(@PathVariable Long id) {
        boolean disabled = planProgramService.disable(id);
        if(disabled) {
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/enable/{id}")
    public ResponseEntity enablePlanProgram(@PathVariable Long id) {
        boolean enabled = planProgramService.enable(id);
        if(enabled) {
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadPlanProgram(@RequestParam Long personId){

        byte[] pdfFile = planProgramService.download(personId);
        String fullName = planProgramService.getPlanProgramsByPersonIdAndEnabled(personId).get(0).getPersonFullName();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""
                        .concat(fullName.replace(" ", "_"))
                        .concat(".pdf\""))
                .body(pdfFile);
    }
}
