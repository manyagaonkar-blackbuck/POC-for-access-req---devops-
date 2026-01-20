package com.example.awsaccess.controller;

import com.example.awsaccess.dto.request.ApprovalDecisionDto;
import com.example.awsaccess.service.ApprovalService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/access-requests")
public class ApprovalController {

    private final ApprovalService approvalService;

    public ApprovalController(ApprovalService approvalService) {
        this.approvalService = approvalService;
    }

    // Manager Approval
    @PostMapping("/{requestId}/manager-approval")
    public String managerApproval(
            @PathVariable Long requestId,
            @RequestBody ApprovalDecisionDto dto
    ) {
        approvalService.managerApprove(
                requestId,
                dto.getDecision(),
                dto.getReason()
        );
        return "Manager decision recorded";
    }

    // DevOps Approval
    @PostMapping("/{requestId}/devops-approval")
    public String devopsApproval(
            @PathVariable Long requestId,
            @RequestBody ApprovalDecisionDto dto
    ) {
        approvalService.devopsApprove(
                requestId,
                dto.getDecision(),
                dto.getReason()
        );
        return "DevOps decision recorded";
    }
}
