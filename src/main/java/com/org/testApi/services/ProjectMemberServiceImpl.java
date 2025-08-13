package com.org.testApi.services;

import com.org.testApi.models.ProjectMember;
import com.org.testApi.repository.ProjectMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Service
public class ProjectMemberServiceImpl implements ProjectMemberService {

    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    private List<Observer<ProjectMember>> observers = new ArrayList<>();

    @Override
    public List<ProjectMember> getAllProjectMembers() {
        return projectMemberRepository.findAll();
    }

    @Override
    public Optional<ProjectMember> getProjectMemberById(Long id) {
        return projectMemberRepository.findById(id);
    }

    @Override
    public ProjectMember saveProjectMember(ProjectMember projectMember) {
        ProjectMember savedProjectMember = projectMemberRepository.save(projectMember);
        notifyObservers("SAVE", savedProjectMember);
        return savedProjectMember;
    }

    @Override
    public ProjectMember updateProjectMember(Long id, ProjectMember projectMember) {
        if (projectMemberRepository.existsById(id)) {
            projectMember.setId(id);
            ProjectMember updatedProjectMember = projectMemberRepository.save(projectMember);
            notifyObservers("UPDATE", updatedProjectMember);
            return updatedProjectMember;
        }
        throw new RuntimeException("ProjectMember not found with id: " + id);
    }

    @Override
    public void deleteProjectMember(Long id) {
        ProjectMember projectMember = projectMemberRepository.findById(id).orElse(null);
        projectMemberRepository.deleteById(id);
        if (projectMember != null) {
            notifyObservers("DELETE", projectMember);
        }
    }

    @Override
    public void softDeleteProjectMember(Long id) {
        ProjectMember projectMember = projectMemberRepository.findById(id).orElse(null);
        projectMemberRepository.softDelete(id);
        if (projectMember != null) {
            notifyObservers("SOFT_DELETE", projectMember);
        }
    }

    @Override
    public void addObserver(Observer<ProjectMember> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<ProjectMember> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String event, ProjectMember entity) {
        for (Observer<ProjectMember> observer : observers) {
            observer.update(event, entity);
        }
    }
}
