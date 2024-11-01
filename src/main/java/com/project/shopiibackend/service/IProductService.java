package com.project.shopiibackend.service;

import com.project.shopiibackend.domain.dto.request.ProductRequest;
import com.project.shopiibackend.domain.dto.response.ProductResponse;
import com.project.shopiibackend.domain.dto.response.ResponsePage;
import com.project.shopiibackend.domain.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IProductService {

    Page<ProductResponse> findAll(Pageable page);

    Iterable<ProductResponse> findAllProduct(Product product);

    ResponsePage save(ProductRequest productRequest);

    ResponsePage update(ProductRequest productRequest);

    ResponsePage deleteById(Long id);

    Optional<ProductResponse> findById(Long id);

    Page<ProductResponse> searchProduct(String keyword, Pageable pageable);

    Page<ProductResponse> findAllByOrderByPriceAsc(Pageable pageable);

    Page<ProductResponse> findAllByOrderByPriceDesc(Pageable pageable);

    Page<ProductResponse> findBySubCategoryId(Long id, Pageable pageable);

    Page<ProductResponse> findByBrandId(Long id, Pageable pageable);

}
