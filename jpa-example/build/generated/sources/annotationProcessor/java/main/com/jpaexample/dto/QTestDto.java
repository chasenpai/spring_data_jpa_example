package com.jpaexample.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.jpaexample.dto.QTestDto is a Querydsl Projection type for TestDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QTestDto extends ConstructorExpression<TestDto> {

    private static final long serialVersionUID = 2056998796L;

    public QTestDto(com.querydsl.core.types.Expression<String> name, com.querydsl.core.types.Expression<String> detail) {
        super(TestDto.class, new Class<?>[]{String.class, String.class}, name, detail);
    }

}

