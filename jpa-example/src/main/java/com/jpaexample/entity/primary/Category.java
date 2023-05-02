package com.jpaexample.entity.primary;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Category extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude //순환참조 방지
    private List<Product> productList = new ArrayList<>();

    @Builder
    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void updateProductList(List<Product> productList){
        this.productList = productList;
    }

}
