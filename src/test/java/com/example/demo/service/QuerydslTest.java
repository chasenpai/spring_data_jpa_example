package com.example.demo.service;

import com.example.demo.dto.search.ProductSearch;
import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepositoryCustom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class QuerydslTest {

    @Autowired
    ProductRepositoryCustom productRepositoryCustom;

    /**
     * 검색어
     */
    @Test
    void getProductListBySearchKey(){

        ProductSearch search = ProductSearch.builder()
                .searchBy("provider")
                .searchKey("애플")
                .build();

        List<Product> productList = productRepositoryCustom.getProductList(search);
        System.out.println(productList.get(0).getName());
        System.out.println(productList.get(0).getProvider().getName());

    }

    /**
     * 가격대
     */
    @Test
    void getProductListByPrice(){

        ProductSearch search = ProductSearch.builder()
                .minPrice(50000)
                .maxPrice(100000)
                .build();

        List<Product> productList = productRepositoryCustom.getProductList(search);
        System.out.println(productList.get(0).getName());
        System.out.println(productList.get(0).getPrice());

    }

    /**
     * 페이징 처리
     */

}
