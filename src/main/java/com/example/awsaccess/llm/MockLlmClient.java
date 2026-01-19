package com.example.awsaccess.llm;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class MockLlmClient implements LlmClient {

    @Override
    public LlmResponse process(LlmRequest request) {

        LlmResponse response = new LlmResponse();

        // Always assume S3 for POC
        response.setServices(List.of("S3"));
        response.setActionGroups(List.of(
                "READ_OBJECTS",
                "UPLOAD_OBJECTS",
                "DELETE_OBJECTS"
        ));

        // If resources are missing → ask follow-up
        if (request.getResolvedResources() == null || request.getResolvedResources().isEmpty()) {

            response.setNeedFollowup(true);
            response.setFollowupQuestion(
                    "Please provide the S3 bucket ARN and optional object prefix."
            );

            return response;
        }

        // If resources exist → satisfied
        response.setNeedFollowup(false);
        response.setResolvedResources(request.getResolvedResources());

        return response;
    }
}

