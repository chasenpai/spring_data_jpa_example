package com.jpaexample.repository.primary;

import com.jpaexample.entity.primary.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {
}
