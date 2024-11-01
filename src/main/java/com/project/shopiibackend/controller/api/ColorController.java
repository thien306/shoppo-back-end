package com.project.shopiibackend.controller.api;

import com.project.shopiibackend.domain.dto.response.ColorResponse;
import com.project.shopiibackend.service.impl.ColorService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/color")
@CrossOrigin(origins = "*")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ColorController {

    @Autowired
    ColorService colorService;

    @GetMapping
    public ResponseEntity<List<ColorResponse>> homeColor() {
        List<ColorResponse> colorIterable = colorService.findAll();
        if (!colorIterable.iterator().hasNext()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(colorIterable, HttpStatus.OK);
    }

}
