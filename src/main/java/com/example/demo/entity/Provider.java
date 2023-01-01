package com.example.demo.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "provider")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Provider extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "provider", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Product> productList = new ArrayList<>();

    @Builder
    public Provider(Long id, String name, List<Product> productList) {
        this.id = id;
        this.name = name;
        this.productList = productList;
    }
}
