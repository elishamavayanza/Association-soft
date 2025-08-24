package com.org.testApi.controllers;

import com.org.testApi.models.Activity;
import com.org.testApi.payload.ActivityPayload;
import com.org.testApi.services.ActivityService;
import com.org.testApi.mapper.ActivityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ActivityMapper activityMapper;

    @GetMapping
    public ResponseEntity<List<Activity>> getAllActivities() {
        List<Activity> activities = activityService.getAllActivities();
        return ResponseEntity.ok(activities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Activity> getActivityById(@PathVariable Long id) {
        return activityService.getActivityById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Activity> createActivity(@RequestBody Activity activity) {
        Activity savedActivity = activityService.saveActivity(activity);
        return ResponseEntity.ok(savedActivity);
    }

    @PostMapping("/payload")
    public ResponseEntity<Activity> createActivityFromPayload(@RequestBody ActivityPayload payload) {
        Activity activity = activityMapper.toEntityFromPayload(payload);
        Activity savedActivity = activityService.saveActivity(activity);
        return ResponseEntity.ok(savedActivity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Activity> updateActivity(@PathVariable Long id, @RequestBody Activity activity) {
        try {
            Activity updatedActivity = activityService.updateActivity(id, activity);
            return ResponseEntity.ok(updatedActivity);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/payload")
    public ResponseEntity<Activity> updateActivityWithPayload(@PathVariable Long id, @RequestBody ActivityPayload payload) {
        return activityService.getActivityById(id)
                .map(activity -> {
                    activityMapper.updateEntityFromPayload(payload, activity);
                    Activity updatedActivity = activityService.updateActivity(id, activity);
                    return ResponseEntity.ok(updatedActivity);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivity(@PathVariable Long id) {
        activityService.deleteActivity(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/soft")
    public ResponseEntity<Void> softDeleteActivity(@PathVariable Long id) {
        activityService.softDeleteActivity(id);
        return ResponseEntity.noContent().build();
    }
}
