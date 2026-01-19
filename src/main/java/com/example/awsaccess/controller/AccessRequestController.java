package com.example.awsaccess.controller;

import com.example.awsaccess.dto.request.CreateAccessRequestDto;
import com.example.awsaccess.dto.response.LlmResponseDto;
import com.example.awsaccess.llm.LlmResponse;
import com.example.awsaccess.service.AccessRequestService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/access-requests")
public class AccessRequestController {

    private final AccessRequestService accessRequestService;

    public AccessRequestController(AccessRequestService accessRequestService) {
        this.accessRequestService = accessRequestService;
    }

    @PostMapping
    public LlmResponseDto createRequest(@RequestBody CreateAccessRequestDto dto) {

        LlmResponse response =
                accessRequestService.createRequest(dto.getRequesterEmail(), dto.getReason());

        return map(response);
    }

    private LlmResponseDto map(LlmResponse response) {
        LlmResponseDto dto = new LlmResponseDto();
        dto.setNeedFollowup(response.isNeedFollowup());
        dto.setFollowupQuestion(response.getFollowupQuestion());
        dto.setServices(response.getServices());
        dto.setActionGroups(response.getActionGroups());
        dto.setResolvedResources(response.getResolvedResources());
        return dto;
    }
}
