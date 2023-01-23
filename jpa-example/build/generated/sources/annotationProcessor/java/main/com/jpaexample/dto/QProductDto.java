package com.jpaexample.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.jpaexample.dto.QProductDto is a Querydsl Projection type for ProductDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QProductDto extends ConstructorExpression<ProductDto> {

    private static final long serialVersionUID = -87382415L;

    public QProductDto(com.querydsl.core.types.Expression<String> category, com.querydsl.core.types.Expression<String> provider, com.querydsl.core.types.Expression<Integer> price) {
        super(ProductDto.class, new Class<?>[]{String.class, String.class, int.class}, category, provider, price);
    }

}

