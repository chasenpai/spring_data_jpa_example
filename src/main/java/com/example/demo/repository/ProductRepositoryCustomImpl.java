package com.example.demo.repository;

import com.example.demo.dto.search.ProductSearch;
import com.example.demo.entity.Product;
import com.example.demo.entity.QProduct;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private final QProduct product = QProduct.product; //Q class

    /**
     * 제품 목록
     */
    @Override
    public List<Product> getProductList(ProductSearch search) {
        return queryFactory
                .selectFrom(product)
                .where(
                        searchByKey(search), //검색어
                        searchByPrice(search), //가격대
                        product.stock.gt(0) //stock > 0

                )
                .fetch();
    }

    /**
     * 검색어 조건
     * BooleanExpression - 동적 쿼리를 작성하기 위해 사용한다. 가독성 및 재사용성 증가
     * null 을 리턴하면 where 절에서 무시된다
     */
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

    /**
     * 가격대 조건
     */
    private BooleanExpression searchByPrice(ProductSearch search){

        if(search.getMaxPrice() > 0){
            return product.price.between(search.getMinPrice(), search.getMaxPrice());
        }

        return null;
    }

    /**
     * 페이징
     */
    @Override
    public Page<Product> getProductListPaging(ProductSearch search, Pageable pageable) {
        return null;
    }
}
