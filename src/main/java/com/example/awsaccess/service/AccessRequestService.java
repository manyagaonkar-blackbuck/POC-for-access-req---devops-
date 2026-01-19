package com.example.awsaccess.service;

import com.example.awsaccess.llm.LlmClient;
import com.example.awsaccess.llm.LlmRequest;
import com.example.awsaccess.llm.LlmResponse;
import com.example.awsaccess.model.AccessRequest;
import com.example.awsaccess.model.FollowupQuestion;
import com.example.awsaccess.repository.AccessRequestRepository;
import com.example.awsaccess.repository.FollowupQuestionRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccessRequestService {

    private final AccessRequestRepository accessRequestRepository;
    private final FollowupQuestionRepository followupQuestionRepository;
    private final LlmClient llmClient;

    private static final int MAX_FOLLOWUPS = 5;

    public AccessRequestService(
            AccessRequestRepository accessRequestRepository,
            FollowupQuestionRepository followupQuestionRepository,
            LlmClient llmClient
    ) {
        this.accessRequestRepository = accessRequestRepository;
        this.followupQuestionRepository = followupQuestionRepository;
        this.llmClient = llmClient;
    }

    /**
     * Step 1: Create access request and call LLM
     */
    public LlmResponse createRequest(String requesterEmail, String reason) {

        AccessRequest request = new AccessRequest();
        request.setRequesterEmail(requesterEmail);
        request.setReason(reason);

        accessRequestRepository.save(request);

        LlmRequest llmRequest = new LlmRequest();
        llmRequest.setReason(reason);

        LlmResponse llmResponse = llmClient.process(llmRequest);

        if (llmResponse.isNeedFollowup()) {
            createFollowup(request.getId(), llmResponse.getFollowupQuestion());
        } else {
            finalizeRequest(request, llmResponse);
        }

        return llmResponse;
    }

    /**
     * Step 2: Handle follow-up answer and loop until satisfied
     */
    public LlmResponse answerFollowup(Long requestId, Long followupId, Map<String, Object> answer) {

        FollowupQuestion followup = followupQuestionRepository
                .findById(followupId)
                .orElseThrow(() -> new RuntimeException("Follow-up not found"));

        followup.setAnswer(answer.toString());
        followup.setStatus("ANSWERED");
        followupQuestionRepository.save(followup);

        List<FollowupQuestion> allFollowups =
                followupQuestionRepository.findByAccessRequestId(requestId);

        if (allFollowups.size() > MAX_FOLLOWUPS) {
            throw new RuntimeException("Too many follow-up attempts");
        }

        Map<String, Object> resolvedResources = new HashMap<>();
        for (FollowupQuestion fq : allFollowups) {
            if (fq.getAnswer() != null) {
                resolvedResources.put("S3", fq.getAnswer());
            }
        }

        LlmRequest llmRequest = new LlmRequest();
        llmRequest.setResolvedResources(resolvedResources);

        LlmResponse llmResponse = llmClient.process(llmRequest);

        if (llmResponse.isNeedFollowup()) {
            createFollowup(requestId, llmResponse.getFollowupQuestion());
        } else {
            AccessRequest request = accessRequestRepository
                    .findById(requestId)
                    .orElseThrow(() -> new RuntimeException("Request not found"));

            finalizeRequest(request, llmResponse);
        }

        return llmResponse;
    }

    private void createFollowup(Long requestId, String question) {
        FollowupQuestion followup = new FollowupQuestion();
        followup.setAccessRequestId(requestId);
        followup.setQuestion(question);
        followupQuestionRepository.save(followup);
    }

    private void finalizeRequest(AccessRequest request, LlmResponse llmResponse) {
        request.setServices(llmResponse.getServices().toString());
        request.setResourceArns(
                llmResponse.getResolvedResources() != null
                        ? llmResponse.getResolvedResources().toString()
                        : null
        );
        request.setStatus("CREATED");
        accessRequestRepository.save(request);
    }
}

