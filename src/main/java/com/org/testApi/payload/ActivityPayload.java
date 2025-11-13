package com.org.testApi.payload;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ActivityPayload extends BasePayload {
    private Long id;
    private String title;
    private String description;
    private String type;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String location;
    private Boolean deleted;
    private Long associationId;
    private Long projectId;
    private Long creatorId;
    private Long[] memberParticipantIds;
    private Long[] userParticipantIds;
    private String status;
    
    // Manually adding missing getter methods to fix compilation errors
    public Long getId() {
        return this.id;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public String getType() {
        return this.type;
    }
    
    public LocalDateTime getStartDateTime() {
        return this.startDateTime;
    }
    
    public LocalDateTime getEndDateTime() {
        return this.endDateTime;
    }
    
    public String getLocation() {
        return this.location;
    }
    
    public Boolean getDeleted() {
        return this.deleted;
    }
    
    public Long getAssociationId() {
        return this.associationId;
    }
    
    public Long getProjectId() {
        return this.projectId;
    }
    
    public Long getCreatorId() {
        return this.creatorId;
    }
    
    public Long[] getMemberParticipantIds() {
        return this.memberParticipantIds;
    }
    
    public Long[] getUserParticipantIds() {
        return this.userParticipantIds;
    }
    
    public String getStatus() {
        return this.status;
    }
    
    // Manually adding missing setter methods
    public void setId(Long id) {
        this.id = id;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }
    
    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
    
    public void setAssociationId(Long associationId) {
        this.associationId = associationId;
    }
    
    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
    
    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }
    
    public void setMemberParticipantIds(Long[] memberParticipantIds) {
        this.memberParticipantIds = memberParticipantIds;
    }
    
    public void setUserParticipantIds(Long[] userParticipantIds) {
        this.userParticipantIds = userParticipantIds;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
}