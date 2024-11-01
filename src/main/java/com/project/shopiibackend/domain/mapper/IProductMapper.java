package com.project.shopiibackend.domain.mapper;

import com.project.shopiibackend.domain.dto.request.ProductRequest;
import com.project.shopiibackend.domain.entity.product.Product;

public interface IProductMapper {

    Product dtoToEntity(ProductRequest productRequest);

    Product deleteProductId(Long id);
}
