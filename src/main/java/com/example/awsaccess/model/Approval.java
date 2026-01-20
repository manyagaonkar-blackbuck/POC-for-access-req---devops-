package com.example.awsaccess.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "approvals")
public class Approval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long accessRequestId;

    @Column(nullable = false)
    private String approverRole; // MANAGER or DEVOPS

    @Column(nullable = false)
    private String decision; // APPROVED or REJECTED

    @Column(columnDefinition = "TEXT")
    private String reason;

    private LocalDateTime decidedAt;

    @PrePersist
    public void onCreate() {
        this.decidedAt = LocalDateTime.now();
    }
}
