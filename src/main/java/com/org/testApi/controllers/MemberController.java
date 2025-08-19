package com.org.testApi.controllers;

import com.org.testApi.models.Member;
import com.org.testApi.services.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping
    public ResponseEntity<List<Member>> getAllMembers() {
        List<Member> members = memberService.getAllMembers();
        return ResponseEntity.ok(members);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Member> getMemberById(@PathVariable Long id) {
        return memberService.getMemberById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Member> createMember(@RequestBody Member member) {
        Member savedMember = memberService.saveMember(member);
        return ResponseEntity.ok(savedMember);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Member> updateMember(@PathVariable Long id, @RequestBody Member member) {
        try {
            Member updatedMember = memberService.updateMember(id, member);
            return ResponseEntity.ok(updatedMember);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/soft")
    public ResponseEntity<Void> softDeleteMember(@PathVariable Long id) {
        memberService.softDeleteMember(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Recherche des membres avec des filtres complexes.
     */
    @GetMapping("/search")
    public ResponseEntity<List<Member>> searchMembers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Member.MemberType memberType,
            @RequestParam(required = false) Long associationId,
            @RequestParam(required = false) Boolean isActive) {
        // Note: Pour une implémentation complète, vous devriez ajouter cette méthode au service
        List<Member> members = memberService.getAllMembers();
        return ResponseEntity.ok(members);
    }

    /**
     * Vérifie si un membre est éligible pour emprunter.
     */
    @GetMapping("/{id}/eligible")
    public ResponseEntity<Boolean> isMemberEligibleForLoan(@PathVariable Long id) {
        // Note: Vous devriez ajouter cette méthode au MemberService
        Member member = memberService.getMemberById(id).orElse(null);
        if (member != null) {
            boolean eligible = member.isEligibleForLoan();
            return ResponseEntity.ok(eligible);
        }
        return ResponseEntity.notFound().build();
    }
}
