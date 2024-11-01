package com.project.shopiibackend.repository;

import com.project.shopiibackend.domain.entity.utilities.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IColorRepository extends JpaRepository<Color, Long> {
}
