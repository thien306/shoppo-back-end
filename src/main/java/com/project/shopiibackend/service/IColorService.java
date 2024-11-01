package com.project.shopiibackend.service;

import com.project.shopiibackend.domain.dto.response.ColorResponse;

public interface IColorService {

    Iterable<ColorResponse> findAll();
}
