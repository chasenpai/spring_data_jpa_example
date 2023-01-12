package com.jpaexample.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class TestDto {

    private String name;

    private String detail;

    @QueryProjection
    public TestDto(String name, String detail) {
        this.name = name;
        this.detail = detail;
    }
}
