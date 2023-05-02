package com.jpaexample.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
        basePackages = {"com.jpaexample.repository.secondary"},
        entityManagerFactoryRef = "secondaryEntityManager",
        transactionManagerRef = "secondaryTransactionManager"
)
public class SecondaryDataSourceConfig {

    @Bean
    public LocalContainerEntityManagerFactoryBean secondaryEntityManager() {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setPersistenceUnitName("secondaryEntityManager");
        entityManagerFactoryBean.setDataSource(secondaryDataSource());
        entityManagerFactoryBean.setPackagesToScan("com.jpaexample.entity.secondary");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        entityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);

        Properties prop = new Properties();
        prop.setProperty("hibernate.ddl-auto", "none");
        prop.setProperty("hibernate.dialect", "org.hibernate.dialect.MariaDBDialect");
        prop.setProperty("hibernate.show_sql", "true");
        prop.setProperty("hibernate.format_sql", "true");
        prop.setProperty("hibernate.default_batch_fetch_size", "100");

        entityManagerFactoryBean.setJpaProperties(prop);
        entityManagerFactoryBean.afterPropertiesSet();

        return entityManagerFactoryBean;
    }

    @Bean
    @ConfigurationProperties(prefix="spring.secondary-datasource")
    public DataSource secondaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public PlatformTransactionManager secondaryTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(secondaryEntityManager().getObject());
        return transactionManager;
    }

}
