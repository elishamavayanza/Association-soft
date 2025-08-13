package com.org.testApi.services;

import com.org.testApi.models.ProjectMember;
import java.util.List;
import java.util.Optional;

public interface ProjectMemberService extends ObservableService<ProjectMember> {
    List<ProjectMember> getAllProjectMembers();
    Optional<ProjectMember> getProjectMemberById(Long id);
    ProjectMember saveProjectMember(ProjectMember projectMember);
    ProjectMember updateProjectMember(Long id, ProjectMember projectMember);
    void deleteProjectMember(Long id);
    void softDeleteProjectMember(Long id);
}
