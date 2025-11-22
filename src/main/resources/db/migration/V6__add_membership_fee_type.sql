-- Create membership_fees table with all fields as defined in the MembershipFee entity
CREATE TABLE IF NOT EXISTS membership_fees (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP,
    created_by VARCHAR(255),
    last_modified_by VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    member_id INTEGER NOT NULL,
    amount DECIMAL(19,2) NOT NULL,
    currency VARCHAR(3) NOT NULL DEFAULT 'CDF',
    payment_date DATE NOT NULL,
    start_date DATE,
    end_date DATE,
    fee_type VARCHAR(20),
    transaction_id INTEGER,
    payment_method VARCHAR(20),
    reference VARCHAR(50),
    FOREIGN KEY (member_id) REFERENCES members(id),
    FOREIGN KEY (transaction_id) REFERENCES financial_transactions(id)
);

-- Create indexes for foreign key columns
CREATE INDEX IF NOT EXISTS idx_membership_fees_member_id ON membership_fees(member_id);
CREATE INDEX IF NOT EXISTS idx_membership_fees_transaction_id ON membership_fees(transaction_id);