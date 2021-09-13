package com.ubt.workoutservice.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ubt.workoutservice.configurations.exceptions.DatabaseException;
import com.ubt.workoutservice.entities.Category;
import com.ubt.workoutservice.entities.dtos.CategoryDTO;
import com.ubt.workoutservice.repositories.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAll(){
        return categoryRepository.findAll();
    }

    public List<Category> getAllEnabled(){
        return categoryRepository.findAllByEnabled(true);
    }

    public Category getById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public boolean save(CategoryDTO categoryDTO) throws DatabaseException {
        Category category = Category.builder()
                .name(categoryDTO.getName())
                .description(categoryDTO.getDescription())
                .enabled(true).build();

        if(StringUtils.isNotBlank(category.getName()) && StringUtils.isNotBlank(category.getDescription())) {
            try {
                categoryRepository.save(category);
                return true;
            }catch (Exception e) {
                throw new DatabaseException("duplicate");
            }
        }
        else {
            return false;
        }
    }

    public boolean update(CategoryDTO categoryDTO) throws DatabaseException {

        Category category = getById(categoryDTO.getId());
        Category tempCategory = Category.builder()
                .id(category.getId())
                .name(categoryDTO.getName())
                .description(categoryDTO.getDescription())
                .enabled(true).build();

        if(StringUtils.isNotBlank(tempCategory.getName()) && StringUtils.isNotBlank(tempCategory.getDescription())
                && categoryRepository.existsById(tempCategory.getId())) {
            try {
                categoryRepository.save(tempCategory);
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

        Category category = getById(id);
        if(category.isEnabled()){
            category.setEnabled(false);
            categoryRepository.save(category);
            return true;
        }
        else {
            return false;
        }
    }

    public boolean enable(Long id) {

        Category category = getById(id);
        if(!category.isEnabled()){
            category.setEnabled(true);
            categoryRepository.save(category);
            return true;
        }
        else {
            return false;
        }
    }

    public CategoryDTO prepareCategoryDTO(final Long id) {

        Category category = getById(id);

        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .enabled(category.isEnabled())
                .build();
    }


    public List<CategoryDTO> prepareCategoryDTOList(List<Category> categories) {
        List<CategoryDTO> categoryDTOS = new ArrayList<>();
        for(Category category: categories) {
            categoryDTOS.add(prepareCategoryDTO(category.getId()));
        }
        return categoryDTOS;
    }

}
