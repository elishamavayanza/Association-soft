package com.org.testApi;

import com.org.testApi.controllers.RotatingController;
import com.org.testApi.dto.ContributionDTO;
import com.org.testApi.mapper.ContributionMapper;
import com.org.testApi.models.Contribution;
import com.org.testApi.payload.ContributionPayload;
import com.org.testApi.services.RotatingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TestRotatingController {

    @Autowired
    private RotatingController rotatingController;

    @MockBean
    private RotatingService rotatingService;

    @MockBean
    private ContributionMapper contributionMapper;

    @Test
    public void testMakeContributionWithPayload() {
        // Given
        ContributionPayload payload = new ContributionPayload();
        payload.setMemberId(1L);
        payload.setRoundId(1L);
        payload.setAmount(BigDecimal.valueOf(10000));
        payload.setContributionDate(LocalDate.of(2025, 11, 15));

        Contribution contribution = new Contribution();
        contribution.setId(1L);
        contribution.setAmount(BigDecimal.valueOf(10000));
        contribution.setContributionDate(LocalDate.of(2025, 11, 15));

        ContributionDTO contributionDTO = new ContributionDTO();
        contributionDTO.setId(1L);
        contributionDTO.setAmount(BigDecimal.valueOf(10000));
        contributionDTO.setContributionDate(LocalDate.of(2025, 11, 15));

        when(rotatingService.makeContribution(any(), any(), any(), any())).thenReturn(contribution);
        when(contributionMapper.toDTO((Contribution) any())).thenReturn(contributionDTO);

        // When
        ResponseEntity<?> response = rotatingController.makeContributionWithPayload(payload);

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ContributionDTO);
    }
}