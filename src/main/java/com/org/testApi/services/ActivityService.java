package com.org.testApi.services;

import com.org.testApi.models.Activity;
import com.org.testApi.models.Member;
import com.org.testApi.models.User;
import java.util.List;
import java.util.Optional;

public interface ActivityService extends ObservableService<Activity> {
    List<Activity> getAllActivities();
    Optional<Activity> getActivityById(Long id);
    Activity saveActivity(Activity activity);
    Activity updateActivity(Long id, Activity activity);
    void deleteActivity(Long id);
    void softDeleteActivity(Long id);
    List<Activity> getActivitiesByAssociationId(Long associationId);
    List<Activity> getActivitiesByProjectId(Long projectId);
    List<Activity> getActivitiesByUserId(Long userId);
    List<Activity> getActivitiesByMemberId(Long memberId);
    
    // Methods to work with Member participants
    Activity addMemberParticipants(Long activityId, List<Long> memberIds);
    Activity removeMemberParticipants(Long activityId, List<Long> memberIds);
    
    // Methods to work with User participants
    Activity addUserParticipants(Long activityId, List<Long> userIds);
    Activity removeUserParticipants(Long activityId, List<Long> userIds);
    
    // Methods to get participants
    List<Member> getMemberParticipantsByActivityId(Long activityId);
    List<User> getUserParticipantsByActivityId(Long activityId);
}