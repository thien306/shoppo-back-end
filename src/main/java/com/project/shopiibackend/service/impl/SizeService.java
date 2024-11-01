package com.project.shopiibackend.service.impl;

import com.project.shopiibackend.domain.dto.response.SizeResponse;
import com.project.shopiibackend.domain.entity.utilities.Size;
import com.project.shopiibackend.repository.ISizeRepository;
import com.project.shopiibackend.service.ISizeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SizeService implements ISizeService {

    static final Logger LOGGER = LoggerFactory.getLogger(SizeService.class);

    @Autowired
    ISizeRepository sizeRepository;

    @Override
    public List<SizeResponse> findAllSizeNames() {
        List<Size> sizes = sizeRepository.findAll();
        List<SizeResponse> sizeResponses = new ArrayList<>();
        for (Size size : sizes) {
            sizeResponses.add(new SizeResponse(size.getId(), size.getSizeName()));
        }

        return sizeResponses;
    }
}
