package com.jpaexample.repository;

import com.jpaexample.dto.ProductDto;
import com.jpaexample.dto.QProductDto;
import com.jpaexample.dto.search.ProductSearch;
import com.jpaexample.entity.*;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.JPAExpressions;
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
     * 집계 함수 사용
     */
    @Override
    public List<Tuple> getProductAggregation() {
        return queryFactory
                .select(
                        product.count(), //제품 총 개수
                        product.price.sum(), //총합
                        product.price.avg(), //평균가
                        product.price.max(), //최고가
                        product.price.min() //최저가
                )
                .from(product)
                .fetch();
    }

    /**
     * group by, having 사용 - 제조사별 휴대폰 평균가
     * 프로젝션(select 대상 지정) 대상이 둘 이상일 땐 튜플, DTO 조회를 사용
     */
    @Override
    public List<Tuple> getProductTuple() {
        return queryFactory
                .select(
                        product.category.name,
                        product.provider.name,
                        product.price.avg()
                )
                .from(product)
                .groupBy(product.provider)
                .having(product.category.name.eq("휴대폰"))
                .fetch();
    }

    /**
     * DTO 조회
     * - 아래 예시는 @QueryProjection 을 사용한 불변 QDto 클래스 생성 방식
     * - 컴파일러로 타입을 체크할 수 있는 가장 안전한 방식
     */
    @Override
    public List<ProductDto> getProductDto() {
        return queryFactory
                .select(
                        new QProductDto(
                                product.category.name,
                                product.provider.name,
                                product.price.avg().castToNum(Integer.class) //타입 변환
                        )
                )
                .from(product)
                .groupBy(product.provider)
                .having(product.category.name.eq("휴대폰"))
                .fetch();
    }

    /**
     * 페치 조인
     * - N + 1 문제 해결
     * - 즉시로딩으로 product, category, provider 한번에 조회
     */
    @Override
    public List<Product> getProductFetchJoin() {
        return queryFactory
                .selectFrom(product)
                .join(product.category, category).fetchJoin()
                .join(product.provider, provider).fetchJoin()
                .leftJoin(product.productDetail, productDetail).fetchJoin()
                .fetch();
    }

    /**
     * 서브쿼리 사용 - 최저가 휴대폰 & 평균가
     * - from 절의 서브쿼리는 지원하지 않는다 > join 사용 또는 쿼리 분리 실행
     */
    @Override
    public List<Tuple> getProductMinAvgPrice() {
        return queryFactory
                .select(
                        product.name,
                        product.price,
                        JPAExpressions
                                .select(product.price.avg())
                                .from(product)
                )
                .from(product)
                .where(product.price.eq(
                        JPAExpressions
                                .select(product.price.min())
                                .from(product)
                        ),
                        product.category.name.eq("휴대폰")
                )
                .fetch();
    }

    /**
     * case 문 사용 - 재고 상태
     */
    @Override
    public List<Tuple> getProductCaseStock() {
        return queryFactory
                .select(
                        product.name,
                        new CaseBuilder()
                                .when(product.stock.gt(50)) //gt >
                                .then("재고 있음")
                                .when(product.stock.between(1, 49))
                                .then("품절 임박")
                                .otherwise("품절")
                )
                .from(product)
                .fetch();
    }
}
