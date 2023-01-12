package com.jpaexample.service;

import com.jpaexample.entity.Category;
import com.jpaexample.entity.Product;
import com.jpaexample.entity.ProductDetail;
import com.jpaexample.entity.Provider;
import com.jpaexample.repository.CategoryRepository;
import com.jpaexample.repository.ProductDetailRepository;
import com.jpaexample.repository.ProductRepository;
import com.jpaexample.repository.ProviderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
public class MappingTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProviderRepository providerRepository;

    @Autowired
    ProductDetailRepository productDetailRepository;

    @Test
    void categorySave(){
        Category category = Category.builder()
                .name("휴대폰")
                .build();
        categoryRepository.save(category);
    }

    @Test
    void providerSave(){
        Provider provider = Provider.builder()
                .name("삼성")
                .build();
        providerRepository.save(provider);
    }

    /**
     * 엔티티 양방향 매핑
     */
    @Test
    void selectProductAndProductDetail(){

        //양방향으로 매핑된 엔티티는 서로를 참조할 수 있다
        Product product = productRepository.findById(1L).orElseThrow(EntityNotFoundException::new);
        System.out.println("product = " + product.getProductDetail());

        ProductDetail productDetail = productDetailRepository.findById(1L).orElseThrow(EntityNotFoundException::new);
        System.out.println("productDetail = " + productDetail.getProduct());

    }

    /**
     * 지연로딩 & 즉시로딩
     */
    @Test
    void selectProduct(){

        /**
         * 즉시로딩이 설정된 엔티티
         * - 조회시 연관된 엔티티까지 조회해옴 > 연관관계가 복잡하다면 성능 저하를 일으킬 수 있음
         */
        Product product = productRepository.findById(1L).orElseThrow(EntityNotFoundException::new);

        /**
         * 지연로딩이 설정된 엔티티
         * - 연관된 엔티티를 사용하는 시점에 쿼리를 날림
         * - OneToMany 의 경우 지연로딩(LAZY)이 기본 값이다
         */
        Provider provider = providerRepository.findById(1L).orElseThrow(EntityNotFoundException::new);
        System.out.println("productList = " + provider.getProductList());
        //지연로딩으로 product 엔티티를 조회할 경우 트랜잭션처리가 되어있지 않으면 proxy no session 오류가 발생한다

    }

    /**
     * cascade persist
     */
    @Test
    @Commit
    void saveCategoryAndProductList(){

        Provider provider = Provider.builder()
                .name("애플")
                .build();

        List<Product> productList1 = new ArrayList<>();

        for(int i = 8; i < 15; i++){
            Product product = Product.builder()
                    .name("아이폰 " + i)
                    .price(50000)
                    .stock(100)
                    .provider(provider)
                    .build();
            productList1.add(product);
        }
        provider.updateProductList(productList1);

        providerRepository.save(provider); //연관관계의 주인이 provider 가 아니기 때문에 product 는 저장되지 않는다

        //cascade persist 적용된 엔티티
        Category category = Category.builder()
                .name("마우스")
                .build();

        List<Product> productList2 = new ArrayList<>();

        for(int i = 1; i < 6; i++){
            Product product = Product.builder()
                    .name("마우스 " + i)
                    .price(50000)
                    .stock(100)
                    .category(category)
                    .build();
            productList2.add(product);
        }
        category.updateProductList(productList2);

        categoryRepository.save(category); //cascade 로 영속성 전이가 발생하여 category 가 영속화될 때, product 도 영속화
    }

    /**
     * cascade remove vs orphanRemoval
     */
    @Test
    @Commit
    void deleteCategoryAndProduct(){

        //cascade remove
        Category category1 = categoryRepository.findById(1L).orElseThrow(EntityNotFoundException::new);
        categoryRepository.delete(category1); //부모(category) 가 삭제되면 자식(product) 도 삭제된다

        //orphanRemoval = true
        Category category2 = categoryRepository.findById(5L).orElseThrow(EntityNotFoundException::new);
        category2.getProductList().remove(0);
        /**
         * 자식 엔티티를 컬렉션에서 제거 > 부모 엔티티와의 연관관계가 끊어짐 = 고아객체
         * orphanRemoval 을 활성화 하면 고아객체는 삭제된다
         * 두 옵션을 모두 활성화 하면 부모 엔티티로 자식 엔티티의 생명주기를 관리할 수 있다
         */

    }

}
