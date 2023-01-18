package com.jpaexample.service;

import com.jpaexample.dto.CategoryProviderDto;
import com.jpaexample.dto.search.ProductSearch;
import com.jpaexample.entity.Product;
import com.jpaexample.repository.ProductRepositoryCustom;
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

    @Test
    void getProductListJoin(){

        String categoryName = "이어폰";

        List<Product> productList1 = productRepositoryCustom.getProductListJoin(categoryName);
//        System.out.println(productList1.get(0));

        //List<Product> productList2 = productRepositoryCustom.getProductListTest(categoryName);
        //System.out.println(productList2.get(0));
        //System.out.println(productList2.get(0).getProductDetail().getDetail());

        List<CategoryProviderDto> categoryProviderDtoList = productRepositoryCustom.getCategoryProvider("아이폰8");
    }

}
