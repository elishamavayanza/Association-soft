package com.org.testApi;

import com.org.testApi.models.Activity;
import com.org.testApi.models.Member;
import com.org.testApi.models.User;
import com.org.testApi.repository.ActivityRepository;
import com.org.testApi.repository.MemberRepository;
import com.org.testApi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ActivityParticipantsTest {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testActivityWithMemberAndUserParticipants() {
        // Create test user
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user = userRepository.save(user);

        // Create test member
        Member member = new Member();
        member.setMemberCode("TEST-001");
        member.setFirstName("Test");
        member.setLastName("Member");
        member.setEmail("member@example.com");
        member.setUser(user);
        member = memberRepository.save(member);

        // Create test activity
        Activity activity = new Activity();
        activity.setTitle("Test Activity");
        activity.setDescription("Test activity for participants");
        
        // Save activity
        activity = activityRepository.save(activity);

        // Add member as participant
        List<Member> memberParticipants = new ArrayList<>();
        memberParticipants.add(member);
        activity.setMemberParticipants(memberParticipants);
        
        // Add user as participant
        List<User> userParticipants = new ArrayList<>();
        userParticipants.add(user);
        activity.setUserParticipants(userParticipants);
        
        // Save updated activity
        activity = activityRepository.save(activity);

        // Verify participants were added
        Activity savedActivity = activityRepository.findById(activity.getId()).orElse(null);
        assertNotNull(savedActivity);
        assertEquals(1, savedActivity.getMemberParticipants().size());
        assertEquals(1, savedActivity.getUserParticipants().size());
        assertEquals(member.getId(), savedActivity.getMemberParticipants().get(0).getId());
        assertEquals(user.getId(), savedActivity.getUserParticipants().get(0).getId());
    }
}