package com.example.demo.service;

import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.entity.ProductDetail;
import com.example.demo.entity.Provider;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductDetailRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.ProviderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
public class JpaTest {

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
                .name("이어폰")
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
     * cascade persist
     */
    @Test
    void productAndProductDetailSave(){

        Provider provider = providerRepository.findById(2L).orElseThrow(EntityNotFoundException::new);
        Category category = categoryRepository.findById(1L).orElseThrow(EntityNotFoundException::new);

        Product product = Product.builder()
                .name("갤럭시 노트 10")
                .price(1300000)
                .stock(100)
                .provider(provider)
                .category(category)
                .build();

        ProductDetail productDetail = ProductDetail.builder()
                .detail("퍼플")
                .product(product)
                .build();

        product.updateDetail(productDetail);

        productRepository.save(product); //cascade 로 영속성 전이가 발생하여 product 가 영속화될 때, productDetail 도 영속화
    }

    /**
     * cascade persist
     */
    @Test
    @Commit
    void saveCategoryAndProductList(){

        Category category = Category.builder()
                .name("마우스")
                .build();

        List<Product> productList = new ArrayList<>();

        for(int i = 1; i < 6; i++){
            Product product = Product.builder()
                    .name("마우스 " + i)
                    .price(50000)
                    .stock(100)
                    .category(category)
                    .build();
            productList.add(product);
        }
        category.updateProductList(productList);

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

    @Test
    void productDetailSave(){

        Product product = productRepository.findById(3L).orElseThrow(EntityNotFoundException::new);

        ProductDetail productDetail = ProductDetail.builder()
                .detail("화이트")
                .product(product)
                .build();

        productDetailRepository.save(productDetail);
    }

}
