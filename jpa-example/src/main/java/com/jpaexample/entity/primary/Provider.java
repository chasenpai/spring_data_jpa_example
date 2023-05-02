package com.jpaexample.entity.primary;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "provider")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Provider extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "provider", orphanRemoval = true)
    @ToString.Exclude
    private List<Product> productList = new ArrayList<>();

    @Builder
    public Provider(Long id, String name, List<Product> productList) {
        this.id = id;
        this.name = name;
        this.productList = productList;
    }

    public void updateProductList(List<Product> productList){
        this.productList = productList;
    }

}
