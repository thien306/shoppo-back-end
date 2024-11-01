package com.project.shopiibackend.service.impl;

import com.project.shopiibackend.domain.mapper.impl.ProductMapper;
import com.project.shopiibackend.domain.dto.request.ProductRequest;
import com.project.shopiibackend.domain.dto.response.ProductResponse;
import com.project.shopiibackend.domain.dto.response.ResponsePage;
import com.project.shopiibackend.domain.entity.category.SubCategory;
import com.project.shopiibackend.domain.entity.utilities.Brand;
import com.project.shopiibackend.domain.entity.utilities.Image;
import com.project.shopiibackend.domain.entity.product.Product;
import com.project.shopiibackend.domain.entity.utilities.Color;
import com.project.shopiibackend.domain.entity.utilities.Size;
import com.project.shopiibackend.domain.entity.utilities.Status;
import com.project.shopiibackend.repository.IProductRepository;
import com.project.shopiibackend.service.IProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductService implements IProductService {

    static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    IProductRepository productRepository;

    @Autowired
    ProductMapper productMapper;

    @Override
    public Page<ProductResponse> findAll(Pageable pageable) {
        Page<Product> productPage = productRepository.findByStatus(Status.ACTIVE,pageable);
        return productPage.map(product -> ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .brand(product.getBrand().getName())
                .subCategory(product.getSubCategory().getName())
                .colors(product.getColor().stream()
                        .map(Color::getColorName)
                        .collect(Collectors.toList()))
                .sizes(product.getSize().stream()
                        .map(Size::getSizeName)
                        .collect(Collectors.toList()))
                .images(product.getImages().stream()
                        .map(Image::getUrl)
                        .collect(Collectors.toList()))
                .status(product.getStatus().name())
                .createdDate(product.getCreatedDate())
                .build());
    }

    @Override
    public Iterable<ProductResponse> findAllProduct(Product product) {
        Iterable<Product> productIterable = productRepository.findAllBy();
        return StreamSupport.stream(productIterable.spliterator(), false)
                .map(productItem -> ProductResponse.builder()
                        .id(productItem.getId())
                        .name(productItem.getName())
                        .description(productItem.getDescription())
                        .price(productItem.getPrice())
                        .stock(productItem.getStock())
                        .brand(productItem.getBrand().getName())
                        .subCategory(productItem.getSubCategory().getName())
                        .colors(productItem.getColor().stream()
                                .map(Color::getColorName)
                                .collect(Collectors.toList()))
                        .sizes(productItem.getSize().stream()
                                .map(Size::getSizeName)
                                .collect(Collectors.toList()))
                        .images(productItem.getImages().stream()
                                .map(Image::getUrl)
                                .collect(Collectors.toList()))
                        .status(productItem.getStatus().name())
                        .createdDate(productItem.getCreatedDate())
                        .build())
                .collect(Collectors.toList());
    }


    @Override
    public ResponsePage save(ProductRequest productRequest) {
        LOGGER.info("ProductService -> save invoked");

        Product product = productMapper.dtoToEntity(productRequest);
        try {
            productRepository.save(product);
            return ResponsePage.builder()
                    .data(null)
                    .message("product created successfully")
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
    public ResponsePage update(ProductRequest productRequest) {
        Optional<Product> productOptional = productRepository.findById(productRequest.getId());
        if (productOptional.isEmpty()) {
            return ResponsePage.builder()
                    .data(null)
                    .message("Product not found!")
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }

        Product product = productOptional.get();
        product.setId(productRequest.getId());
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setStock(productRequest.getStock());
        product.setCreatedDate(productRequest.getCreatedDate());
        Brand brand = Brand.builder().id(productRequest.getBrand()).build();
        product.setBrand(brand);
        SubCategory subCategory = SubCategory.builder().id(productRequest.getSubCategory()).build();
        product.setSubCategory(subCategory);
        List<Color> colors = new ArrayList<>(product.getColor());
        colors.clear();
        colors.addAll(productRequest.getColors().stream()
                .map(colorId -> Color.builder().id(Long.valueOf(colorId)).build())
                .toList());
        product.setColor(colors);

        List<Size> sizes = new ArrayList<>(product.getSize());
        sizes.clear();
        sizes.addAll(productRequest.getSizes().stream()
                .map(sizeId -> Size.builder().id(Long.valueOf(sizeId)).build())
                .toList());
        product.setSize(sizes);

        List<Image> currentImages = product.getImages();
        currentImages.clear();
        List<Image> newImages = productRequest.getImages().stream()
                .map(imageUrl -> {
                    Image image = Image.builder().url(imageUrl).product(product).build();
                    return image;
                })
                .toList();
        currentImages.addAll(newImages);
        product.setImages(currentImages);


        try {
            productRepository.save(product);
            return ResponsePage.builder()
                    .data(null)
                    .message("The product has been updated successfully.")
                    .status(HttpStatus.OK)
                    .build();
        } catch (Exception e) {
            LOGGER.error("Error updating product: ", e);
            return ResponsePage.builder()
                    .data(null)
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .error(e.getMessage())
                    .build();
        }
    }


    @Override
    public ResponsePage deleteById(Long id) {
        LOGGER.info("ProductService -> deleteById invoked with id: {}", id);

        Product product = productMapper.deleteProductId(id);
        try {
            productRepository.delete(product);
            return ResponsePage.builder()
                    .data(null)
                    .message("Product deleted successfully")
                    .status(HttpStatus.OK)
                    .build();
        } catch (Exception e) {
            LOGGER.error("Error deleting product with id {}: {}", id, e.getMessage());
            return ResponsePage.builder()
                    .data(null)
                    .message("Failed to delete product")
                    .status(HttpStatus.BAD_REQUEST)
                    .error(e.getMessage())
                    .build();
        }
    }


    @Override
    public Optional<ProductResponse> findById(Long id) {
        Optional<Product> productPage = productRepository.findById(id);
        return productPage.map(product -> ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .brand(product.getBrand().getName())
                .subCategory(product.getSubCategory().getName())
                .colors(product.getColor().stream().map(Color::getColorName).collect(Collectors.toList()))
                .sizes(product.getSize().stream().map(Size::getSizeName).collect(Collectors.toList()))
                .images(product.getImages().stream().map(Image::getUrl).collect(Collectors.toList()))
                .createdDate(product.getCreatedDate())
                .status(String.valueOf(product.getStatus()))
                .build());
    }

    @Override
    public Page<ProductResponse> searchProduct(String keyword, Pageable pageable) {
        Page<Product> productPage = productRepository.searchProducts(keyword, pageable);
        return productPage.map(product -> ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .brand(product.getBrand().getName())
                .subCategory(product.getSubCategory().getName())
                .colors(product.getColor().stream().map(Color::getColorName).collect(Collectors.toList()))
                .sizes(product.getSize().stream().map(Size::getSizeName).collect(Collectors.toList()))
                .images(product.getImages().stream().map(Image::getUrl).collect(Collectors.toList()))
                .createdDate(product.getCreatedDate())
                .build());
    }

    @Override
    public Page<ProductResponse> findAllByOrderByPriceAsc(Pageable pageable) {
        Page<Product> productPage = productRepository.findAllByOrderByPriceAsc(pageable);
        return productPage.map(product -> ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .brand(product.getBrand().getName())
                .subCategory(product.getSubCategory().getName())
                .colors(product.getColor().stream().map(Color::getColorName).collect(Collectors.toList()))
                .sizes(product.getSize().stream().map(Size::getSizeName).collect(Collectors.toList()))
                .images(product.getImages().stream().map(Image::getUrl).collect(Collectors.toList()))
                .createdDate(product.getCreatedDate())
                .build());
    }

    @Override
    public Page<ProductResponse> findAllByOrderByPriceDesc(Pageable pageable) {
        Page<Product> productPage = productRepository.findAllByOrderByPriceDesc(pageable);
        return productPage.map(product -> ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .brand(product.getBrand().getName())
                .subCategory(product.getSubCategory().getName())
                .colors(product.getColor().stream().map(Color::getColorName).collect(Collectors.toList()))
                .sizes(product.getSize().stream().map(Size::getSizeName).collect(Collectors.toList()))
                .images(product.getImages().stream().map(Image::getUrl).collect(Collectors.toList()))
                .createdDate(product.getCreatedDate())
                .build());
    }

    @Override
    public Page<ProductResponse> findBySubCategoryId(Long id, Pageable pageable) {
        Page<Product> productPage = productRepository.findBySubCategoryId(id, pageable);
        return productPage.map(product -> ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .brand(product.getBrand().getName())
                .subCategory(product.getSubCategory().getName())
                .colors(product.getColor().stream().map(Color::getColorName).collect(Collectors.toList()))
                .sizes(product.getSize().stream().map(Size::getSizeName).collect(Collectors.toList()))
                .images(product.getImages().stream().map(Image::getUrl).collect(Collectors.toList()))
                .createdDate(product.getCreatedDate())
                .build());
    }

    @Override
    public Page<ProductResponse> findByBrandId(Long id, Pageable pageable) {
        Page<Product> productPage = productRepository.findByBrandId(id, pageable);
        return productPage.map(product -> ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .brand(product.getBrand().getName())
                .subCategory(product.getSubCategory().getName())
                .colors(product.getColor().stream().map(Color::getColorName).collect(Collectors.toList()))
                .sizes(product.getSize().stream().map(Size::getSizeName).collect(Collectors.toList()))
                .images(product.getImages().stream().map(Image::getUrl).collect(Collectors.toList()))
                .createdDate(product.getCreatedDate())
                .build());
    }

//    @Override
//    public Optional<ProductResponse> findProductWithAllDetails(Long id) {
//        // Gọi truy vấn `findByIdWithImages`
//        Optional<Product> productWithImages = productRepository.findByIdWithImages(id);
//
//        if (!productWithImages.isPresent()) {
//            return Optional.empty(); // Nếu không tìm thấy sản phẩm, trả về Optional rỗng
//        }
//
//        Product product = productWithImages.get();
//
//        // Gọi và hợp nhất dữ liệu từ `findByIdWithColor`
//        productRepository.findByIdWithColor(id).ifPresent(p -> product.setColor(p.getColor()));
//
//        // Gọi và hợp nhất dữ liệu từ `findByIdWithSize`
//        productRepository.findByIdWithSize(id).ifPresent(p -> product.setSize(p.getSize()));
//
//        // Gọi và hợp nhất dữ liệu từ `findByIdWithSuAndSubCategory`
//        productRepository.findByIdWithSuAndSubCategory(id).ifPresent(p -> {
//            product.setSubCategory(p.getSubCategory());
//            product.setBrand(p.getBrand());
//        });
//
//        // Chuyển đổi từ `Product` sang `ProductResponse`
//        ProductResponse productResponse = mapToProductResponse(product);
//
//        return Optional.of(productResponse);
//    }



    public ResponsePage toggleStatus(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);

        if (productOptional.isPresent()) {
            Product product = productOptional.get();

            if (product.getStatus() == Status.ACTIVE) {
                product.setStatus(Status.INACTIVE);
            } else {
                product.setStatus(Status.ACTIVE);
            }

            productRepository.save(product);

            return ResponsePage.builder()
                    .data(product)
                    .message("Product status updated successfully")
                    .status(HttpStatus.OK)
                    .build();
        } else {
            return ResponsePage.builder()
                    .data(null)
                    .message("Product not found")
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }


}
