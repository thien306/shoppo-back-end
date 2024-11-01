package com.project.shopiibackend.domain.mapper;

import com.project.shopiibackend.domain.dto.request.CategoriesRequest;
import com.project.shopiibackend.domain.dto.response.CategoriesResponse;
import com.project.shopiibackend.domain.entity.category.Categories;

public interface ICategoriesMapper {

    Categories dtoEntity(CategoriesRequest categoriesRequest);

    Categories deleteCategories(Long id);

    CategoriesResponse entityToDto(Categories categories);
}
