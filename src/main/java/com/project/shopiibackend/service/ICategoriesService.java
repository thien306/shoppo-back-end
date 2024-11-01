package com.project.shopiibackend.service;

import com.project.shopiibackend.domain.dto.request.CategoriesRequest;
import com.project.shopiibackend.domain.dto.response.CategoriesResponse;
import com.project.shopiibackend.domain.dto.response.ResponsePage;
import com.project.shopiibackend.domain.entity.category.Categories;

import java.util.Optional;

public interface ICategoriesService {

    Iterable<CategoriesResponse> findAll();

    ResponsePage save(CategoriesRequest categoriesRequest);

    ResponsePage update(CategoriesRequest categoriesRequest);

    Optional<Categories> findById(Long id);

    ResponsePage deleteById(Long id);

    Iterable<CategoriesResponse> searchCategories(String keyword);


}
