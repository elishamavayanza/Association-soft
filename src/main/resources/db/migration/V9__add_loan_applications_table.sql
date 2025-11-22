-- Create loan_applications table with all fields as defined in the LoanApplication entity
CREATE TABLE IF NOT EXISTS loan_applications (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP,
    created_by VARCHAR(255),
    last_modified_by VARCHAR(255),
    version INTEGER,
    is_active BOOLEAN DEFAULT TRUE,
    member_id INTEGER NOT NULL,
    loan_type_id INTEGER NOT NULL,
    requested_amount DECIMAL(15,2),
    approved_amount DECIMAL(15,2),
    application_date DATE,
    processed_date DATE,
    purpose TEXT,
    status VARCHAR(255),
    FOREIGN KEY (member_id) REFERENCES members(id),
    FOREIGN KEY (loan_type_id) REFERENCES loan_types(id)
);

-- Create indexes for foreign key columns
CREATE INDEX IF NOT EXISTS idx_loan_applications_member_id ON loan_applications(member_id);
CREATE INDEX IF NOT EXISTS idx_loan_applications_loan_type_id ON loan_applications(loan_type_id);