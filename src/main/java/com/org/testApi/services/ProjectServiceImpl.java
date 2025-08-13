package com.org.testApi.services;

import com.org.testApi.models.Project;
import com.org.testApi.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    private List<Observer<Project>> observers = new ArrayList<>();

    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Override
    public Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id);
    }

    @Override
    public Project saveProject(Project project) {
        Project savedProject = projectRepository.save(project);
        notifyObservers("SAVE", savedProject);
        return savedProject;
    }

    @Override
    public Project updateProject(Long id, Project project) {
        if (projectRepository.existsById(id)) {
            project.setId(id);
            Project updatedProject = projectRepository.save(project);
            notifyObservers("UPDATE", updatedProject);
            return updatedProject;
        }
        throw new RuntimeException("Project not found with id: " + id);
    }

    @Override
    public void deleteProject(Long id) {
        Project project = projectRepository.findById(id).orElse(null);
        projectRepository.deleteById(id);
        if (project != null) {
            notifyObservers("DELETE", project);
        }
    }

    @Override
    public void softDeleteProject(Long id) {
        Project project = projectRepository.findById(id).orElse(null);
        projectRepository.softDelete(id);
        if (project != null) {
            notifyObservers("SOFT_DELETE", project);
        }
    }

    @Override
    public void addObserver(Observer<Project> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<Project> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String event, Project entity) {
        for (Observer<Project> observer : observers) {
            observer.update(event, entity);
        }
    }
}
