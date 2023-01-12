package com.jpaexample.dto.search;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ProductSearch {

    private int maxPrice;

    private int minPrice;

    private String provider;

    private String category;

    private int stock;

    private String searchBy;

    private String searchKey;

    private LocalDateTime createdDate;

}
