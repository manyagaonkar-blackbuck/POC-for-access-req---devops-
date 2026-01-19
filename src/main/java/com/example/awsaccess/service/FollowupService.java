package com.example.awsaccess.service;

import com.example.awsaccess.model.FollowupQuestion;
import com.example.awsaccess.repository.FollowupQuestionRepository;
import org.springframework.stereotype.Service;

@Service
public class FollowupService {

    private final FollowupQuestionRepository followupQuestionRepository;

    public FollowupService(FollowupQuestionRepository followupQuestionRepository) {
        this.followupQuestionRepository = followupQuestionRepository;
    }

    public FollowupQuestion getActiveFollowup(Long requestId) {
        return followupQuestionRepository.findByAccessRequestId(requestId)
                .stream()
                .filter(f -> "ASKED".equals(f.getStatus()))
                .findFirst()
                .orElse(null);
    }
}

