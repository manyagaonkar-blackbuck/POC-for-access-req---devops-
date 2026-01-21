package com.example.awsaccess.llm;

import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
public class MockLlmClient implements LlmClient {

    @Override
    public LlmResponse process(String input) {
        // Identify missing ARN
        if (input.toLowerCase().contains("bucket") && !input.toLowerCase().contains("arn:aws")) {
            LlmResponse response = new LlmResponse();
            response.setNeedFollowup(true);
            response.setFollowupQuestion("Please provide the S3 bucket ARN.");
            return response;
        }

        // No followup needed after full details
        Map<String, Object> resolved = new HashMap<>();
        resolved.put("resources", input);

        LlmResponse response = new LlmResponse();
        response.setNeedFollowup(false);
        response.setResolvedResources(resolved);
        return response;
    }
}
