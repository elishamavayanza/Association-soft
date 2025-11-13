package com.org.testApi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceUtils;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
public class SQLitePragmaConfig {

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void enableForeignKeys() {
        try (Connection connection = DataSourceUtils.getConnection(dataSource)) {
            try (Statement statement = connection.createStatement()) {
                // Enable foreign key constraints for SQLite
                statement.execute("PRAGMA foreign_keys = ON");
                // Optionally enable deferred foreign keys to handle migration issues
                statement.execute("PRAGMA defer_foreign_keys = ON");
            }
        } catch (SQLException e) {
            // Log the error but don't fail the application startup
            System.err.println("Failed to enable foreign key constraints: " + e.getMessage());
        }
    }
}