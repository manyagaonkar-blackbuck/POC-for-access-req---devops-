package com.example.awsaccess.service;

import com.example.awsaccess.model.Approval;
import com.example.awsaccess.repository.ApprovalRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ApprovalService {

    private final ApprovalRepository approvalRepository;

    public ApprovalService(ApprovalRepository approvalRepository) {
        this.approvalRepository = approvalRepository;
    }

    public void createInitialApprovals(Long requestId) {
        Approval m = new Approval();
        m.setAccessRequestId(requestId);
        m.setApproverRole("MANAGER");
        m.setStatus("PENDING");
        approvalRepository.save(m);

        Approval d = new Approval();
        d.setAccessRequestId(requestId);
        d.setApproverRole("DEVOPS");
        d.setStatus("PENDING");
        approvalRepository.save(d);
    }

    public void managerApprove(Long requestId, String approver, String comment) {
        List<Approval> list = approvalRepository.findByAccessRequestId(requestId);
        for (Approval a : list) {
            if ("MANAGER".equals(a.getApproverRole())) {
                a.setStatus("APPROVED");
                approvalRepository.save(a);
                break;
            }
        }
    }

    public void devopsApprove(Long requestId, String approver, String comment) {
        List<Approval> list = approvalRepository.findByAccessRequestId(requestId);
        for (Approval a : list) {
            if ("DEVOPS".equals(a.getApproverRole())) {
                a.setStatus("APPROVED");
                approvalRepository.save(a);
                break;
            }
        }
    }
}
