package com.org.testApi.services;

import com.org.testApi.models.Activity;
import java.util.List;
import java.util.Optional;

public interface ActivityService extends ObservableService<Activity> {
    List<Activity> getAllActivities();
    Optional<Activity> getActivityById(Long id);
    Activity saveActivity(Activity activity);
    Activity updateActivity(Long id, Activity activity);
    void deleteActivity(Long id);
    void softDeleteActivity(Long id);
    
    // Added missing methods
    List<Activity> getActivitiesByAssociationId(Long associationId);
    List<Activity> getActivitiesByProjectId(Long projectId);
    List<Activity> getActivitiesByUserId(Long userId);
    Activity addParticipants(Long activityId, List<Long> userIds);
    Activity removeParticipants(Long activityId, List<Long> userIds);
}