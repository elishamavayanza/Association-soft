-- Add foreign key columns to members table
-- These columns reference other entities in the system

-- Add user_id column
ALTER TABLE members ADD COLUMN user_id INTEGER;

-- Add association_id column
ALTER TABLE members ADD COLUMN association_id INTEGER;

-- Add indexes for foreign key columns
CREATE INDEX IF NOT EXISTS idx_members_user_id ON members(user_id);
CREATE INDEX IF NOT EXISTS idx_members_association_id ON members(association_id);