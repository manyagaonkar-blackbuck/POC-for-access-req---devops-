package com.example.awsaccess.controller;

import com.example.awsaccess.dto.request.ApprovalDecisionDto;
import com.example.awsaccess.model.AccessRequest;
import com.example.awsaccess.repository.AccessRequestRepository;
import com.example.awsaccess.service.ApprovalService;
import com.example.awsaccess.service.PolicyGenerationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/access-requests")
public class ApprovalController {

    private final ApprovalService approvalService;
    private final PolicyGenerationService policyGenerationService;
    private final AccessRequestRepository accessRequestRepository;

    public ApprovalController(
            ApprovalService approvalService,
            PolicyGenerationService policyGenerationService,
            AccessRequestRepository accessRequestRepository
    ) {
        this.approvalService = approvalService;
        this.policyGenerationService = policyGenerationService;
        this.accessRequestRepository = accessRequestRepository;
    }

    // MANAGER APPROVAL
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

    // DEVOPS APPROVAL + POLICY GENERATION
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

        AccessRequest request = accessRequestRepository
                .findById(requestId)
                .orElseThrow();

        String policy = policyGenerationService.generateIamPolicy(request);
        String cli = policyGenerationService.generateAwsCliCommand(requestId);

        return policy + "\n\n" + cli;
    }

    // FINAL STEP — MARK ACCESS GRANTED
    @PostMapping("/{requestId}/mark-granted")
    public String markGranted(@PathVariable Long requestId) {

        AccessRequest request = accessRequestRepository
                .findById(requestId)
                .orElseThrow();

        if (!"DEVOPS_APPROVED".equals(request.getStatus())) {
            throw new RuntimeException("Request not approved by DevOps");
        }

        request.setStatus("ACCESS_GRANTED");
        accessRequestRepository.save(request);

        return "Access marked as granted";
    }
}
