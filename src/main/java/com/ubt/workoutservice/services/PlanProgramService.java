package com.ubt.workoutservice.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.ubt.workoutservice.configurations.exceptions.DatabaseException;
import com.ubt.workoutservice.entities.Category;
import com.ubt.workoutservice.entities.PlanProgram;
import com.ubt.workoutservice.entities.dtos.PlanProgramDTO;
import com.ubt.workoutservice.repositories.PlanProgramRepository;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class PlanProgramService
{

    @Autowired
    private PlanProgramRepository planProgramRepository;

    @Autowired
    private CategoryService categoryService;

    public List<PlanProgram> getAll(){
        return planProgramRepository.findAll();
    }

    public List<PlanProgram> getAllEnabled(){
        return planProgramRepository.findAllByEnabled(true);
    }

    public List<PlanProgram> getAllDisabled(){
        return planProgramRepository.findAllByEnabled(false);
    }

    public List<Long> getAllPersonsWithPlanProgramEnabled(){
        return planProgramRepository.findAllPersonsWithPlanProgramEnabled();
    }

    public PlanProgram getById(Long id) {
        return planProgramRepository.findById(id).orElse(null);
    }

    public boolean save(PlanProgramDTO planProgramDTO) throws DatabaseException {
        Category category = categoryService.getById(planProgramDTO.getCategoryId());

        PlanProgram planProgram = PlanProgram.builder()
                .day(planProgramDTO.getDay())
                .personFullName(planProgramDTO.getPersonFullName())
                .personId(planProgramDTO.getPersonId())
                .category(category)
                .enabled(true).build();

        if(StringUtils.isNotBlank(planProgram.getDay())) {
            try {
                planProgramRepository.save(planProgram);
                return true;
            }catch (Exception e) {
                throw new DatabaseException("duplicate");
            }
        }
        else {
            return false;
        }
    }

    public boolean update(PlanProgramDTO planProgramDTO) throws DatabaseException {
        PlanProgram planProgram = getById(planProgramDTO.getId());
        Category category = categoryService.getById(planProgramDTO.getCategoryId());

        PlanProgram tempPlanProgram = PlanProgram.builder()
                .id(planProgram.getId())
                .day(planProgramDTO.getDay())
                .personFullName(planProgramDTO.getPersonFullName())
                .personId(planProgramDTO.getPersonId())
                .category(category)
                .enabled(true).build();

        if(StringUtils.isNotBlank(planProgram.getDay()) && planProgramRepository.existsById(tempPlanProgram.getId())) {
            try {
                planProgramRepository.save(tempPlanProgram);
                return true;
            }catch (Exception e) {
                throw new DatabaseException("duplicate");
            }
        }
        else {
            return false;
        }
    }

    public boolean disable(Long id) {
        PlanProgram planProgram = getById(id);
        if(planProgram.isEnabled()){
            planProgram.setEnabled(false);
            planProgramRepository.save(planProgram);
            return true;
        }
        else {
            return false;
        }
    }

    public boolean enable(Long id) {
        PlanProgram planProgram = getById(id);
        if(!planProgram.isEnabled()){
            planProgram.setEnabled(true);
            planProgramRepository.save(planProgram);
            return true;
        }
        else {
            return false;
        }
    }

    public List<PlanProgram> getPlanProgramsByPersonIdAndEnabled(Long personId) {
        return planProgramRepository.findAllByPersonIdAndEnabled(personId, true);
    }

    public byte[] download(Long personId) {
        List<PlanProgram> planPrograms = planProgramRepository.findAllByPersonIdAndEnabled(personId, true);
        if(!planPrograms.isEmpty()){
            try {
                File file = ResourceUtils.getFile("classpath:planProgramReport/jasper_report.jrxml");
                JasperReport report = JasperCompileManager.compileReport(file.getAbsolutePath());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                HashMap<String, Object> parameters = new HashMap<String, Object>();

                parameters.put("person", planPrograms.get(0).getPersonFullName());
                parameters.put("date", simpleDateFormat.format(new Date()));

                ArrayList<HashMap<String, String>> list = new ArrayList();
                for (PlanProgram planProgram : planPrograms) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("day", planProgram.getDay());
                    map.put("category", planProgram.getCategory().getName());
                    list.add(map);
                }
                JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(list);
                JasperPrint print = JasperFillManager.fillReport(report, parameters, beanColDataSource);
                return JasperExportManager.exportReportToPdf(print);
            } catch (FileNotFoundException | JRException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public PlanProgramDTO preparePlanProgramDTO(final Long id) {

        PlanProgram planProgram = getById(id);

        return PlanProgramDTO.builder()
                .id(planProgram.getId())
                .day(planProgram.getDay())
                .personFullName(planProgram.getPersonFullName())
                .categoryName(planProgram.getCategory().getName())
                .personId(planProgram.getPersonId())
                .categoryId(planProgram.getCategory().getId())
                .enabled(planProgram.isEnabled())
                .build();
    }

    public List<PlanProgramDTO> preparePlanProgramDTOList(List<PlanProgram> planPrograms) {
        List<PlanProgramDTO> planProgramDTOS = new ArrayList<>();
        for(PlanProgram planProgram : planPrograms) {
            planProgramDTOS.add(preparePlanProgramDTO(planProgram.getId()));
        }
        return planProgramDTOS;
    }
}
