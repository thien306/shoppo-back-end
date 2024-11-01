package com.project.shopiibackend.domain.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRequest {

    Long id;

    String name;

    String description;

    Double price;

    int stock;

    Long brand;

    Long subCategory;

    List<String> colors;

    List<String> sizes;

    List<String> images;

    String status;

    LocalDateTime createdDate;
}
