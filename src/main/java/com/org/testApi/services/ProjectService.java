package com.org.testApi.services;

import com.org.testApi.models.Project;
import java.util.List;
import java.util.Optional;

public interface ProjectService extends ObservableService<Project> {
    List<Project> getAllProjects();
    Optional<Project> getProjectById(Long id);
    Project saveProject(Project project);
    Project updateProject(Long id, Project project);
    void deleteProject(Long id);
    void softDeleteProject(Long id);
}
