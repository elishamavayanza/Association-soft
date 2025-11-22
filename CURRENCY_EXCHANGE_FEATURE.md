# Currency Exchange Rate Feature

## Overview

This feature adds support for multiple currencies (USD, CDF, EUR) with automatic exchange rate conversion. CDF (Congolese Franc) is set as the default currency.

## Models Added

1. **Currency Enum**: Defines the supported currencies (USD, CDF, EUR)
2. **ExchangeRate**: Entity to store exchange rates between currency pairs

## Models Updated

The following existing models have been updated to include currency support:
1. **FinancialTransaction** - Financial transactions
2. **MembershipFee** - Member membership fees
3. **Contribution** - Likelemba system contributions
4. **RotatingGroup** - Likelemba rotating financial groups
5. **Round** - Rounds in rotating groups

## Services

1. **ExchangeRateService**: Manages exchange rates and performs currency conversions
2. **CurrencyConverter**: Utility class for easy currency conversion operations
3. **CurrencyConversionService**: Service for converting amounts in the rotating financial system

## Controllers

1. **ExchangeRateController**: REST API for managing exchange rates
2. **CurrencyDemoController**: Demo endpoints showing currency conversion usage

## API Endpoints

### Exchange Rate Management

- `GET /api/exchange-rates`: Get all exchange rates
- `POST /api/exchange-rates/update`: Update or create an exchange rate
- `GET /api/exchange-rates/rate`: Get exchange rate between two currencies
- `GET /api/exchange-rates/convert`: Convert amount between two currencies

### Currency Conversion Demo

- `GET /api/currency-demo/rates`: Get all exchange rates
- `GET /api/currency-demo/convert`: Convert amount between currencies
- `GET /api/currency-demo/convert-to-default`: Convert amount to default currency (CDF)

## Usage Examples

### Update Exchange Rate

```bash
curl -X POST "http://localhost:8080/api/exchange-rates/update?from=USD&to=CDF&rate=2000"
```

### Convert Currency

```bash
curl "http://localhost:8080/api/exchange-rates/convert?amount=100&from=USD&to=CDF"
```

### Get All Exchange Rates

```bash
curl "http://localhost:8080/api/exchange-rates"
```

## Financial Components Integration

The following financial components have been updated with currency support:

### FinancialTransaction
- Added `currency` field with default value of CDF
- DTO and Payload classes updated accordingly
- Mapper updated to handle currency field

### MembershipFee (Cotisations)
- Added `currency` field with default value of CDF
- DTO and Payload classes updated accordingly
- Mapper updated to handle currency field

### Contribution (Likelemba contributions)
- Added `currency` field with default value of CDF
- DTO and Payload classes updated accordingly
- Mapper updated to handle currency field

### RotatingGroup (Likelemba groups)
- Added `currency` field with default value of CDF
- DTO and Payload classes updated accordingly
- Mapper updated to handle currency field

### Round (Likelemba rounds)
- Added `currency` field
- DTO and Payload classes updated accordingly
- Mapper updated to handle currency field

## Default Exchange Rates

The system is initialized with default exchange rates:

- 1 USD = 2000 CDF
- 1 EUR = 2200 CDF
- 1 USD = 0.90 EUR

These can be updated through the API as needed.

## How to Use in Your Code

### Autowire Services

```java
@Autowired
private ExchangeRateService exchangeRateService;

@Autowired
private CurrencyConverter currencyConverter;

@Autowired
private CurrencyConversionService currencyConversionService;
```

### Convert Currency

```java
// Convert 100 USD to CDF
BigDecimal amountCDF = currencyConverter.convert(
    new BigDecimal("100"), 
    Currency.USD, 
    Currency.CDF
);
```

### Convert Financial Components

```java
// Convert membership fee to EUR
BigDecimal feeInEUR = currencyConversionService.convertMembershipFee(membershipFee, Currency.EUR);

// Convert contribution to USD
BigDecimal contributionInUSD = currencyConversionService.convertContribution(contribution, Currency.USD);

// Convert rotating group amount to CDF
BigDecimal groupAmountInCDF = currencyConversionService.convertRotatingGroupAmount(rotatingGroup, Currency.CDF);
```

### Get Exchange Rate

```java
Optional<BigDecimal> rate = exchangeRateService.getExchangeRate(Currency.USD, Currency.CDF);
```

## Database Schema

The feature adds a new table `exchange_rates` with the following columns:

- `id`: Primary key
- `from_currency`: Source currency (USD, CDF, EUR)
- `to_currency`: Target currency (USD, CDF, EUR)
- `rate`: Exchange rate value
- `last_updated`: Timestamp of last update

A unique constraint ensures only one rate per currency pair exists.

All existing financial tables (membership_fees, contributions, rotating_groups, rounds) have been updated with a `currency` column.

## Compilation Fixes

Several issues were fixed to ensure successful compilation:

1. **ExchangeRateMapper**: Removed mapping for "id" field which caused compilation errors
2. **Builder annotations**: Added @Builder.Default to fields with default values to prevent warnings
3. **MapStruct errors**: Fixed cascading errors from the ExchangeRateMapper issue

See [FIXES_FOR_COMPILATION.md](file:///media/elishama/New%20Volume/project/Test/testApi/FIXES_FOR_COMPILATION.md) for detailed information about the fixes applied.