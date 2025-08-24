package com.org.testApi.controllers;

import com.org.testApi.models.MembershipFee;
import com.org.testApi.payload.MembershipFeePayload;
import com.org.testApi.services.MembershipFeeService;
import com.org.testApi.mapper.MembershipFeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/membership-fees")
public class MembershipFeeController {

    @Autowired
    private MembershipFeeService membershipFeeService;

    @Autowired
    private MembershipFeeMapper membershipFeeMapper;

    @GetMapping
    public ResponseEntity<List<MembershipFee>> getAllMembershipFees() {
        List<MembershipFee> membershipFees = membershipFeeService.getAllMembershipFees();
        return ResponseEntity.ok(membershipFees);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MembershipFee> getMembershipFeeById(@PathVariable Long id) {
        return membershipFeeService.getMembershipFeeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MembershipFee> createMembershipFee(@RequestBody MembershipFee membershipFee) {
        MembershipFee savedMembershipFee = membershipFeeService.saveMembershipFee(membershipFee);
        return ResponseEntity.ok(savedMembershipFee);
    }

    @PostMapping("/payload")
    public ResponseEntity<MembershipFee> createMembershipFeeFromPayload(@RequestBody MembershipFeePayload payload) {
        MembershipFee membershipFee = membershipFeeMapper.toEntityFromPayload(payload);
        MembershipFee savedMembershipFee = membershipFeeService.saveMembershipFee(membershipFee);
        return ResponseEntity.ok(savedMembershipFee);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MembershipFee> updateMembershipFee(@PathVariable Long id, @RequestBody MembershipFee membershipFee) {
        try {
            MembershipFee updatedMembershipFee = membershipFeeService.updateMembershipFee(id, membershipFee);
            return ResponseEntity.ok(updatedMembershipFee);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/payload")
    public ResponseEntity<MembershipFee> updateMembershipFeeWithPayload(@PathVariable Long id, @RequestBody MembershipFeePayload payload) {
        return membershipFeeService.getMembershipFeeById(id)
                .map(membershipFee -> {
                    membershipFeeMapper.updateEntityFromPayload(payload, membershipFee);
                    MembershipFee updatedMembershipFee = membershipFeeService.updateMembershipFee(id, membershipFee);
                    return ResponseEntity.ok(updatedMembershipFee);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMembershipFee(@PathVariable Long id) {
        membershipFeeService.deleteMembershipFee(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/soft")
    public ResponseEntity<Void> softDeleteMembershipFee(@PathVariable Long id) {
        membershipFeeService.softDeleteMembershipFee(id);
        return ResponseEntity.noContent().build();
    }
}
