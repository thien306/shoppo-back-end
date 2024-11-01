package com.project.shopiibackend.repository;

import com.project.shopiibackend.domain.entity.category.SubCategory;

import com.project.shopiibackend.domain.entity.utilities.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ISubCategoryRepository extends JpaRepository<SubCategory, Long> {

    Iterable<SubCategory> findByStatus(Status status);

    void delete(SubCategory subCategory);

    @Query("SELECT c FROM SubCategory c " +
            "WHERE (LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(c.categories.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND c.status = com.project.shopiibackend.domain.entity.utilities.Status.ACTIVE")
    Iterable<SubCategory> searchSubCategory(@Param("keyword") String keyword);



}
