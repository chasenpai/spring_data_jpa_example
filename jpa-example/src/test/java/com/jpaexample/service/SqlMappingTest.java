package com.jpaexample.service;

import com.jpaexample.entity.primary.SqlMapping;
import com.jpaexample.repository.primary.SqlMappingRepositoryCustom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class SqlMappingTest {

    @Autowired
    SqlMappingRepositoryCustom sqlMappingRepositoryCustom;

    /**
     * Native Query Dto Mapping
     */
    @Test
    void getSqlMappingTestResult() {

        List<SqlMapping> list = sqlMappingRepositoryCustom.getSqlMappingTestResult();
        System.out.println("list = " + list);

    }

}
