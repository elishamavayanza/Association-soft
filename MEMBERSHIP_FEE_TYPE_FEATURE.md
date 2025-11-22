# Membership Fee Type Feature

## Overview
This feature adds a "type" field to the membership fees (cotisations) to categorize them as weekly, monthly, yearly, or per installment. This allows users to better organize and filter membership fees based on their payment frequency.

## Implementation Details

### 1. Added MembershipFeeType Enum
Created a new enum `MembershipFeeType` with the following values:
- WEEKLY ("par semaine")
- MONTHLY ("par mois")
- YEARLY ("par an")
- INSTALLMENT ("par dose")

### 2. Updated MembershipFee Entity
Added a new field `feeType` of type `MembershipFeeType` to the MembershipFee entity:
```java
@Enumerated(EnumType.STRING)
@Column(name = "fee_type")
private MembershipFeeType feeType;
```

### 3. Updated DTO and Payload Classes
- Updated `MembershipFeeDTO` to include the feeType field
- Updated `MembershipFeePayload` to include the feeType field

### 4. Updated Mapper
Updated `MembershipFeeMapper` to handle the mapping of the new feeType field between entities and DTOs/payloads.

### 5. Updated API Documentation
Updated the OpenAPI examples in the controller to include the new feeType field in request/response examples.

### 6. Database Migration
Created a new Flyway migration script `V6__add_membership_fee_type.sql` to add the fee_type column to the membership_fees table:
```sql
ALTER TABLE membership_fees ADD COLUMN fee_type VARCHAR(20);
```

## API Usage

The feeType field can now be used in all existing membership fee endpoints:

### Create a membership fee with type
POST /api/membership-fees
```json
{
  "amount": 50.00,
  "currency": "CDF",
  "paymentDate": "2025-09-27",
  "startDate": "2025-10-01",
  "endDate": "2026-09-30",
  "feeType": "MONTHLY",
  "paymentMethod": "CASH",
  "reference": "COT-2025-001",
  "member": {
    "id": 1
  }
}
```

### Get membership fees by type
GET /api/membership-fees (filtering by type would need to be implemented in the service layer)

### Update membership fee type
PUT /api/membership-fees/{id}
```json
{
  "feeType": "YEARLY"
}
```

## Possible Values for feeType
- WEEKLY: "par semaine"
- MONTHLY: "par mois"
- YEARLY: "par an"
- INSTALLMENT: "par dose"