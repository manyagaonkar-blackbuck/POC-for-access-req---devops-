package com.example.awsaccess.controller;

import com.example.awsaccess.dto.request.FollowupAnswerDto;
import com.example.awsaccess.dto.response.LlmResponseDto;
import com.example.awsaccess.llm.LlmResponse;
import com.example.awsaccess.service.AccessRequestService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/access-requests")
public class FollowupController {

    private final AccessRequestService accessRequestService;

    public FollowupController(AccessRequestService accessRequestService) {
        this.accessRequestService = accessRequestService;
    }

    @PostMapping("/{requestId}/followups/{followupId}")
    public LlmResponseDto answerFollowup(
            @PathVariable Long requestId,
            @PathVariable Long followupId,
            @RequestBody FollowupAnswerDto dto
    ) {

        LlmResponse response =
                accessRequestService.answerFollowup(requestId, followupId, dto.getAnswer());

        LlmResponseDto out = new LlmResponseDto();
        out.setNeedFollowup(response.isNeedFollowup());
        out.setFollowupQuestion(response.getFollowupQuestion());
        out.setServices(response.getServices());
        out.setActionGroups(response.getActionGroups());
        out.setResolvedResources(response.getResolvedResources());
        return out;
    }
}
