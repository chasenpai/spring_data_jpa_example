package com.jpaexample.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Configuration
public class QuerydslConfig {

    @PersistenceContext(unitName = "primaryEntityManager") //EntityManager 빈 주입시 사용
    private EntityManager primaryEntityManager;

    @PersistenceContext(unitName = "secondaryEntityManager")
    private EntityManager secondaryEntityManager;

    @Primary
    @Bean("primaryJpaQueryFactory")
    public JPAQueryFactory primaryJpaQueryFactory(){
        return new JPAQueryFactory(primaryEntityManager); //JPAQueryFactory 의 생성자로 EntityManager 객체를 넣는다
    }

    @Bean("secondaryJpaQueryFactory")
    public JPAQueryFactory secondaryJpaQueryFactory(){
        return new JPAQueryFactory(secondaryEntityManager);
    }

}
