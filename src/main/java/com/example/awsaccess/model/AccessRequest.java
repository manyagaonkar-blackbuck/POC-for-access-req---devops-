package com.example.awsaccess.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "access_requests")
public class AccessRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String requesterEmail;

    @Column(nullable = false)
    private String reason;

    @Column(columnDefinition = "TEXT")
    private String services; // JSON as String (POC)

    @Column(columnDefinition = "TEXT")
    private String resourceArns; // JSON as String (POC)

    @Column(nullable = false)
    private String status;

    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.status = "DRAFT";
    }

    // getters and setters (we’ll add later)
}

