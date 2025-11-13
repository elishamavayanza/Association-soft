package com.org.testApi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "members")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Member extends BaseEntity {

    /**
     * Code unique du membre au sein de l'association.
     * Ce code est généré automatiquement à la création du membre.
     */
    @Column(name = "member_code", unique = true)
    private String memberCode;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "photo", length = 255)
    private String photo;

    @Column(name = "join_date")
    private LocalDate joinDate;

    @Column(name = "leave_date")
    private LocalDate leaveDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    @Builder.Default
    private MemberType type = MemberType.REGULAR;

    @Column(name = "is_admin")
    private Boolean isAdmin;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "user_id", nullable = true)
    @ToString.Exclude
    @JsonIgnore
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "association_id", nullable = true)
    @ToString.Exclude
    @JsonIgnore
    private Association association;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    @JsonIgnore
    private List<MembershipFee> fees = new ArrayList<>();
    
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    @JsonIgnore
    private List<MemberRoleHistory> roleHistory = new ArrayList<>();
    
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    @JsonIgnore
    private List<Loan> loans = new ArrayList<>();

    @ManyToMany(mappedBy = "members", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    @JsonIgnore
    private List<RotatingGroup> rotatingGroups = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    @JsonIgnore
    private List<Contribution> contributions = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    @JsonIgnore
    private List<Penalty> penalties = new ArrayList<>();

    @ManyToMany(mappedBy = "beneficiaries", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    @JsonIgnore
    private List<Round> roundsAsBeneficiary = new ArrayList<>();
    
    @ManyToMany(mappedBy = "memberParticipants", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    @JsonIgnore
    private List<Activity> attendedActivities = new ArrayList<>();

    /**
     * Checks if the member is eligible for a loan.
     * A member is eligible if they are active, have paid fees, and have no overdue loans.
     *
     * @return true if the member is eligible for a loan, false otherwise
     */
    public boolean isEligibleForLoan() {
        // Check if member is active (has not left the association)
        if (this.leaveDate != null) {
            return false;
        }

        // Check if member has paid any fees
        if (this.fees == null || this.fees.isEmpty()) {
            return false;
        }

        // Check if member has any overdue loans
        if (this.loans != null) {
            boolean hasOverdueLoans = this.loans.stream()
                    .filter(loan -> loan != null)
                    .anyMatch(loan -> loan.getStatus() == Loan.LoanStatus.OVERDUE);
            if (hasOverdueLoans) {
                return false;
            }
        }

        return true;
    }
}