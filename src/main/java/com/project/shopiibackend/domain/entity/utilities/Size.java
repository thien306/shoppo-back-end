package com.project.shopiibackend.domain.entity.utilities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.shopiibackend.domain.entity.product.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Size {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String sizeName;

    @ManyToMany(mappedBy = "size")
    @JsonIgnoreProperties("size")
    List<Product> product;
}
