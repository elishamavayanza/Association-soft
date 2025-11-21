-- Create loan_types table with all fields as defined in the LoanType entity
CREATE TABLE IF NOT EXISTS loan_types (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP,
    created_by VARCHAR(255),
    last_modified_by VARCHAR(255),
    version INTEGER,
    is_active BOOLEAN DEFAULT TRUE,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    monthly_interest_rate DECIMAL(10,4),
    max_penalty_rate DECIMAL(10,4),
    grace_period_days INTEGER,
    category VARCHAR(255) NOT NULL,
    sub_category VARCHAR(255),
    active BOOLEAN DEFAULT TRUE
);

-- Create indexes
CREATE INDEX IF NOT EXISTS idx_loan_types_name ON loan_types(name);
CREATE INDEX IF NOT EXISTS idx_loan_types_category ON loan_types(category);