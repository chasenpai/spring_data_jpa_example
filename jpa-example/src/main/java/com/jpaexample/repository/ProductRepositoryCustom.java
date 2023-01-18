package com.jpaexample.repository;

import com.jpaexample.dto.ProductDto;
import com.jpaexample.dto.search.ProductSearch;
import com.jpaexample.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductRepositoryCustom {

    List<Product> getProductList(ProductSearch search);

    Page<Product> getProductListPaging(Pageable pageable);

    List<ProductDto> getProductDtoList();

    List<Product> getProductListJoin(String categoryName);

    List<Product> getProductListTest(String categoryName);

}
