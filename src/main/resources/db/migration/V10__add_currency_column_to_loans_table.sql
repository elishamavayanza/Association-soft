-- Add currency column to existing loans table
ALTER TABLE loans ADD COLUMN currency VARCHAR(3) NOT NULL DEFAULT 'CDF';