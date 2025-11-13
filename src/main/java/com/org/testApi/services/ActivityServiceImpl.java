package com.org.testApi.services;

import com.org.testApi.models.Activity;
import com.org.testApi.models.Member;
import com.org.testApi.models.User;
import com.org.testApi.repository.ActivityRepository;
import com.org.testApi.repository.MemberRepository;
import com.org.testApi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ActivityServiceImpl implements ActivityService {

    private static final Logger logger = LoggerFactory.getLogger(ActivityServiceImpl.class);

    @Autowired
    private ActivityRepository activityRepository;
    
    @Autowired
    private MemberRepository memberRepository;
    
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
       return activityRepository.findByUserParticipantsId(userId);
    }

    @Override
    public List<Activity> getActivitiesByMemberId(Long memberId) {
       return activityRepository.findByMemberParticipantsId(memberId);
    }

    @Override
    public Activity addMemberParticipants(Long activityId, List<Long> memberIds) {
        Optional<Activity> activityOpt = activityRepository.findById(activityId);
        if (activityOpt.isPresent()) {
            Activity activity = activityOpt.get();
            
            // Fetch members from repository
            List<Member> membersToAdd = memberRepository.findAllById(memberIds);
            
            // Add members to participants (avoiding duplicates)
            for (Member member : membersToAdd) {
                if (!activity.getMemberParticipants().contains(member)) {
                    activity.getMemberParticipants().add(member);
                }
            }
            
            return activityRepository.save(activity);
        }
        throw new RuntimeException("Activity not found with id: " + activityId);
    }

    @Override
    public Activity removeMemberParticipants(Long activityId, List<Long> memberIds) {
        Optional<Activity> activityOpt = activityRepository.findById(activityId);
        if (activityOpt.isPresent()) {
            Activity activity = activityOpt.get();
            
            // Remove members from participants
            activity.getMemberParticipants().removeIf(member -> memberIds.contains(member.getId()));
            
            return activityRepository.save(activity);
        }
        throw new RuntimeException("Activity not found with id: " + activityId);
    }
    
    @Override
    public Activity addUserParticipants(Long activityId, List<Long> userIds) {
        Optional<Activity> activityOpt = activityRepository.findById(activityId);
        if (activityOpt.isPresent()) {
            Activity activity = activityOpt.get();
            
            // Fetch users from repository
            List<User> usersToAdd = userRepository.findAllById(userIds);
            
            // Add users to participants (avoiding duplicates)
            for (User user : usersToAdd) {
                if (!activity.getUserParticipants().contains(user)) {
                    activity.getUserParticipants().add(user);
                }
            }
            
            return activityRepository.save(activity);
        }
        throw new RuntimeException("Activity not found with id: " + activityId);
    }

    @Override
    public Activity removeUserParticipants(Long activityId, List<Long> userIds) {
        Optional<Activity> activityOpt = activityRepository.findById(activityId);
        if (activityOpt.isPresent()) {
            Activity activity = activityOpt.get();
            
            // Remove users from participants
            activity.getUserParticipants().removeIf(user -> userIds.contains(user.getId()));
            
            return activityRepository.save(activity);
        }
        throw new RuntimeException("Activity not found with id: " + activityId);
    }
    
    @Override
    public List<Member> getMemberParticipantsByActivityId(Long activityId) {
        Optional<Activity> activityOpt = activityRepository.findById(activityId);
        if (activityOpt.isPresent()) {
            return activityOpt.get().getMemberParticipants();
        }
        throw new RuntimeException("Activity not found with id: " + activityId);
    }
    
    @Override
    public List<User> getUserParticipantsByActivityId(Long activityId) {
        Optional<Activity> activityOpt = activityRepository.findById(activityId);
        if (activityOpt.isPresent()) {
            return activityOpt.get().getUserParticipants();
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