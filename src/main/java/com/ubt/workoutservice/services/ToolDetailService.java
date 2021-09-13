package com.ubt.workoutservice.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ubt.workoutservice.configurations.exceptions.DatabaseException;
import com.ubt.workoutservice.entities.Tool;
import com.ubt.workoutservice.entities.ToolDetail;
import com.ubt.workoutservice.entities.dtos.ToolDetailDTO;
import com.ubt.workoutservice.repositories.ToolDetailRepository;

@Service
public class ToolDetailService {

    @Autowired
    private ToolDetailRepository toolDetailRepository;

    @Autowired
    private ToolService toolService;

    public List<ToolDetail> getAllByTool(Long toolId){

        Tool tool = toolService.getById(toolId);
        return toolDetailRepository.findAllByTool(tool);
    }

    public ToolDetail getById(Long id) {
        return toolDetailRepository.findById(id).orElse(null);
    }

    public boolean save(ToolDetailDTO toolDetailDTO) throws DatabaseException {

        Tool tool = toolService.getById(toolDetailDTO.getToolId());

        ToolDetail toolDetail = ToolDetail.builder()
                .name(toolDetailDTO.getName())
                .value(toolDetailDTO.getValue())
                .tool(tool).build();

        if(StringUtils.isNotBlank(toolDetail.getName()) && StringUtils.isNotBlank(toolDetail.getValue())) {
            try {
                toolDetailRepository.save(toolDetail);
                return true;
            }catch (Exception e) {
                throw new DatabaseException("duplicate");
            }
        }
        else {
            return false;
        }
    }

    public boolean update(ToolDetailDTO toolDetailDTO) throws DatabaseException {

        ToolDetail toolDetail = getById(toolDetailDTO.getId());
        Tool tool = toolService.getById(toolDetailDTO.getToolId());

        ToolDetail tempToolDetail = ToolDetail.builder()
                .id(toolDetail.getId())
                .name(toolDetailDTO.getName())
                .value(toolDetailDTO.getValue())
                .tool(tool).build();

        if(StringUtils.isNotBlank(tempToolDetail.getName()) && StringUtils.isNotBlank(tempToolDetail.getValue())
                && toolDetailRepository.existsById(tempToolDetail.getId())) {
            try {
                toolDetailRepository.save(tempToolDetail);
                return true;
            }catch (Exception e) {
                throw new DatabaseException("duplicate");
            }
        }
        else {
            return false;
        }
    }

    public boolean delete(Long id) {

        if(toolDetailRepository.existsById(id)){
            toolDetailRepository.delete(getById(id));
            return true;
        }
        else {
            return false;
        }
    }

    public ToolDetailDTO prepareToolDetailDTO(final Long id) {

        ToolDetail toolDetail = getById(id);

        return ToolDetailDTO.builder()
                .id(toolDetail.getId())
                .name(toolDetail.getName())
                .value(toolDetail.getValue())
                .toolId(toolDetail.getTool().getId())
                .toolName(toolDetail.getTool().getName())
                .build();
    }

    public List<ToolDetailDTO> prepareToolDetailDTOList(List<ToolDetail> toolDetails) {
        List<ToolDetailDTO> toolDetailsDTOS = new ArrayList<>();
        for(ToolDetail toolDetail : toolDetails) {
            toolDetailsDTOS.add(prepareToolDetailDTO(toolDetail.getId()));
        }
        return toolDetailsDTOS;
    }
}
