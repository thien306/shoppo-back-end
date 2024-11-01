package com.project.shopiibackend.controller.api;

import com.project.shopiibackend.domain.dto.request.ProductRequest;
import com.project.shopiibackend.domain.dto.response.ProductResponse;
import com.project.shopiibackend.domain.dto.response.ResponsePage;
import com.project.shopiibackend.domain.entity.product.Product;
import com.project.shopiibackend.service.impl.ProductService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductController {

    @Autowired
    ProductService productService;

    private static final int DEFAULT_PAGE = 0;

    public static final int PAGE_SIZE = 5;

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> homeProduct(@PageableDefault(page = DEFAULT_PAGE, size = PAGE_SIZE) Pageable pageable) {
        Page<ProductResponse> productResponsePage;

        productResponsePage = productService.findAll(pageable);

        if (productResponsePage.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(productResponsePage, HttpStatus.OK);
    }

    @GetMapping("/allProduct")
    public ResponseEntity<List<ProductResponse>> allProduct(Product product) {
        List<ProductResponse> productResponseList = StreamSupport
                .stream(productService.findAllProduct(product).spliterator(), false)
                .collect(Collectors.toList());
        if (productResponseList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(productResponseList, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<ResponsePage> addProduct(@RequestBody ProductRequest productRequest) {
        ResponsePage responsePage = productService.save(productRequest);
        return new ResponseEntity<>(responsePage, responsePage.getStatus());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponsePage> deleteProduct(@PathVariable Long id) {
        Optional<ProductResponse> productOptional = productService.findById(id);
        if (productOptional.isEmpty()) {
            return new ResponseEntity<>(ResponsePage.builder()
                    .data(null)
                    .message("Product not found")
                    .status(HttpStatus.NOT_FOUND)
                    .build(), HttpStatus.NOT_FOUND);
        }
        ResponsePage responsePage = productService.deleteById(id);

        return new ResponseEntity<>(responsePage, responsePage.getStatus());
    }


    @GetMapping("/{id}")
    public ResponseEntity<Optional<ProductResponse>> searchProductId(@PathVariable Long id) {
        Optional<ProductResponse> productOptional = productService.findById(id);
        return new ResponseEntity<>(productOptional, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponsePage> updateProduct(@RequestBody ProductRequest productRequest) {
        ResponsePage responsePage = productService.update(productRequest);
        return new ResponseEntity<>(responsePage, responsePage.getStatus());
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProductResponse>> searchProduct(@RequestParam("search") String keyword, @PageableDefault(page = DEFAULT_PAGE, size = PAGE_SIZE) Pageable pageable) {
        Page<ProductResponse> productResponsePage = productService.searchProduct(keyword, pageable);
        if (productResponsePage.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(productResponsePage, HttpStatus.OK);
    }

    @GetMapping("/sort")
    public ResponseEntity<Page<ProductResponse>> sortPrice(@RequestParam(name = "value") String value, Pageable pageable) {
        Page<ProductResponse> productResponsePage;

        if (value.equalsIgnoreCase("ASC")) {
            productResponsePage = productService.findAllByOrderByPriceAsc(pageable);
        } else if (value.equalsIgnoreCase("DESC")) {
            productResponsePage = productService.findAllByOrderByPriceDesc(pageable);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(productResponsePage, HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<ProductResponse>> filterByCategoryOrBrand(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long brandId,
            Pageable pageable) {

        Page<ProductResponse> productResponsePage;

        if (categoryId != null) {
            productResponsePage = productService.findBySubCategoryId(categoryId, pageable);
        } else if (brandId != null) {
            productResponsePage = productService.findByBrandId(brandId, pageable);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (productResponsePage.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(productResponsePage, HttpStatus.OK);
    }

    @PutMapping("/{id}/toggle-status")
    public ResponseEntity<ResponsePage> toggleProductStatus(@PathVariable Long id) {
        Optional<ProductResponse> productOptional = productService.findById(id);

        if (productOptional.isEmpty()) {
            return new ResponseEntity<>(ResponsePage.builder()
                    .data(null)
                    .message("Product not found")
                    .status(HttpStatus.NOT_FOUND)
                    .build(), HttpStatus.NOT_FOUND);
        }

        ResponsePage responsePage = productService.toggleStatus(id);

        return new ResponseEntity<>(responsePage, responsePage.getStatus());
    }

//    @GetMapping("/search-images/{id}")
//    public ResponseEntity<Optional<ProductResponse>> searchProductIdWithImages(@PathVariable Long id) {
//        Optional<ProductResponse> productOptional = productService.findByIdWithImages(id);
//        return new ResponseEntity<>(productOptional, HttpStatus.OK);
//    }


}
