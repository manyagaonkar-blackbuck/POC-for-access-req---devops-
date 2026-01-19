package com.example.awsaccess.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "followup_questions")
public class FollowupQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long accessRequestId;

    @Column(nullable = false)
    private String question;

    @Column(columnDefinition = "TEXT")
    private String answer;

    @Column(nullable = false)
    private String status; // ASKED / ANSWERED

    private LocalDateTime createdAt;
    private LocalDateTime answeredAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.status = "ASKED";
    }

    // getters and setters (later)
}

