package com.jpaexample.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductDto {

    private Long id;

    private String category;

    private String provider;

    private String name;

    private int price;

    private String stock;

    private String detail;

    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;

    @QueryProjection
    public ProductDto(Long id,String category, String provider, String name, int price, String detail) {
        this.id = id;
        this.category = category;
        this.provider = provider;
        this.name = name;
        this.price = price;
        this.detail = detail;
    }
}
