package com.example.demo.repository;

import com.example.demo.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * 쿼리 메소드 정리
     *  - (is)는 생략 가능
     */

    //find ~ By
    Optional<Product> findById(Long id);
    List<Product> findAllByName(String name);
    Product queryById(Long id);

    //exists ~ By
    boolean existsById(Long id);

    //count ~ By
    long countByName(String name);

    //delete ~ By, remove ~ By
    void deleteById(Long id);
    long removeByName(String name);

    // ~ First ~, ~ Top
    List<Product> findFirst5ByName(String name);
    List<Product> findTop10ByName(String name);

    // (is)Not, Equals
    Product findByIdIs(Long id);
    Product findByIdEquals(Long id);
    Product findByIdNot(Long id);
    Product findByIdIsNot(Long id);

    //Null, (is)Null
    List<Product> findByStockNull();
    List<Product> findByStockIsNull();
    List<Product> findByStockNotNull();
    List<Product> findByStockIsNotNull();

    //(is)True, (is)False
    //List<Product> findBySoldOutTrue();
    //List<Product> findBySoldOutFalse();

    //And, Or
    Product findByIdAndName(Long id, String name);
    Product findByIdOrName(Long id, String name);

    //(is)GreaterThan, (is)LessThan, (is)Between
    List<Product> findByPriceIsGreaterThan(Long price);
    List<Product> findByPriceGreaterThan(Long price);
    List<Product> findByPriceGreaterThanEqual(Long price);

    List<Product> findByPriceIsLessThan(Long price);
    List<Product> findByPriceLessThan(Long price);
    List<Product> findByPriceLessThanEqual(Long price);

    List<Product> findByPriceIsBetween(Long lowPrice, Long highPrice);
    List<Product> findByPriceBetween(Long lowPrice, Long highPrice);

    //(is)StartingWith(StartsWith), (is)EndingWith(EndsWith), (is)Containing(Contains), (is)Like
    List<Product> findByNameStartsWith(String name);
    List<Product> findByNameStartingWith(String name);
    List<Product> findByNameIsStartingWith(String name);

    List<Product> findByNameEndsWith(String name);
    List<Product> findByNameEndingWith(String name);
    List<Product> findByNameIsEndingWith(String name);

    List<Product> findByNameContains(String name);
    List<Product> findByNameContaining(String name);
    List<Product> findByNameIsContaining(String name);

    List<Product> findByNameLike(String name);
    List<Product> findByNameIsLike(String name);

    //Asc, Desc
    List<Product> findByNameOrderByIdAsc(String name);
    List<Product> findByNameOrderByIdDesc(String name);
    List<Product> findByNameOrderByPriceAscStockDesc(String name);

    //Sort
    List<Product> findByName(String name, Sort sort);

    //paging
    Page<Product> findByName(String name, Pageable pageable);
    //Page<Product> productPage = productRepository.findById("펜", PageRequest.of(0, 2));

    @Query("select p from Product p where p.name = ?1")
    List<Product> findByName(String name);

    @Query("select p from Product p where p.name = :name")
    List<Product> findByNameParam(@Param("name") String name);

    @Query("select p.name, p.price, p.stock from Product p where p.name = :name")
    List<Object[]> findByNameParam2(@Param("name") String name);

    @Query("update Product p set p.name = :name where p.id = :id")
    @Modifying
    void updateNameById(@Param("name") String name, @Param("id") Long id);

    @Query(value = "select * from product p where p.name =:name", nativeQuery = true)
    List<Product> findByName2(String name);

}
