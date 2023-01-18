package com.jpaexample.repository;

import com.jpaexample.dto.CategoryProviderDto;
import com.jpaexample.dto.ProductDto;
import com.jpaexample.dto.QCategoryProviderDto;
import com.jpaexample.dto.QProductDto;
import com.jpaexample.dto.search.ProductSearch;
import com.jpaexample.entity.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QProduct product = QProduct.product; //Q class
    private final QCategory category = QCategory.category;
    private final QProvider provider = QProvider.provider;
    private final QProductDetail productDetail = QProductDetail.productDetail;

    /**
     * 동적 쿼리 작성
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
     * 페이징 기본
     */
    @Override
    public Page<Product> getProductListPaging(Pageable pageable) {

        List<Product> productList = queryFactory
                .selectFrom(product)
                .orderBy(product.createdDate.desc()) //정렬 기준
                .offset(pageable.getOffset()) //데이터 시작 index
                .limit(pageable.getPageSize()) //최대 개수 지정
                .fetch();

        //fetchResult = deprecated.. 개수를 구하는 쿼리를 따로 날리는 방식을 권장
        Long count = getProductListCount(pageable);

        /**
         * Page 의 구현체 PageImpl
         *  - content : 페이지에 들어갈 List
         * - pageable : 요청 페이지 정보
         * - total : 들어갈 데이터의 총 개수
         */
        return new PageImpl<>(productList, pageable, count);
    }

    /**
     * 총 개수 쿼리
     */
    public Long getProductListCount(Pageable pageable){
        return queryFactory
                .select(Wildcard.count)
                .from(product)
                .orderBy(product.createdDate.desc()) //정렬 기준
                .offset(pageable.getOffset()) //데이터 시작 index
                .limit(pageable.getPageSize()) //최대 개수 지정
                .fetchOne();
    }

    /**
     * Projection
     * 테이블에서 원하는 컬럼만 뽑아서 조회할 수 있다
     * 아래 예시는 @QueryProjection 을 사용한 불변 QDto 클래스 생성 방식
     */
    @Override
    public List<ProductDto> getProductDtoList() {
        return queryFactory
                .select(
                        new QProductDto(
                                product.id,
                                product.category.name,
                                product.provider.name,
                                product.name,
                                product.price,
                                product.productDetail.detail
                        )
                )
                .from(product)
                .fetch();
    }

    @Override
    public List<Product> getProductListJoin(String categoryName) {
        return queryFactory
                .selectFrom(product)
                .join(product.category, category)
                .on(category.name.eq(categoryName))
                .fetch();
    }

    @Override
    public List<Product> getProductListTest(String categoryName) {
        return queryFactory
                .selectFrom(product)
                .where(
                        product.category.name.eq(categoryName)
                )
                .fetch();
    }
}
