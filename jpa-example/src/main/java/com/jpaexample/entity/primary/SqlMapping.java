package com.jpaexample.entity.primary;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity //영속성 관리를 위해 선언(가상 엔티티?)
@SqlResultSetMapping( //SQL 쿼리 결과 매핑
        name = "sqlResultSetMappingTest",
        classes = @ConstructorResult( //결과를 매핑할 클래스와 생성자 지정
                targetClass = SqlMapping.class,
                columns = { //생성자와 순서 동일할 것
                        @ColumnResult(name = "product_id", type = Long.class),
                        @ColumnResult(name = "product_name", type = String.class),
                        @ColumnResult(name = "category_name", type = String.class),
                        @ColumnResult(name = "provider_name", type = String.class)
                }
        )
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class SqlMapping {

    @Id
    private Long productId;
    private String product;
    private String category;
    private String provider;

    public SqlMapping(Long productId, String product, String category, String provider) {
        this.productId = productId;
        this.product = product;
        this.category = category;
        this.provider = provider;
    }

}
