package com.jpaexample.repository.primary;

import com.jpaexample.entity.primary.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
