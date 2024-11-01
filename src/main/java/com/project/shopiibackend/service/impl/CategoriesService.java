package com.project.shopiibackend.service.impl;

import com.project.shopiibackend.domain.mapper.impl.CategoriesMapper;
import com.project.shopiibackend.domain.dto.request.CategoriesRequest;
import com.project.shopiibackend.domain.dto.response.CategoriesResponse;
import com.project.shopiibackend.domain.dto.response.ResponsePage;
import com.project.shopiibackend.domain.entity.category.Categories;
import com.project.shopiibackend.domain.entity.utilities.Status;
import com.project.shopiibackend.repository.ICategoriesRepository;
import com.project.shopiibackend.service.ICategoriesService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoriesService implements ICategoriesService {

    static final Logger LOGGER = LoggerFactory.getLogger(CategoriesService.class);

    @Autowired
    ICategoriesRepository categoriesRepository;

    @Autowired
    CategoriesMapper categoriesConverter;


    @Override
    public Iterable<CategoriesResponse> findAll() {
        LOGGER.info("CategoriesService -> findAll invoked");

        Iterable<Categories> categories = categoriesRepository.findByStatus(Status.ACTIVE);

        return StreamSupport.stream(categories.spliterator(), false)
                .map(categoriesConverter::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ResponsePage save(CategoriesRequest categoriesRequest) {
        LOGGER.info("CategoriesService -> save invoke");

        Categories categories = categoriesConverter.dtoEntity(categoriesRequest);
        try {
            categoriesRepository.save(categories);
            return ResponsePage.builder()
                    .data(null)
                    .message("categories created successfully")
                    .status(HttpStatus.OK)
                    .build();
        } catch (Exception e) {
            return ResponsePage.builder()
                    .data(null)
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    @Override
    public ResponsePage update(CategoriesRequest categoriesRequest) {
        Optional<Categories> categoriesOptional = categoriesRepository.findById(categoriesRequest.getId());
        if (categoriesOptional.isEmpty()) {
            return ResponsePage.builder()
                    .data(null)
                    .message("categories not found!")
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }

        Categories categories = categoriesOptional.get();
        categories.setName(categoriesRequest.getName());

        try {
            Status status = Status.valueOf(categoriesRequest.getStatus().toUpperCase());
            categories.setStatus(status);
        } catch (IllegalArgumentException e) {
            return ResponsePage.builder()
                    .data(null)
                    .message("Invalid status value: " + categoriesRequest.getStatus())
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }

        categories.setCreatedDate(categoriesRequest.getCreatedDate());

        try {
            categoriesRepository.save(categories);
            return ResponsePage.builder()
                    .data(null)
                    .message("The categories has been updated successfully.")
                    .status(HttpStatus.OK)
                    .build();
        } catch (Exception e) {
            LOGGER.error("Error updating categories: ", e);
            return ResponsePage.builder()
                    .data(null)
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .error(e.getMessage())
                    .build();
        }
    }


    @Override
    public Optional<Categories> findById(Long id) {
        return categoriesRepository.findById(id);
    }

    @Override
    public ResponsePage deleteById(Long id) {
        LOGGER.info("CategoriesService -> deleteById invoked with id: {}", id);

        Categories categories = categoriesConverter.deleteCategories(id);
        try {
            categoriesRepository.delete(categories);
            return ResponsePage.builder()
                    .data(null)
                    .message("Categories deleted successfully")
                    .status(HttpStatus.OK)
                    .build();
        } catch (Exception e) {
            LOGGER.error("Error deleting categories with id {}: {}", id, e.getMessage());
            return ResponsePage.builder()
                    .data(null)
                    .message("Failed to delete categories")
                    .status(HttpStatus.BAD_REQUEST)
                    .error(e.getMessage())
                    .build();
        }
    }

    @Override
    public Iterable<CategoriesResponse> searchCategories(String keyword) {
        Iterable<Categories> categories = categoriesRepository.searchCategories(keyword);
        return StreamSupport.stream(categories.spliterator(), false)
                .map(categoriesConverter::entityToDto)
                .collect(Collectors.toList());
    }

    public ResponsePage toggleStatus(Long id) {
        Optional<Categories> categoriesOptional = categoriesRepository.findById(id);

        if (categoriesOptional.isPresent()) {
            Categories categories = categoriesOptional.get();

            if (categories.getStatus() == Status.ACTIVE) {
                categories.setStatus(Status.INACTIVE);
            } else {
                categories.setStatus(Status.ACTIVE);
            }

            categoriesRepository.save(categories);

            return ResponsePage.builder()
                    .data(categories)
                    .message("Categories status updated successfully")
                    .status(HttpStatus.OK)
                    .build();
        } else {
            return ResponsePage.builder()
                    .data(null)
                    .message("Categories not found")
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }

}
