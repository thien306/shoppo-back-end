package com.project.shopiibackend.domain.mapper.impl;

import com.project.shopiibackend.domain.mapper.ICategoriesMapper;
import com.project.shopiibackend.domain.dto.request.CategoriesRequest;
import com.project.shopiibackend.domain.dto.response.CategoriesResponse;
import com.project.shopiibackend.domain.entity.category.Categories;
import com.project.shopiibackend.domain.entity.utilities.Status;
import com.project.shopiibackend.repository.ICategoriesRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoriesMapper implements ICategoriesMapper {

    static final Logger LOGGER = LoggerFactory.getLogger(CategoriesMapper.class);

    @Autowired
    ICategoriesRepository categoriesRepository;

    @Override
    public Categories dtoEntity(CategoriesRequest categoriesRequest) {
        LOGGER.debug("CategoriesConverter - > dtoEntity");
        Categories categories = new Categories();
        BeanUtils.copyProperties(categoriesRequest, categories);
        if (categoriesRequest.getStatus() != null) {
            categories.setStatus(Status.valueOf(categoriesRequest.getStatus()));
        }
        return categories;
    }


    @Override
    public Categories deleteCategories(Long id) {
        LOGGER.debug("CategoriesConverter -> deleteCategoriesId with id: {}", id);
        return categoriesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categories not found with id: " + id));
    }

    @Override
    public CategoriesResponse entityToDto(Categories categories) {
        LOGGER.debug("CategoriesConverter -> entityToDto");
        CategoriesResponse categoriesResponse = new CategoriesResponse();
        BeanUtils.copyProperties(categories, categoriesResponse);
        if (categories.getStatus() != null) {
            categoriesResponse.setStatus(categories.getStatus().name());
        }
        return categoriesResponse;
    }

}
