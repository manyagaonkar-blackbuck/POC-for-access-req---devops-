package com.example.awsaccess.llm;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MockLlmClient implements LlmClient {

    @Override
    public LlmResponse process(String input) {
        LlmResponse response = new LlmResponse();
        response.setNeedFollowup(true);
        response.setFollowupQuestion("Please provide the S3 bucket ARN.");
        response.setServices(List.of("S3"));
        return response;
    }
}
