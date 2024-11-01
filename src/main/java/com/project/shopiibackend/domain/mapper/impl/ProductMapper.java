package com.project.shopiibackend.domain.mapper.impl;

import com.project.shopiibackend.domain.mapper.IProductMapper;
import com.project.shopiibackend.domain.dto.request.ProductRequest;
import com.project.shopiibackend.domain.entity.category.SubCategory;
import com.project.shopiibackend.domain.entity.utilities.Image;
import com.project.shopiibackend.domain.entity.product.Product;
import com.project.shopiibackend.domain.entity.utilities.Brand;
import com.project.shopiibackend.domain.entity.utilities.Color;
import com.project.shopiibackend.domain.entity.utilities.Size;
import com.project.shopiibackend.domain.entity.utilities.Status;
import com.project.shopiibackend.repository.IProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductMapper implements IProductMapper {

    static final Logger LOGGER = LoggerFactory.getLogger(ProductMapper.class);

    final IProductRepository productRepository;


    @Override
    public Product dtoToEntity(ProductRequest productRequest) {
        LOGGER.debug("ProductConverter -> dtoToEntity");
        Product product = new Product();
        BeanUtils.copyProperties(productRequest, product);
        Brand brand = Brand.builder().id(productRequest.getBrand()).build();
        SubCategory subCategory = SubCategory.builder().id(productRequest.getSubCategory()).build();
        Status status = Status.valueOf(productRequest.getStatus().toUpperCase());
        List<Color> colors = productRequest.getColors().stream()
                .map(colorId -> Color.builder().id(Long.valueOf(colorId)).build())
                .collect(Collectors.toList());
        List<Size> sizes = productRequest.getSizes().stream()
                .map(sizeId -> Size.builder().id(Long.valueOf(sizeId)).build())
                .toList();
        List<Image> images = productRequest.getImages().stream()
                .map(imageUrl -> {
                    Image image = Image.builder().url(imageUrl).build();
                    image.setProduct(product);
                    return image;
                })
                .collect(Collectors.toList());
        product.setBrand(brand);
        product.setSubCategory(subCategory);
        product.setColor(colors);
        product.setSize(sizes);
        product.setImages(images);
        product.setStatus(status);
        return product;
    }

    @Override
    public Product deleteProductId(Long id) {
        LOGGER.debug("ProductConverter -> deleteProductId with id: {}", id);
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

}
