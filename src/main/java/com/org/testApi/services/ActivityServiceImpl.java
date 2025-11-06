package com.org.testApi.services;

import com.org.testApi.models.Activity;
import com.org.testApi.models.User;
import com.org.testApi.repository.ActivityRepository;
import com.org.testApi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private UserRepository userRepository;

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
    @Transactional
    public Activity saveActivity(Activity activity) {
        Activity savedActivity = activityRepository.save(activity);
        notifyObservers("SAVE", savedActivity);
        return savedActivity;
    }

    @Override
    @Transactional
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
    @Transactional
    public void deleteActivity(Long id) {
        Activity activity = activityRepository.findById(id).orElse(null);
        activityRepository.deleteById(id);
        if (activity != null) {
            notifyObservers("DELETE", activity);
        }
    }

    @Override
    @Transactional
    public void softDeleteActivity(Long id) {
        Activity activity = activityRepository.findById(id).orElse(null);
        if (activity != null) {
            try {
                activityRepository.softDeleteActivity(activity);
                notifyObservers("SOFT_DELETE", activity);
            } catch (Exception e) {
                throw new RuntimeException("Error soft deleting activity with id: " + id, e);
            }
        }
    }

    @Override
    public List<Activity> getActivitiesByAssociationId(Long associationId) {
        return activityRepository.findByAssociationId(associationId);
    }

    @Override
    public List<Activity> getActivitiesByProjectId(Long projectId) {
        return activityRepository.findByProjectId(projectId);
    }

    @Override
    public List<Activity> getActivitiesByUserId(Long userId) {
       return activityRepository.findByParticipantsId(userId);
    }

    @Override
    @Transactional
    public Activity addParticipants(Long activityId, List<Long> userIds) {
        Optional<Activity> activityOpt = activityRepository.findById(activityId);
        if (activityOpt.isPresent()) {
            Activity activity = activityOpt.get();
            
            // Fetch users from repository
            List<User> usersToAdd = userRepository.findAllById(userIds);
            
            // Add users to participants (avoiding duplicates)
            for (User user : usersToAdd) {
                if (!activity.getParticipants().contains(user)) {
                    activity.getParticipants().add(user);
                }
            }
            return activityRepository.save(activity);
        }
        throw new RuntimeException("Activity not found with id: " + activityId);
    }

    @Override
    @Transactional
    public Activity removeParticipants(Long activityId, List<Long> userIds) {
        Optional<Activity> activityOpt = activityRepository.findById(activityId);
        if (activityOpt.isPresent()) {
            Activity activity = activityOpt.get();
            
            // Remove users from participants
            activity.getParticipants().removeIf(user -> userIds.contains(user.getId()));
            
            return activityRepository.save(activity);
        }
        throw new RuntimeException("Activity not found with id: " + activityId);
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