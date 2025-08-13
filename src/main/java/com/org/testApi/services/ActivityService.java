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
}
