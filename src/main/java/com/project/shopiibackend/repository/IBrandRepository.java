package com.project.shopiibackend.repository;


import com.project.shopiibackend.domain.entity.utilities.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBrandRepository extends JpaRepository<Brand, Long> {


}
