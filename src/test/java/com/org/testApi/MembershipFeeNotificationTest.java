package com.org.testApi;

import com.org.testApi.models.Member;
import com.org.testApi.models.MembershipFee;
import com.org.testApi.models.User;
import com.org.testApi.services.MembershipFeeNotificationService;
import com.org.testApi.services.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.*;

public class MembershipFeeNotificationTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private MembershipFeeNotificationService membershipFeeNotificationService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSendMembershipFeeNotification() {
        // Create test data
        User user = new User();
        user.setPhoneNumber("+1234567890");
        user.setFirstName("John");
        user.setLastName("Doe");

        Member member = new Member();
        member.setUser(user);
        member.setFirstName("John");
        member.setLastName("Doe");

        MembershipFee fee = new MembershipFee();
        fee.setId(1L);
        fee.setMember(member);
        fee.setAmount(BigDecimal.valueOf(100));
        fee.setCurrency(com.org.testApi.models.Currency.CDF);
        fee.setPaymentDate(LocalDate.now());
        fee.setFeeType(com.org.testApi.models.MembershipFeeType.MONTHLY);

        // Test the notification
        membershipFeeNotificationService.update("SAVE", fee);

        // Verify that SMS notification was sent
        verify(notificationService, times(1))
            .sendSmsNotification(eq("+1234567890"), anyString());
    }

    @Test
    public void testNoNotificationForNullPhoneNumber() {
        // Create test data with null phone number
        User user = new User();
        user.setPhoneNumber(null);
        user.setFirstName("John");
        user.setLastName("Doe");

        Member member = new Member();
        member.setUser(user);
        member.setFirstName("John");
        member.setLastName("Doe");

        MembershipFee fee = new MembershipFee();
        fee.setId(1L);
        fee.setMember(member);
        fee.setAmount(BigDecimal.valueOf(100));
        fee.setCurrency(com.org.testApi.models.Currency.CDF);
        fee.setPaymentDate(LocalDate.now());
        fee.setFeeType(com.org.testApi.models.MembershipFeeType.MONTHLY);

        // Test the notification
        membershipFeeNotificationService.update("SAVE", fee);

        // Verify that no SMS notification was sent
        verify(notificationService, never())
            .sendSmsNotification(anyString(), anyString());
    }
}