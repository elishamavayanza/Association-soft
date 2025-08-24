package com.org.testApi.controllers;

import com.org.testApi.models.MemberRoleHistory;
import com.org.testApi.payload.MemberRoleHistoryPayload;
import com.org.testApi.services.MemberRoleHistoryService;
import com.org.testApi.mapper.MemberRoleHistoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/member-role-histories")
public class MemberRoleHistoryController {

    @Autowired
    private MemberRoleHistoryService memberRoleHistoryService;

    @Autowired
    private MemberRoleHistoryMapper memberRoleHistoryMapper;

    @GetMapping
    public ResponseEntity<List<MemberRoleHistory>> getAllMemberRoleHistories() {
        List<MemberRoleHistory> memberRoleHistories = memberRoleHistoryService.getAllMemberRoleHistories();
        return ResponseEntity.ok(memberRoleHistories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberRoleHistory> getMemberRoleHistoryById(@PathVariable Long id) {
        return memberRoleHistoryService.getMemberRoleHistoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MemberRoleHistory> createMemberRoleHistory(@RequestBody MemberRoleHistory memberRoleHistory) {
        MemberRoleHistory savedMemberRoleHistory = memberRoleHistoryService.saveMemberRoleHistory(memberRoleHistory);
        return ResponseEntity.ok(savedMemberRoleHistory);
    }

    @PostMapping("/payload")
    public ResponseEntity<MemberRoleHistory> createMemberRoleHistoryFromPayload(@RequestBody MemberRoleHistoryPayload payload) {
        MemberRoleHistory memberRoleHistory = memberRoleHistoryMapper.toEntityFromPayload(payload);
        MemberRoleHistory savedMemberRoleHistory = memberRoleHistoryService.saveMemberRoleHistory(memberRoleHistory);
        return ResponseEntity.ok(savedMemberRoleHistory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MemberRoleHistory> updateMemberRoleHistory(@PathVariable Long id, @RequestBody MemberRoleHistory memberRoleHistory) {
        try {
            MemberRoleHistory updatedMemberRoleHistory = memberRoleHistoryService.updateMemberRoleHistory(id, memberRoleHistory);
            return ResponseEntity.ok(updatedMemberRoleHistory);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/payload")
    public ResponseEntity<MemberRoleHistory> updateMemberRoleHistoryWithPayload(@PathVariable Long id, @RequestBody MemberRoleHistoryPayload payload) {
        return memberRoleHistoryService.getMemberRoleHistoryById(id)
                .map(memberRoleHistory -> {
                    memberRoleHistoryMapper.updateEntityFromPayload(payload, memberRoleHistory);
                    MemberRoleHistory updatedMemberRoleHistory = memberRoleHistoryService.updateMemberRoleHistory(id, memberRoleHistory);
                    return ResponseEntity.ok(updatedMemberRoleHistory);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMemberRoleHistory(@PathVariable Long id) {
        memberRoleHistoryService.deleteMemberRoleHistory(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/soft")
    public ResponseEntity<Void> softDeleteMemberRoleHistory(@PathVariable Long id) {
        memberRoleHistoryService.softDeleteMemberRoleHistory(id);
        return ResponseEntity.noContent().build();
    }
}
