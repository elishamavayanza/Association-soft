# New Membership Fee Endpoints for Type-based Filtering

## Overview
This document describes the new endpoints added to support filtering membership fees by type, improving the user experience for managing membership fees.

## New Endpoints

### GET /api/membership-fees/type/{feeType}
Retrieves all membership fees of a specific type.

#### Path Parameters
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
GET /api/membership-fees/type/MONTHLY
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

## Existing Endpoints with Enhanced Type Support

All existing endpoints now fully support the `feeType` field:

### POST /api/membership-fees
Create a new membership fee with a specific type.

### PUT /api/membership-fees/{id}
Update an existing membership fee, including changing its type.

### GET /api/membership-fees
Retrieve all membership fees (all types included).

### GET /api/membership-fees/{id}
Retrieve a specific membership fee by ID.

### DELETE /api/membership-fees/{id}
Delete a membership fee permanently.

### DELETE /api/membership-fees/{id}/soft
Soft delete a membership fee.

## Usage Examples

1. **Create a monthly membership fee**:
   POST to `/api/membership-fees` with `"feeType": "MONTHLY"` in the payload

2. **Find all yearly membership fees**:
   GET `/api/membership-fees/type/YEARLY`

3. **Change a membership fee from monthly to yearly**:
   PUT to `/api/membership-fees/{id}` with `"feeType": "YEARLY"` in the payload

4. **Delete a specific weekly membership fee**:
   DELETE `/api/membership-fees/{id}` where the fee has type WEEKLY

## Notes
- The feeType field is optional when creating or updating membership fees
- All existing functionality remains unchanged
- The new endpoint provides a more efficient way to filter membership fees by type compared to retrieving all fees and filtering on the client side