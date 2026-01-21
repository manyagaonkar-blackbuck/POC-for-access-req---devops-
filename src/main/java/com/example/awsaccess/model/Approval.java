package com.example.awsaccess.model;

import jakarta.persistence.*;

@Entity
@Table(name="approvals")
public class Approval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="access_request_id", nullable=false)
    private Long accessRequestId;

    @Column(name="approver_role", nullable=false)
    private String approverRole;

    @Column(nullable=false)
    private String status;

    public Long getId() {
        return id;
    }
    public Long getAccessRequestId() {
        return accessRequestId;
    }
    public void setAccessRequestId(Long accessRequestId) {
        this.accessRequestId = accessRequestId;
    }

    public String getApproverRole() {
        return approverRole;
    }
    public void setApproverRole(String approverRole) {
        this.approverRole = approverRole;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
