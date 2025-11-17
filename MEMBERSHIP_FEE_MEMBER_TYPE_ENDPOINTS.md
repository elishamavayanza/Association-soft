# New Membership Fee Endpoints for Member and Type-based Operations

## Overview
This document describes the new endpoints added to support retrieving and deleting membership fees by member ID and type, improving the user experience for managing membership fees.

## New Endpoints

### GET /api/membership-fees/member/{memberId}/type/{feeType}
Retrieves all membership fees of a specific type for a specific member.

#### Path Parameters
- `memberId` (long, required): The ID of the member
- `feeType` (string, required): The type of membership fee to retrieve. 
  Valid values:
  - `WEEKLY` - Cotisations hebdomadaires ("par semaine")
  - `MONTHLY` - Cotisations mensuelles ("par mois")
  - `YEARLY` - Cotisations annuelles ("par an")
  - `INSTALLMENT` - Cotisations par versement ("par dose")

#### Responses
- `200`: Successfully retrieved membership fees list
- `400`: Invalid fee type provided
- `500`: Internal server error

#### Example Request
```
GET /api/membership-fees/member/1/type/MONTHLY
```

#### Example Response
```json
[
  {
    "id": 1,
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
]
```

### DELETE /api/membership-fees/member/{memberId}/type/{feeType}
Permanently deletes all membership fees of a specific type for a specific member.

#### Path Parameters
- `memberId` (long, required): The ID of the member
- `feeType` (string, required): The type of membership fee to delete. 
  Valid values:
  - `WEEKLY` - Cotisations hebdomadaires ("par semaine")
  - `MONTHLY` - Cotisations mensuelles ("par mois")
  - `YEARLY` - Cotisations annuelles ("par an")
  - `INSTALLMENT` - Cotisations par versement ("par dose")

#### Responses
- `204`: Successfully deleted membership fees
- `400`: Invalid fee type provided
- `500`: Internal server error

#### Example Request
```
DELETE /api/membership-fees/member/1/type/MONTHLY
```

### DELETE /api/membership-fees/member/{memberId}/type/{feeType}/soft
Soft deletes all membership fees of a specific type for a specific member.

#### Path Parameters
- `memberId` (long, required): The ID of the member
- `feeType` (string, required): The type of membership fee to soft delete. 
  Valid values:
  - `WEEKLY` - Cotisations hebdomadaires ("par semaine")
  - `MONTHLY` - Cotisations mensuelles ("par mois")
  - `YEARLY` - Cotisations annuelles ("par an")
  - `INSTALLMENT` - Cotisations par versement ("par dose")

#### Responses
- `204`: Successfully soft deleted membership fees
- `400`: Invalid fee type provided
- `500`: Internal server error

#### Example Request
```
DELETE /api/membership-fees/member/1/type/MONTHLY/soft
```

## Usage Examples

1. **Find all monthly membership fees for member with ID 1**:
   GET `/api/membership-fees/member/1/type/MONTHLY`

2. **Delete all monthly membership fees for member with ID 1**:
   DELETE `/api/membership-fees/member/1/type/MONTHLY`

3. **Soft delete all yearly membership fees for member with ID 2**:
   DELETE `/api/membership-fees/member/2/type/YEARLY/soft`

## Notes
- These endpoints provide a more efficient way to manage membership fees by member and type
- All existing functionality remains unchanged
- The delete operations will notify observers of the DELETE or SOFT_DELETE events for each membership fee