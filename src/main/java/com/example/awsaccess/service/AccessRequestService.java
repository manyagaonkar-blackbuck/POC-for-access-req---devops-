package com.example.awsaccess.service;

import com.example.awsaccess.llm.LlmClient;
import com.example.awsaccess.llm.LlmResponse;
import com.example.awsaccess.model.AccessRequest;
import com.example.awsaccess.model.FollowupQuestion;
import com.example.awsaccess.repository.AccessRequestRepository;
import com.example.awsaccess.repository.FollowupQuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AccessRequestService {

    private final AccessRequestRepository accessRequestRepository;
    private final FollowupQuestionRepository followupRepository;
    private final LlmClient llmClient;
    private final ApprovalService approvalService;

    public AccessRequestService(
            AccessRequestRepository accessRequestRepository,
            FollowupQuestionRepository followupRepository,
            LlmClient llmClient,
            ApprovalService approvalService
    ) {
        this.accessRequestRepository = accessRequestRepository;
        this.followupRepository = followupRepository;
        this.llmClient = llmClient;
        this.approvalService = approvalService;
    }

    public LlmResponse createRequest(String requesterEmail, String reason) {

        AccessRequest request = new AccessRequest();
        request.setRequesterEmail(requesterEmail);
        request.setReason(reason);
        request.setStatus("DRAFT");
        accessRequestRepository.save(request);

        LlmResponse llmResponse = llmClient.process(reason);

        if (llmResponse.isNeedFollowup()) {
            createFollowup(request.getId(), llmResponse.getFollowupQuestion());
        }

        return llmResponse;
    }

    public LlmResponse answerFollowup(
            Long requestId,
            Long followupId,
            Map<String, Object> answer
    ) {

        FollowupQuestion followup = followupRepository.findById(followupId)
                .orElseThrow(() -> new RuntimeException("Followup not found"));

        followup.setAnswer(answer.toString());
        followup.setStatus("ANSWERED");
        followupRepository.save(followup);

        AccessRequest request = accessRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        // FINALIZE REQUEST
        request.setResourceArns(answer.toString());
        request.setStatus("PENDING_MANAGER_APPROVAL");
        accessRequestRepository.save(request);

        // 🔥 THIS WILL NOW EXECUTE
        approvalService.createInitialApprovals(requestId);

        LlmResponse response = new LlmResponse();
        response.setNeedFollowup(false);
        response.setResolvedResources(answer);
        response.setServices(List.of("S3"));

        return response;
    }

    private void createFollowup(Long requestId, String question) {
        FollowupQuestion followup = new FollowupQuestion();
        followup.setAccessRequestId(requestId);
        followup.setQuestion(question);
        followup.setStatus("ASKED");
        followupRepository.save(followup);
    }
}
