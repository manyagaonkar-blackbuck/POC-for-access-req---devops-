package com.example.awsaccess.llm;

import java.util.Map;

public interface LlmClient {
    LlmResponse process(String input);
}
