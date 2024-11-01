package com.project.shopiibackend.repository;

import com.project.shopiibackend.domain.entity.utilities.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ISizeRepository extends JpaRepository<Size, Long> {

    @Query("SELECT s.sizeName FROM Size s")
    Iterable<String> findAllSizeNames();
}
