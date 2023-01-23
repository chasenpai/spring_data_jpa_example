package com.jpaexample.service;

import com.jpaexample.dto.ProductDto;
import com.jpaexample.dto.search.ProductSearch;
import com.jpaexample.entity.Product;
import com.jpaexample.repository.ProductRepositoryCustom;
import com.querydsl.core.Tuple;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    @Test
    void getProductListPaging(){

        Pageable pageable = PageRequest.of(0, 5); //시작 index, 가져올 개수 설정

        Page<Product> productPage = productRepositoryCustom.getProductListPaging(pageable);
        System.out.println(productPage.getContent());

    }

    /**
     * 제품 집계
     */
    @Test
    void getProductAggregation(){
        
        List<Tuple> aggregation = productRepositoryCustom.getProductAggregation();

        Tuple result = aggregation.get(0);
        System.out.println("result = " + result);
        
    }

    /**
     * 제조사별 휴대폰 평균가 - Tuple & Dto
     */
    @Test
    void getProductGroupByProvider(){

        List<Tuple> tuple = productRepositoryCustom.getProductTuple();

        System.out.println("tuple.get(0) = " + tuple.get(0));
        System.out.println("tuple.get(1) = " + tuple.get(1));
        
        List<ProductDto> dto = productRepositoryCustom.getProductDto();
        System.out.println("dto = " + dto);

    }

    /**
     * 서브 쿼리
     */
    @Test
    void getProductSubQuery(){

        List<Tuple> maxPrice = productRepositoryCustom.getProductMinAvgPrice();
        System.out.println("maxPrice = " + maxPrice.get(0));
        
    }

    /**
     * 재고 상태
     */
    @Test
    void getProductCaseStock(){

        List<Tuple> productStock = productRepositoryCustom.getProductCaseStock();
        System.out.println("productStock = " + productStock);
        
    }


}
