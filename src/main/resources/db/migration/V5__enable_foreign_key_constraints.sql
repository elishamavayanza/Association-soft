-- Enable foreign key constraints for SQLite
-- This needs to be done at the beginning of each database session

-- Add foreign key constraints to members table
-- Since SQLite doesn't support adding foreign key constraints to existing tables,
-- we need to use a different approach

-- We can add the constraints when creating the table, but since the table already exists,
-- we'll need to recreate it with the constraints, which is a complex operation
-- For now, we'll just enable foreign key checking

-- Note: PRAGMA foreign_keys = ON is now handled by the Flyway callback
-- in SQLiteFlywayConfig.java to avoid transaction issues