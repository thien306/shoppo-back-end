package com.project.shopiibackend.domain.mapper.impl;

import com.project.shopiibackend.domain.mapper.ISubCategoryMapper;
import com.project.shopiibackend.domain.dto.request.SubCategoryRequest;
import com.project.shopiibackend.domain.dto.response.SubCategoryResponse;
import com.project.shopiibackend.domain.entity.category.Categories;
import com.project.shopiibackend.domain.entity.category.SubCategory;
import com.project.shopiibackend.domain.entity.utilities.Status;
import com.project.shopiibackend.repository.ISubCategoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubCategoryMapper implements ISubCategoryMapper {

    static final Logger LOGGER = LoggerFactory.getLogger(SubCategoryMapper.class);

    @Autowired
    ISubCategoryRepository categoryRepository;

    @Override
    public SubCategory dtoEntity(SubCategoryRequest subCategoryRequest) {
        LOGGER.debug("SubCategoryConverter - > dtoEntity");
        SubCategory subCategory = new SubCategory();
        BeanUtils.copyProperties(subCategoryRequest, subCategory);
        Status status = Status.valueOf(subCategoryRequest.getStatus().toUpperCase());
        Categories categories =  Categories.builder().id(subCategoryRequest.getCategories()).build();
        subCategory.setStatus(status);
        subCategory.setCategories(categories);
        return subCategory;
    }

    @Override
    public SubCategory deleteCategories(Long id) {
        LOGGER.debug("SubCategoryConverter -> deleteSubCategoryId with id: {}", id);
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SubCategory not found with id: " + id));
    }

    @Override
    public SubCategoryResponse entityToDto(SubCategory subCategory) {
        LOGGER.debug("SubCategoryConverter -> entityToDto");
        SubCategoryResponse subCategoryResponse = new SubCategoryResponse();
        BeanUtils.copyProperties(subCategory, subCategoryResponse);
        if (subCategory.getStatus() != null) {
            subCategoryResponse.setStatus(subCategory.getStatus().name());
        }
        return subCategoryResponse;
    }
}
