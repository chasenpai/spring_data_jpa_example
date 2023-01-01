package com.example.demo.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "product_detail")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductDetail extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String detail;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Builder
    public ProductDetail(Long id, String detail, Product product) {
        this.id = id;
        this.detail = detail;
        this.product = product;
    }
}
