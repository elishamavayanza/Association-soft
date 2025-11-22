package com.org.testApi.config;

import org.flywaydb.core.api.callback.Callback;
import org.flywaydb.core.api.callback.Context;
import org.flywaydb.core.api.callback.Event;
import org.springframework.stereotype.Component;

import java.sql.Statement;

@Component
public class SQLiteFlywayConfig implements Callback {

    @Override
    public boolean supports(Event event, Context context) {
        // We want to support the "before connect" event to set PRAGMA statements
        // In newer Flyway versions, we use BEFORE_CONNECT instead of AFTER_CONNECT
        return Event.BEFORE_CONNECT.equals(event);
    }

    @Override
    public boolean canHandleInTransaction(Event event, Context context) {
        // PRAGMA statements cannot be handled in a transaction
        return false;
    }

    @Override
    public void handle(Event event, Context context) {
        try (Statement statement = context.getConnection().createStatement()) {
            // Enable foreign key constraints for SQLite
            statement.execute("PRAGMA foreign_keys = ON");
        } catch (Exception e) {
            System.err.println("Failed to enable foreign key constraints: " + e.getMessage());
        }
    }
    
    @Override
    public String getCallbackName() {
        return "SQLitePragmaCallback";
    }
}