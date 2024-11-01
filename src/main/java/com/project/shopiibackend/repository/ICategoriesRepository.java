package com.project.shopiibackend.repository;

import com.project.shopiibackend.domain.entity.category.Categories;
import com.project.shopiibackend.domain.entity.utilities.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoriesRepository extends JpaRepository<Categories, Long> {

    Iterable<Categories> findByStatus(Status status);

    @Query("SELECT c FROM Categories c LEFT JOIN c.subCategory sc " +
            "WHERE (LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(sc.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND c.status = com.project.shopiibackend.domain.entity.utilities.Status.ACTIVE")
    Iterable<Categories> searchCategories(@Param("keyword") String keyword);


}
