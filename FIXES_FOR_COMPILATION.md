# Fixes for Compilation Issues

## Issues Identified

1. **ExchangeRateMapper compilation error**: Unknown property "id" in result type ExchangeRate.ExchangeRateBuilder
2. **Builder annotation warnings**: Fields with default values need @Builder.Default annotation
3. **MapStruct errors**: Multiple mapper implementations failed due to ExchangeRateMapper issue
4. **ExchangeRateController error**: Incorrect use of Optional.map() method

## Fixes Applied

### 1. Fixed ExchangeRateMapper

**Problem**: The mapper was trying to map the "id" field which doesn't exist in the ExchangeRate entity (it's in the BaseEntity superclass).

**Solution**: Removed the mapping for "id" field in ExchangeRateMapper:

```java
@Mapper
public interface ExchangeRateMapper extends BaseMapper<ExchangeRate, ExchangeRateDTO> {
    ExchangeRateMapper INSTANCE = Mappers.getMapper(ExchangeRateMapper.class);

    @Override
    @Mapping(target = "fromCurrency", source = "fromCurrency")
    @Mapping(target = "toCurrency", source = "toCurrency")
    @Mapping(target = "rate", source = "rate")
    @Mapping(target = "lastUpdated", source = "lastUpdated")
    ExchangeRateDTO toDto(ExchangeRate entity);

    @Override
    @Mapping(target = "fromCurrency", source = "fromCurrency")
    @Mapping(target = "toCurrency", source = "toCurrency")
    @Mapping(target = "rate", source = "rate")
    @Mapping(target = "lastUpdated", source = "lastUpdated")
    ExchangeRate toEntity(ExchangeRateDTO dto);
}
```

### 2. Fixed Builder Annotations

**Problem**: Lombok @Builder was ignoring initializing expressions for fields with default values.

**Solution**: Added @Builder.Default annotation to fields with default values:

#### Round.java
```java
@Enumerated(EnumType.STRING)
@Column(name = "status")
@Builder.Default
private RoundStatus status = RoundStatus.UPCOMING;
```

#### RotatingGroup.java
```java
@Enumerated(EnumType.STRING)
@Column(name = "status")
@Builder.Default
private GroupStatus status = GroupStatus.ACTIVE;

@Column(name = "auto_generate_rounds")
@Builder.Default
private Boolean autoGenerateRounds = Boolean.TRUE;
```

#### Contribution.java
```java
@Enumerated(EnumType.STRING)
@Column(name = "status")
@Builder.Default
private ContributionStatus status = ContributionStatus.PENDING;
```

### 3. Fixed ExchangeRateController

**Problem**: Incorrect use of Optional.map() method in getExchangeRate endpoint.

**Solution**: Fixed the method call with proper lambda expression:

```java
@GetMapping("/rate")
public ResponseEntity<BigDecimal> getExchangeRate(
        @RequestParam Currency from,
        @RequestParam Currency to) {
    
    return exchangeRateService.getExchangeRate(from, to)
            .map(rate -> ResponseEntity.ok(rate))  // Fixed: proper lambda expression
            .orElse(ResponseEntity.notFound().build());
}
```

### 4. Entities Updated with Currency Support

All the following entities were updated with currency fields:
- FinancialTransaction
- MembershipFee
- Contribution
- RotatingGroup
- Round

Each with proper @Builder.Default annotation for the currency field.

### 5. DTOs and Payloads Updated

All corresponding DTOs and Payloads were updated to include currency fields:
- FinancialTransactionDTO / FinancialTransactionPayload
- MembershipFeeDTO / MembershipFeePayload
- ContributionDTO / ContributionPayload
- RotatingGroupDTO / RotatingGroupPayload
- RoundDTO / RoundPayload

### 6. Mappers Updated

All corresponding mappers were updated to handle currency fields:
- FinancialTransactionMapper
- MembershipFeeMapper
- ContributionMapper
- RotatingGroupMapper
- RoundMapper

## Expected Result

After applying these fixes, the compilation should succeed without errors. The MapStruct errors were cascading from the initial ExchangeRateMapper issue, so fixing that should resolve all the mapper implementation errors.

## Testing

Once compiled successfully, the following functionality should work:
1. Exchange rate management via REST API
2. Currency conversion for all financial components
3. Multi-currency support in Membership Fees, Contributions, Rotating Groups, and Rounds
4. Default currency (CDF) applied correctly to all new entities

# Fix for Membership Fee Compilation Issue

## Problem
The project was failing to compile with the following error:
```
ERROR] /media/elishama/New Volume/project/Test/testApi/src/main/java/com/org/testApi/models/MembershipFee.java:[9,8] duplicate class: MembershipFee
```

## Root Cause
The MembershipFee.java file was missing the package declaration and import statements, which caused the Java compiler to not recognize it as part of the correct package and led to compilation errors.

## Solution
I've added the missing package declaration and import statements to the MembershipFee.java file:

1. Added package declaration: `package com.org.testApi.models;`

2. Added required import statements:
   - `import jakarta.persistence.*;`
   - `import lombok.*;`
   - `import java.math.BigDecimal;`
   - `import java.time.LocalDate;`

3. Ensured the class properly extends BaseEntity

4. Verified all other related files (DTO, Payload, Mapper, Controller, Service, Repository) were correctly referencing the MembershipFee class

## Files Modified
- `/media/elishama/New Volume/project/Test/testApi/src/main/java/com/org/testApi/models/MembershipFee.java` - Added package and import statements

## Additional Implementation
As part of the original task, we also implemented the membership fee type feature:
1. Created MembershipFeeType enum with values: WEEKLY, MONTHLY, YEARLY, INSTALLMENT
2. Added feeType field to MembershipFee entity
3. Updated DTO and Payload classes to include feeType
4. Updated mapper to handle feeType mapping
5. Created database migration script to add fee_type column

The project should now compile successfully with the membership fee type feature fully implemented.
