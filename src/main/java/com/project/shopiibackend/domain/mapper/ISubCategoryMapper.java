package com.project.shopiibackend.domain.mapper;

import com.project.shopiibackend.domain.dto.request.SubCategoryRequest;
import com.project.shopiibackend.domain.dto.response.SubCategoryResponse;
import com.project.shopiibackend.domain.entity.category.SubCategory;

public interface ISubCategoryMapper {

    SubCategory dtoEntity(SubCategoryRequest categoriesRequest);

    SubCategory deleteCategories(Long id);

    SubCategoryResponse entityToDto(SubCategory subCategory);
}
