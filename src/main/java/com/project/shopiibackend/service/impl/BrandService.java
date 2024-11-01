package com.project.shopiibackend.service.impl;

import com.project.shopiibackend.domain.dto.response.BrandResponse;
import com.project.shopiibackend.domain.dto.response.ColorResponse;
import com.project.shopiibackend.domain.entity.utilities.Brand;
import com.project.shopiibackend.domain.entity.utilities.Color;
import com.project.shopiibackend.repository.IBrandRepository;
import com.project.shopiibackend.service.IBrandService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BrandService implements IBrandService {

    @Autowired
    IBrandRepository brandRepository;

    @Override
    public Optional<Brand> findById(Long id) {
        return brandRepository.findById(id);
    }

    @Override
    public Iterable<BrandResponse> findAll() {
        List<Brand> brands = brandRepository.findAll();
        List<BrandResponse> brandResponses = new ArrayList<>();
        for (Brand brand : brands) {
            brandResponses.add(new BrandResponse(brand.getId(), brand.getName()));
        }

        return brandResponses;
    }
}
