package com.org.testApi.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import javax.sql.DataSource;

@Configuration
public class SQLiteConfig {

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }
    
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder, DataSource dataSource) {
        Map<String, String> properties = new HashMap<>();
        properties.put("hibernate.dialect", "org.hibernate.community.dialect.SQLiteDialect");
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.format_sql", "true");
        properties.put("hibernate.globally_quoted_identifiers", "false");
        properties.put("hibernate.id.new_generator_mappings", "false");
        properties.put("hibernate.jdbc.batch_size", "0");
        // Add SQLite-specific settings
        properties.put("hibernate.connection.characterEncoding", "utf8");
        properties.put("hibernate.connection.useUnicode", "true");
        // Ensure schema validation and update
        properties.put("hibernate.temp.use_jdbc_metadata_defaults", "false");
        
        return builder
                .dataSource(dataSource)
                .packages("com.org.testApi.models")
                .properties(properties)
                .build();
    }
}