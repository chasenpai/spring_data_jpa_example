package com.example.demo.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "product")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "provider_id")
    private Provider provider;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = false, fetch = FetchType.LAZY)
    private ProductDetail productDetail;

    @Builder
    public Product(Long id, String name, Integer price, Integer stock, Category category, Provider provider, ProductDetail productDetail) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.provider = provider;
        this.productDetail = productDetail;
    }

    public void updateDetail(ProductDetail productDetail){
        this.productDetail = productDetail;
    }
}
