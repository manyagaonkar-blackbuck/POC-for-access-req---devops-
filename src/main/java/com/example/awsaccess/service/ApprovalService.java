package com.example.awsaccess.service;

import com.example.awsaccess.model.Approval;
import com.example.awsaccess.repository.ApprovalRepository;
import org.springframework.stereotype.Service;

@Service
public class ApprovalService {

    private final ApprovalRepository approvalRepository;

    public ApprovalService(ApprovalRepository approvalRepository) {
        this.approvalRepository = approvalRepository;
    }

    public void createInitialApprovals(Long requestId) {

        Approval manager = new Approval();
        manager.setAccessRequestId(requestId);
        manager.setApproverRole("MANAGER");
        manager.setStatus("PENDING");
        approvalRepository.save(manager);

        Approval devops = new Approval();
        devops.setAccessRequestId(requestId);
        devops.setApproverRole("DEVOPS");
        devops.setStatus("PENDING");
        approvalRepository.save(devops);
    }

    public void managerApprove(Long requestId, String approver, String comment) {
        Approval approval = approvalRepository
                .findByAccessRequestId(requestId)
                .stream()
                .filter(a -> a.getApproverRole().equals("MANAGER"))
                .findFirst()
                .orElseThrow();

        approval.setStatus("APPROVED");
        approvalRepository.save(approval);
    }

    public void devopsApprove(Long requestId, String approver, String comment) {
        Approval approval = approvalRepository
                .findByAccessRequestId(requestId)
                .stream()
                .filter(a -> a.getApproverRole().equals("DEVOPS"))
                .findFirst()
                .orElseThrow();

        approval.setStatus("APPROVED");
        approvalRepository.save(approval);
    }
}
