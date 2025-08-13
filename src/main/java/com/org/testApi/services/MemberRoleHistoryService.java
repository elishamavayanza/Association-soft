package com.org.testApi.services;

import com.org.testApi.models.MemberRoleHistory;
import java.util.List;
import java.util.Optional;

public interface MemberRoleHistoryService extends ObservableService<MemberRoleHistory> {
    List<MemberRoleHistory> getAllMemberRoleHistories();
    Optional<MemberRoleHistory> getMemberRoleHistoryById(Long id);
    MemberRoleHistory saveMemberRoleHistory(MemberRoleHistory memberRoleHistory);
    MemberRoleHistory updateMemberRoleHistory(Long id, MemberRoleHistory memberRoleHistory);
    void deleteMemberRoleHistory(Long id);
    void softDeleteMemberRoleHistory(Long id);
}
