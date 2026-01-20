package com.example.awsaccess.service;

import com.example.awsaccess.model.AccessRequest;
import com.example.awsaccess.model.Approval;
import com.example.awsaccess.repository.AccessRequestRepository;
import com.example.awsaccess.repository.ApprovalRepository;
import org.springframework.stereotype.Service;

@Service
public class ApprovalService {

    private final ApprovalRepository approvalRepository;
    private final AccessRequestRepository accessRequestRepository;

    public ApprovalService(ApprovalRepository approvalRepository,
                           AccessRequestRepository accessRequestRepository) {
        this.approvalRepository = approvalRepository;
        this.accessRequestRepository = accessRequestRepository;
    }

    public void managerApprove(Long requestId, String decision, String reason) {

        AccessRequest request = accessRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (!"CREATED".equals(request.getStatus())) {
            throw new RuntimeException("Request not in CREATED state");
        }

        saveApproval(requestId, "MANAGER", decision, reason);

        if ("APPROVED".equals(decision)) {
            request.setStatus("MANAGER_APPROVED");
        } else {
            request.setStatus("REJECTED");
        }

        accessRequestRepository.save(request);
    }

    public void devopsApprove(Long requestId, String decision, String reason) {

        AccessRequest request = accessRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (!"MANAGER_APPROVED".equals(request.getStatus())) {
            throw new RuntimeException("Request not in MANAGER_APPROVED state");
        }

        saveApproval(requestId, "DEVOPS", decision, reason);

        if ("APPROVED".equals(decision)) {
            request.setStatus("DEVOPS_APPROVED");
        } else {
            request.setStatus("REJECTED");
        }

        accessRequestRepository.save(request);
    }

    private void saveApproval(Long requestId, String role, String decision, String reason) {
        Approval approval = new Approval();
        approval.setAccessRequestId(requestId);
        approval.setApproverRole(role);
        approval.setDecision(decision);
        approval.setReason(reason);
        approvalRepository.save(approval);
    }
}
