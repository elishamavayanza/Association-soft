package com.org.testApi.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "members")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "association_id", nullable = false)
    @ToString.Exclude
    private Association association;

    private LocalDate joinDate;
    private LocalDate leaveDate;

    @Enumerated(EnumType.STRING)
    private MemberType type = MemberType.REGULAR;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    private List<MembershipFee> fees = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    private List<MemberRoleHistory> roleHistory = new ArrayList<>();

    @Column(columnDefinition = "boolean default false")
    private boolean isAdmin;

    @PrePersist
    protected void onCreate() {
        if (this.joinDate == null) {
            this.joinDate = LocalDate.now();
        }
    }

    public boolean isActive() {
        return leaveDate == null;
    }

    public enum MemberType {
        REGULAR, HONORARY, BENEFACTOR, VOLUNTEER, BOARD_MEMBER
    }
}

