package com.example.demo.repository;

import com.example.demo.dto.search.ProductSearch;
import com.example.demo.entity.Product;
import com.example.demo.entity.QProduct;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private final QProduct product = QProduct.product; //Q class

    @Override
    public List<Product> getProductList(ProductSearch search) {
        return queryFactory
                .selectFrom(product)
                .where(
                        searchByKey(search), //검색어
                        //product.price.between(search.getMinPrice(), search.getMaxPrice()), //min price ~ max price
                        product.stock.gt(0) //stock > 0

                )
                .fetch();
    }

    private BooleanExpression searchByKey(ProductSearch search){

        if(StringUtils.equals("name", search.getSearchBy())){
            return product.name.contains(search.getSearchKey());
        }else if(StringUtils.equals("provider", search.getSearchBy())){
            return product.provider.name.contains(search.getSearchKey());
        }else if(StringUtils.equals("category", search.getSearchBy())){
            return product.category.name.contains(search.getSearchKey());
        }else {
            return null;
        }

    }

//    private BooleanExpression searchByPrice(ProductSearch search){
//
//
//    }

}
