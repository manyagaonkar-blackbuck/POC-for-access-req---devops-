package com.example.awsaccess.dto.request;

public class ApprovalDecisionDto {

    private String decision; // APPROVED or REJECTED
    private String reason;

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
