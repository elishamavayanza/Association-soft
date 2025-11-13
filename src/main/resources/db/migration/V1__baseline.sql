-- Baseline schema for existing database
-- This represents the initial state of the database before any migrations

-- Create the members table with all fields as defined in the Member entity
CREATE TABLE IF NOT EXISTS members (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP,
    version INTEGER,
    member_code VARCHAR(255),
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(255),
    address TEXT,
    photo VARCHAR(255),
    join_date DATE,
    leave_date DATE,
    type VARCHAR(255),
    is_admin BOOLEAN
);