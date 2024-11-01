package com.project.shopiibackend.controller.api;

import com.project.shopiibackend.domain.dto.response.BrandResponse;
import com.project.shopiibackend.service.impl.BrandService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/brand")
@CrossOrigin(origins = "*")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class BrandController {

    final BrandService brandService;

    @GetMapping
    public ResponseEntity<Iterable<BrandResponse>> homeBrand() {
        Iterable<BrandResponse> brandResponses = brandService.findAll();
        if (!brandResponses.iterator().hasNext()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(brandResponses, HttpStatus.OK);
    }
}
