package com.project.shopiibackend.controller.api;

import com.project.shopiibackend.domain.dto.response.SizeResponse;
import com.project.shopiibackend.service.impl.SizeService;
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
@RequestMapping("/api/size")
@CrossOrigin(origins = "*")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SizeController {

    @Autowired
    SizeService sizeService;

    @GetMapping
    public ResponseEntity<List<SizeResponse>> homeSize() {
        List<SizeResponse> sizeIterable = sizeService.findAllSizeNames();
        if (!sizeIterable.iterator().hasNext()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(sizeIterable, HttpStatus.OK);
    }

}
