package com.project.shopiibackend.service.impl;

import com.project.shopiibackend.domain.dto.response.ColorResponse;
import com.project.shopiibackend.domain.entity.utilities.Color;
import com.project.shopiibackend.repository.IColorRepository;
import com.project.shopiibackend.service.IColorService;
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
public class ColorService implements IColorService {

    static final Logger LOGGER = LoggerFactory.getLogger(ColorService.class);

    @Autowired
    IColorRepository colorRepository;

    @Override
    public List<ColorResponse> findAll() {
        List<Color> colors = colorRepository.findAll();
        List<ColorResponse> colorResponses = new ArrayList<>();
        for (Color color : colors) {
            colorResponses.add(new ColorResponse(color.getId(), color.getColorName()));
        }

        return colorResponses;
    }
}
