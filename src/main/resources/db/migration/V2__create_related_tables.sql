-- Create related tables that are referenced by members table

-- Create users table if it doesn't exist
CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP,
    created_by VARCHAR(255),
    last_modified_by VARCHAR(255),
    version INTEGER,
    username VARCHAR(255),
    email VARCHAR(255),
    password VARCHAR(255),
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    phone_number VARCHAR(255),
    photo VARCHAR(255),
    profile_photo_path VARCHAR(255),
    last_login TIMESTAMP,
    is_active BOOLEAN
);

-- Create associations table if it doesn't exist
CREATE TABLE IF NOT EXISTS associations (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP,
    created_by VARCHAR(255),
    last_modified_by VARCHAR(255),
    version INTEGER,
    name VARCHAR(255),
    description TEXT,
    location VARCHAR(255),
    legal_status VARCHAR(255),
    siret VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(255),
    is_active BOOLEAN
);