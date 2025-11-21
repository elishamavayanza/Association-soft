-- Create loans table with all fields as defined in the Loan entity
CREATE TABLE IF NOT EXISTS loans (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP,
    created_by VARCHAR(255),
    last_modified_by VARCHAR(255),
    version INTEGER,
    is_active BOOLEAN DEFAULT TRUE,
    member_id INTEGER NOT NULL,
    document_id INTEGER,
    loan_type_id INTEGER,
    loan_application_id INTEGER,
    amount DECIMAL(19,2),
    currency VARCHAR(3) NOT NULL DEFAULT 'CDF',
    interest_rate DECIMAL(10,4),
    penalty_rate DECIMAL(10,4),
    loan_date DATE,
    due_date DATE,
    return_date DATE,
    amount_repaid DECIMAL(19,2),
    repayment_date DATE,
    status VARCHAR(20),
    deposit_amount DECIMAL(19,2),
    deposit_refunded BOOLEAN,
    notes TEXT,
    FOREIGN KEY (member_id) REFERENCES members(id),
    FOREIGN KEY (document_id) REFERENCES documents(id),
    FOREIGN KEY (loan_type_id) REFERENCES loan_types(id),
    FOREIGN KEY (loan_application_id) REFERENCES loan_applications(id)
);

-- Create indexes for foreign key columns
CREATE INDEX IF NOT EXISTS idx_loans_member_id ON loans(member_id);
CREATE INDEX IF NOT EXISTS idx_loans_document_id ON loans(document_id);
CREATE INDEX IF NOT EXISTS idx_loans_loan_type_id ON loans(loan_type_id);
CREATE INDEX IF NOT EXISTS idx_loans_loan_application_id ON loans(loan_application_id);