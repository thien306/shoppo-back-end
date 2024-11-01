package com.project.shopiibackend.service.impl;

import com.project.shopiibackend.domain.mapper.impl.SubCategoryMapper;
import com.project.shopiibackend.domain.dto.request.SubCategoryRequest;
import com.project.shopiibackend.domain.dto.response.ResponsePage;
import com.project.shopiibackend.domain.dto.response.SubCategoryResponse;
import com.project.shopiibackend.domain.entity.category.Categories;
import com.project.shopiibackend.domain.entity.category.SubCategory;
import com.project.shopiibackend.domain.entity.utilities.Status;
import com.project.shopiibackend.repository.ICategoriesRepository;
import com.project.shopiibackend.repository.ISubCategoryRepository;
import com.project.shopiibackend.service.ISubCategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubCategoryService implements ISubCategoryService {

    static final Logger LOGGER = LoggerFactory.getLogger(SubCategoryService.class);

    @Autowired
    ISubCategoryRepository subCategoryRepository;

    @Autowired
    SubCategoryMapper subCategoryConverter;

    @Autowired
    ICategoriesRepository categoriesRepository;

    @Override
    public Iterable<SubCategoryResponse> findAll() {
        LOGGER.info("SubCategoryService -> findAll invoked");

        Iterable<SubCategory> subCategoryIterable = subCategoryRepository.findByStatus(Status.ACTIVE);

        return StreamSupport.stream(subCategoryIterable.spliterator(), false)
                .map(subCategoryConverter::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<SubCategory> findById(Long id) {
        return subCategoryRepository.findById(id);
    }

    @Override
    public ResponsePage save(SubCategoryRequest subCategoryRequest) {
        LOGGER.info("SubCategoryService -> save invoke");

        SubCategory subCategory = subCategoryConverter.dtoEntity(subCategoryRequest);
        try {
            subCategoryRepository.save(subCategory);
            return ResponsePage.builder()
                    .data(null)
                    .message("sub category created successfully")
                    .status(HttpStatus.OK)
                    .build();
        } catch (Exception e) {
            return ResponsePage.builder()
                    .data(null)
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    @Override
    public ResponsePage update(SubCategoryRequest subCategoryRequest) {
        Optional<SubCategory> subCategoryOptional = subCategoryRepository.findById(subCategoryRequest.getId());
        if (subCategoryOptional.isEmpty()) {
            return ResponsePage.builder()
                    .data(null)
                    .message("sub category not found!")
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }

        SubCategory subCategory = subCategoryOptional.get();
        subCategory.setName(subCategoryRequest.getName());
        subCategory.setCreatedDate(subCategoryRequest.getCreatedDate());
        Optional<Categories> categoriesOptional = categoriesRepository.findById(subCategoryRequest.getCategories());
        categoriesOptional.ifPresent(subCategory::setCategories);
        Status status = Status.valueOf(subCategoryRequest.getStatus());
        subCategory.setStatus(status);
        try {
            subCategoryRepository.save(subCategory);
            return ResponsePage.builder()
                    .data(null)
                    .message("The sub category has been updated successfully.")
                    .status(HttpStatus.OK)
                    .build();
        } catch (Exception e) {
            LOGGER.error("Error updating sub category: ", e);
            return ResponsePage.builder()
                    .data(null)
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .error(e.getMessage())
                    .build();
        }
    }

    @Override
    public ResponsePage deleteById(Long id) {
        LOGGER.info("SubCategoryService -> deleteById invoked with id: {}", id);
        SubCategory subCategory = subCategoryConverter.deleteCategories(id);
        try {
            subCategoryRepository.delete(subCategory);
            return ResponsePage.builder()
                    .data(null)
                    .message("sub category deleted successfully")
                    .status(HttpStatus.OK)
                    .build();
        } catch (Exception e) {
            LOGGER.error("Error deleting sub category with id {}: {}", id, e.getMessage());
            return ResponsePage.builder()
                    .data(null)
                    .message("Failed to delete sub category")
                    .status(HttpStatus.BAD_REQUEST)
                    .error(e.getMessage())
                    .build();
        }
    }

    @Override
    public Iterable<SubCategoryResponse> searchSubCategory(String keyword) {
        Iterable<SubCategory> subCategoryIterable = subCategoryRepository.searchSubCategory(keyword);
        return StreamSupport.stream(subCategoryIterable.spliterator(), false)
                .map(subCategoryConverter::entityToDto)
                .collect(Collectors.toList());
    }

    public ResponsePage toggleStatus(Long id) {
        Optional<SubCategory> subCategoryOptional = subCategoryRepository.findById(id);

        if (subCategoryOptional.isPresent()) {
            SubCategory subCategory = subCategoryOptional.get();

            if (subCategory.getStatus() == Status.ACTIVE) {
                subCategory.setStatus(Status.INACTIVE);
            } else {
                subCategory.setStatus(Status.ACTIVE);
            }

            subCategoryRepository.save(subCategory);

            return ResponsePage.builder()
                    .data(subCategory)
                    .message("sub category status updated successfully")
                    .status(HttpStatus.OK)
                    .build();
        } else {
            return ResponsePage.builder()
                    .data(null)
                    .message("sub category not found")
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }


}
