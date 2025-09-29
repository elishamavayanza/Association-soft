package com.org.testApi.repository.custom;

import com.org.testApi.models.Activity;
import java.util.List;

public interface ActivityRepositoryCustom {
    List<Activity> findActiveActivitiesWithParticipantsCount();

    List<Activity> searchActivitiesComplexQuery(String title, String location, Activity.ActivityStatus status);

    void updateActivityStats(Long activityId);
    
    void softDeleteActivity(Activity activity);
}