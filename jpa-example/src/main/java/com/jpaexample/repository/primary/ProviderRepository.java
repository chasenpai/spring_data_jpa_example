package com.jpaexample.repository.primary;

import com.jpaexample.entity.primary.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProviderRepository extends JpaRepository<Provider, Long> {
}
