# Membership Fee SMS Notifications

## Overview

This feature automatically sends SMS notifications to members when a new membership fee (cotisation) is created. The notification includes details about the payment such as amount, currency, date, and type.

## Implementation Details

### How It Works

1. When a membership fee is created via either endpoint:
   - `POST /api/membership-fees`
   - `POST /api/membership-fees/payload`

2. The MembershipFeeService saves the fee and notifies all registered observers.

3. The MembershipFeeNotificationService, which is registered as an observer, receives the notification.

4. If the member has a phone number registered, an SMS notification is sent with the details of the payment.

### Notification Content

The SMS notification includes:
- Member's name
- Amount paid
- Currency
- Payment date
- Fee type (if specified)

Example message:
```
Cher(e) John Doe,

Nous vous confirmons que votre cotisation de 100 CDF a été enregistrée avec succès le 2025-11-17.
Type de cotisation: par mois

Merci pour votre contribution.
```

### Technical Implementation

1. **MembershipFeeNotificationService**: A service that implements the Observer pattern to listen for membership fee events.

2. **NotificationService**: Used to send the actual SMS notification via the `sendSmsNotification` method.

3. **Automatic Registration**: The observer is automatically registered with the MembershipFeeService using the `@PostConstruct` annotation.

## Requirements

For SMS notifications to work:
1. The member must have a user account linked
2. The user must have a phone number registered
3. The SMS service must be properly configured in the NotificationService implementation

## Testing

Unit tests have been created to verify:
1. SMS notifications are sent when all requirements are met
2. No notifications are sent when phone number is missing

## Future Improvements

1. Add configuration options for notification templates
2. Support for multiple languages in notifications
3. Add email notifications as a backup communication method
4. Add notification preferences for members