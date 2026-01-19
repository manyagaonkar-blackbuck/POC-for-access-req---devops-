package com.example.awsaccess.dto.request;

import java.util.Map;

public class FollowupAnswerDto {

    private Map<String, Object> answer;

    public Map<String, Object> getAnswer() {
        return answer;
    }

    public void setAnswer(Map<String, Object> answer) {
        this.answer = answer;
    }
}
