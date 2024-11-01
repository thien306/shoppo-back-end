package com.project.shopiibackend.repository;

import com.project.shopiibackend.domain.entity.product.Product;
import com.project.shopiibackend.domain.entity.utilities.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IProductRepository extends PagingAndSortingRepository<Product, Long> {

    Product save(Product product);

    void delete(Product product);

    Optional<Product> findById(@Param("id") Long id);

    @Query("SELECT p FROM Product p LEFT JOIN p.subCategory sc LEFT JOIN p.brand b " +
            "WHERE (LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(sc.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(b.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND p.status = com.project.shopiibackend.domain.entity.utilities.Status.ACTIVE")
    Page<Product> searchProducts(@Param("keyword") String keyword, Pageable pageable);

    Page<Product> findAllByOrderByPriceAsc(Pageable pageable);

    Page<Product> findAllByOrderByPriceDesc(Pageable pageable);

    Page<Product> findBySubCategoryId(Long id, Pageable pageable);

    Page<Product> findByBrandId(Long id, Pageable pageable);

    Page<Product> findByStatus(Status status, Pageable pageable);

    Iterable<Product> findAllBy();

}
