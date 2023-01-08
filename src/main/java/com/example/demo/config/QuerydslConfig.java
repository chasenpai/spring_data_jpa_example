package com.example.demo.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Configuration
public class QuerydslConfig {

    @PersistenceContext //EntityManager 빈 주입시 사용
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory(){
        return new JPAQueryFactory(entityManager); //JPAQueryFactory 의 생성자로 EntityManager 객체를 넣는다
    }

}
