package com.jpaexample.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * multi data source configuration
 */
@Configuration
@PropertySource({"classpath:application.properties"})
@EnableJpaRepositories(
        basePackages = {"com.jpaexample.repository.primary"}, //JPA Repository 스캔 패키지 지정
        entityManagerFactoryRef = "primaryEntityManager", //엔티티 매니저 팩토리 참조
        transactionManagerRef = "primaryTransactionManager" //트랜잭션 매니저 참조
)
public class PrimaryDataSourceConfig {

    /**
     * LocalContainerEntityManagerFactoryBean - 엔티티 매니저 구성
     */
    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean primaryEntityManager() {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setPersistenceUnitName("primaryEntityManager"); //엔티티 매니저 팩토리 빈의 이름 설정
        entityManagerFactoryBean.setDataSource(primaryDataSource()); //데이터베이스 연결 소스 지정
        entityManagerFactoryBean.setPackagesToScan("com.jpaexample.entity.primary"); //JPA Entity 위치 지정

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        entityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter); //JPA 공급자 설정

        Properties prop = new Properties(); //hibernate property 설정
        prop.setProperty("hibernate.ddl-auto", "none");
        prop.setProperty("hibernate.dialect", "org.hibernate.dialect.MariaDBDialect");
        prop.setProperty("hibernate.show_sql", "true");
        prop.setProperty("hibernate.format_sql", "true");
        prop.setProperty("hibernate.default_batch_fetch_size", "100");

        entityManagerFactoryBean.setJpaProperties(prop);
        entityManagerFactoryBean.afterPropertiesSet();

        return entityManagerFactoryBean;
    }

    /**
     * DataSource 생성
     */
    @Primary
    @Bean
    @ConfigurationProperties(prefix="spring.primary-datasource")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * JPA 트랜잭션 매니저 생성
     */
    @Primary
    @Bean
    public PlatformTransactionManager primaryTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(primaryEntityManager().getObject()); //해당 엔티티 매니저의 DB와 트랜잭션을 관리
        return transactionManager;
    }

}
