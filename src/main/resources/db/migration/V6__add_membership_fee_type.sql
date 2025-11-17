-- Add fee_type column to membership_fees table
-- This migration adds the type field to categorize membership fees (weekly, monthly, yearly, etc.)

ALTER TABLE membership_fees ADD COLUMN fee_type VARCHAR(20);