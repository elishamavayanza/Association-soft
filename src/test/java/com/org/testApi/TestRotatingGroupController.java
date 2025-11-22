package com.org.testApi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.testApi.models.Member;
import com.org.testApi.models.RotatingGroup;
import com.org.testApi.payload.ResponsePayload;
import com.org.testApi.repository.MemberRepository;
import com.org.testApi.repository.RotatingGroupRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
public class TestRotatingGroupController {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private RotatingGroupRepository rotatingGroupRepository;

    @MockBean
    private MemberRepository memberRepository;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testAddMembersToGroup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // Create a mock rotating group
        RotatingGroup group = new RotatingGroup();
        group.setId(1L);
        group.setName("Test Group");
        group.setContributionAmount(new BigDecimal("10000"));
        group.setMaxMembers(10);
        group.setMembers(new ArrayList<>());

        // Create mock members
        Member member1 = new Member();
        member1.setId(1L);

        Member member2 = new Member();
        member2.setId(2L);

        List<Member> members = new ArrayList<>();
        members.add(member1);
        members.add(member2);

        // Mock repository behavior
        when(rotatingGroupRepository.findById(1L)).thenReturn(Optional.of(group));
        when(memberRepository.findAllById(any())).thenReturn(members);

        // Create request body with member IDs
        List<Long> memberIds = new ArrayList<>();
        memberIds.add(1L);
        memberIds.add(2L);

        // Perform the request
        mockMvc.perform(post("/api/rotating-groups/{groupId}/members", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberIds)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true));
    }
}