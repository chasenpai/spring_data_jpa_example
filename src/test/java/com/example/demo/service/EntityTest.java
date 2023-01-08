package com.example.demo.service;

import com.example.demo.entity.Product;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductDetailRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.ProviderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class EntityTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProviderRepository providerRepository;

    @Autowired
    ProductDetailRepository productDetailRepository;

    /**
     * 엔티티의 생명주기
     */
    @Test
    void entityLifeCycle(){

        //비영속(new) - 영속성 컨텍스트에 추가되지 않는 상태
        Product product = Product.builder()
                .name("모니터")
                .price(320000)
                .stock(100)
                .build();



    }


}
