package com.example.awsaccess.controller;

import com.example.awsaccess.service.ApprovalService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/approvals")
public class ApprovalController {

    private final ApprovalService approvalService;

    public ApprovalController(ApprovalService approvalService) {
        this.approvalService = approvalService;
    }

    @PostMapping("/{requestId}/manager/approve")
    public void managerApprove(
            @PathVariable Long requestId,
            @RequestBody Map<String, String> body
    ) {
        approvalService.managerApprove(
                requestId,
                body.get("approver"),
                body.get("comment")
        );
    }

    @PostMapping("/{requestId}/devops/approve")
    public void devopsApprove(
            @PathVariable Long requestId,
            @RequestBody Map<String, String> body
    ) {
        approvalService.devopsApprove(
                requestId,
                body.get("approver"),
                body.get("comment")
        );
    }
}
