package com.project.shopiibackend.controller.api;

import com.project.shopiibackend.domain.dto.request.SubCategoryRequest;
import com.project.shopiibackend.domain.dto.response.ResponsePage;
import com.project.shopiibackend.domain.dto.response.SubCategoryResponse;
import com.project.shopiibackend.domain.entity.category.SubCategory;
import com.project.shopiibackend.service.impl.SubCategoryService;
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
@RequestMapping("/api/subCategory")
@CrossOrigin(origins = "*")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubCategoryController {

    @Autowired
    SubCategoryService subCategoryService;

    @GetMapping
    public ResponseEntity<Iterable<SubCategoryResponse>> homeSubCategory() {
        Iterable<SubCategoryResponse> subCategoryIterable = subCategoryService.findAll();
        if (!subCategoryIterable.iterator().hasNext()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(subCategoryIterable, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResponsePage> addSubCategory(@RequestBody SubCategoryRequest subCategoryRequest) {
        ResponsePage responsePage = subCategoryService.save(subCategoryRequest);
        return new ResponseEntity<>(responsePage, responsePage.getStatus());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponsePage> updateSubCategory(@RequestBody SubCategoryRequest subCategoryRequest) {
        ResponsePage responsePage = subCategoryService.update(subCategoryRequest);
        return new ResponseEntity<>(responsePage, responsePage.getStatus());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponsePage> deleteSubCategory(@PathVariable Long id) {
        Optional<SubCategory> productOptional = subCategoryService.findById(id);
        if (productOptional.isEmpty()) {
            return new ResponseEntity<>(ResponsePage.builder()
                    .data(null)
                    .message("sub category not found")
                    .status(HttpStatus.NOT_FOUND)
                    .build(), HttpStatus.NOT_FOUND);
        }
        ResponsePage responsePage = subCategoryService.deleteById(id);

        return new ResponseEntity<>(responsePage, responsePage.getStatus());
    }

    @GetMapping("/search")
    public ResponseEntity<Iterable<SubCategoryResponse>> searchSubCategory(@RequestParam("search") String keyword) {
        Iterable<SubCategoryResponse> searchCategories = subCategoryService.searchSubCategory(keyword);
        if (!searchCategories.iterator().hasNext()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(searchCategories, HttpStatus.OK);
    }

    @PutMapping("/{id}/toggle-status")
    public ResponseEntity<ResponsePage> toggleStatus(@PathVariable Long id) {
        ResponsePage responsePage = subCategoryService.toggleStatus(id);
        return new ResponseEntity<>(responsePage, responsePage.getStatus());
    }
}
