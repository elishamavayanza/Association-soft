package com.org.testApi.controllers;

import com.org.testApi.models.ProjectMember;
import com.org.testApi.payload.ProjectMemberPayload;
import com.org.testApi.services.ProjectMemberService;
import com.org.testApi.mapper.ProjectMemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/project-members")
public class ProjectMemberController {

    @Autowired
    private ProjectMemberService projectMemberService;

    @Autowired
    private ProjectMemberMapper projectMemberMapper;

    @GetMapping
    public ResponseEntity<List<ProjectMember>> getAllProjectMembers() {
        List<ProjectMember> projectMembers = projectMemberService.getAllProjectMembers();
        return ResponseEntity.ok(projectMembers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectMember> getProjectMemberById(@PathVariable Long id) {
        return projectMemberService.getProjectMemberById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProjectMember> createProjectMember(@RequestBody ProjectMember projectMember) {
        ProjectMember savedProjectMember = projectMemberService.saveProjectMember(projectMember);
        return ResponseEntity.ok(savedProjectMember);
    }

    @PostMapping("/payload")
    public ResponseEntity<ProjectMember> createProjectMemberFromPayload(@RequestBody ProjectMemberPayload payload) {
        ProjectMember projectMember = projectMemberMapper.toEntityFromPayload(payload);
        ProjectMember savedProjectMember = projectMemberService.saveProjectMember(projectMember);
        return ResponseEntity.ok(savedProjectMember);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectMember> updateProjectMember(@PathVariable Long id, @RequestBody ProjectMember projectMember) {
        try {
            ProjectMember updatedProjectMember = projectMemberService.updateProjectMember(id, projectMember);
            return ResponseEntity.ok(updatedProjectMember);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/payload")
    public ResponseEntity<ProjectMember> updateProjectMemberWithPayload(@PathVariable Long id, @RequestBody ProjectMemberPayload payload) {
        return projectMemberService.getProjectMemberById(id)
                .map(projectMember -> {
                    projectMemberMapper.updateEntityFromPayload(payload, projectMember);
                    ProjectMember updatedProjectMember = projectMemberService.updateProjectMember(id, projectMember);
                    return ResponseEntity.ok(updatedProjectMember);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProjectMember(@PathVariable Long id) {
        projectMemberService.deleteProjectMember(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/soft")
    public ResponseEntity<Void> softDeleteProjectMember(@PathVariable Long id) {
        projectMemberService.softDeleteProjectMember(id);
        return ResponseEntity.noContent().build();
    }
}
