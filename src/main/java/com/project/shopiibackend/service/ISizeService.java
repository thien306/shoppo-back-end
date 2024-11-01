package com.project.shopiibackend.service;

import com.project.shopiibackend.domain.dto.response.SizeResponse;

public interface ISizeService {

    Iterable<SizeResponse> findAllSizeNames();
}
