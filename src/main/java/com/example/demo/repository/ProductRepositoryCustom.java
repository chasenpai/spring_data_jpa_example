package com.example.demo.repository;

import com.example.demo.dto.search.ProductSearch;
import com.example.demo.entity.Product;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;

public interface ProductRepositoryCustom {

    List<Product> getProductList(ProductSearch search);

}
