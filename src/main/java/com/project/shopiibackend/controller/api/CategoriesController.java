package com.project.shopiibackend.controller.api;

import com.project.shopiibackend.domain.dto.request.CategoriesRequest;
import com.project.shopiibackend.domain.dto.response.CategoriesResponse;
import com.project.shopiibackend.domain.dto.response.ResponsePage;
import com.project.shopiibackend.domain.entity.category.Categories;
import com.project.shopiibackend.service.impl.CategoriesService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoriesController {

    @Autowired
    CategoriesService categoriesService;

    @GetMapping
    public ResponseEntity<Iterable<CategoriesResponse>> homeCategories() {
        Iterable<CategoriesResponse> categories = categoriesService.findAll();
        if (!categories.iterator().hasNext()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResponsePage> addCategories(@RequestBody CategoriesRequest categoriesRequest) {
        ResponsePage responsePage = categoriesService.save(categoriesRequest);
        return new ResponseEntity<>(responsePage, responsePage.getStatus());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponsePage> updateCategories(@RequestBody CategoriesRequest categoriesRequest) {
        ResponsePage responsePage = categoriesService.update(categoriesRequest);
        return new ResponseEntity<>(responsePage, responsePage.getStatus());

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponsePage> deleteCategories(@PathVariable Long id) {
        Optional<Categories> productOptional = categoriesService.findById(id);
        if (productOptional.isEmpty()) {
            return new ResponseEntity<>(ResponsePage.builder()
                    .data(null)
                    .message("Categories not found")
                    .status(HttpStatus.NOT_FOUND)
                    .build(), HttpStatus.NOT_FOUND);
        }
        ResponsePage responsePage = categoriesService.deleteById(id);

        return new ResponseEntity<>(responsePage, responsePage.getStatus());
    }

    @GetMapping("/search")
    public ResponseEntity<Iterable<CategoriesResponse>> searchCategories(@RequestParam("search") String keyword) {
        Iterable<CategoriesResponse> categories = categoriesService.searchCategories(keyword);
        if (!categories.iterator().hasNext()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @PutMapping("/{id}/toggle-status")
    public ResponseEntity<ResponsePage> toggleStatus(@PathVariable Long id) {
        ResponsePage responsePage = categoriesService.toggleStatus(id);
        return new ResponseEntity<>(responsePage, responsePage.getStatus());
    }

}

