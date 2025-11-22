package com.org.testApi.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import javax.sql.DataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class SQLiteConfig {

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder, DataSource dataSource) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", "org.hibernate.community.dialect.SQLiteDialect");
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.format_sql", "true");
        properties.put("hibernate.globally_quoted_identifiers", "false");
        properties.put("hibernate.id.new_generator_mappings", "true");
        properties.put("hibernate.jdbc.batch_size", "0");
        // Add SQLite-specific settings
        properties.put("hibernate.connection.characterEncoding", "utf8");
        properties.put("hibernate.connection.useUnicode", "true");
        // Ensure schema validation and update
        properties.put("hibernate.temp.use_jdbc_metadata_defaults", "false");
        // Fix for ToOne relationship issue
        properties.put("hibernate.jpa.compliance.proxy", "false");
        // Enable schema validation
        properties.put("hibernate.hbm2ddl.auto", "update");
        
        return builder
                .dataSource(dataSource)
                .packages("com.org.testApi.models")
                .properties(properties)
                .build();
    }
    
    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }
    
    @Bean
    @Primary
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
}