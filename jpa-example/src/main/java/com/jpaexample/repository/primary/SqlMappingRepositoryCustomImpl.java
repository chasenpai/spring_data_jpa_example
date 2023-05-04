package com.jpaexample.repository.primary;

import com.jpaexample.entity.primary.SqlMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SqlMappingRepositoryCustomImpl implements SqlMappingRepositoryCustom {

    private final EntityManager entityManager;

    /**
     * 고도화 하다보면 진짜 어쩔 수 없이 이렇게 사용할 일이 생긴다..
     */
    @Override
    public List<SqlMapping> getSqlMappingTestResult() {

        String nativeQuery =
                "SELECT " +
                        "A.id as product_id, " +
                        "A.name as product_name, " +
                        "B.name as category_name, " +
                        "C.name as provider_name " +
                "FROM " +
                        "product A " +
                "INNER JOIN " +
                        "category B " +
                "ON " +
                        "A.category_id = B.id " +
                "INNER JOIN " +
                        "provider C " +
                "ON " +
                        "A.provider_id = C.id ";

        Query query = entityManager.createNativeQuery(nativeQuery, "sqlResultSetMappingTest");

        return query.getResultList();
    }
}
