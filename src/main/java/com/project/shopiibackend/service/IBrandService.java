package com.project.shopiibackend.service;

import com.project.shopiibackend.domain.dto.response.BrandResponse;
import com.project.shopiibackend.domain.entity.utilities.Brand;

import java.util.Optional;

public interface IBrandService {

    Optional<Brand> findById(Long id);

    Iterable<BrandResponse> findAll();
}
