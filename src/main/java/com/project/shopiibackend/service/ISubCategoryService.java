package com.project.shopiibackend.service;

import com.project.shopiibackend.domain.dto.request.SubCategoryRequest;
import com.project.shopiibackend.domain.dto.response.ResponsePage;
import com.project.shopiibackend.domain.dto.response.SubCategoryResponse;
import com.project.shopiibackend.domain.entity.category.SubCategory;

import java.util.Optional;

public interface ISubCategoryService {

    Iterable<SubCategoryResponse> findAll();

    Optional<SubCategory> findById(Long id);

    ResponsePage save(SubCategoryRequest subCategoryRequest);

    ResponsePage update(SubCategoryRequest subCategoryRequest);

    ResponsePage deleteById(Long id);

    Iterable<SubCategoryResponse> searchSubCategory(String keyword);
}
