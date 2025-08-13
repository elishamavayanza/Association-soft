package com.org.testApi.services;

import com.org.testApi.models.Activity;
import com.org.testApi.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    private List<Observer<Activity>> observers = new ArrayList<>();

    @Override
    public List<Activity> getAllActivities() {
        return activityRepository.findAll();
    }

    @Override
    public Optional<Activity> getActivityById(Long id) {
        return activityRepository.findById(id);
    }

    @Override
    public Activity saveActivity(Activity activity) {
        Activity savedActivity = activityRepository.save(activity);
        notifyObservers("SAVE", savedActivity);
        return savedActivity;
    }

    @Override
    public Activity updateActivity(Long id, Activity activity) {
        if (activityRepository.existsById(id)) {
            activity.setId(id);
            Activity updatedActivity = activityRepository.save(activity);
            notifyObservers("UPDATE", updatedActivity);
            return updatedActivity;
        }
        throw new RuntimeException("Activity not found with id: " + id);
    }

    @Override
    public void deleteActivity(Long id) {
        Activity activity = activityRepository.findById(id).orElse(null);
        activityRepository.deleteById(id);
        if (activity != null) {
            notifyObservers("DELETE", activity);
        }
    }

    @Override
    public void softDeleteActivity(Long id) {
        Activity activity = activityRepository.findById(id).orElse(null);
        activityRepository.softDelete(id);
        if (activity != null) {
            notifyObservers("SOFT_DELETE", activity);
        }
    }

    @Override
    public void addObserver(Observer<Activity> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<Activity> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String event, Activity entity) {
        for (Observer<Activity> observer : observers) {
            observer.update(event, entity);
        }
    }
}
