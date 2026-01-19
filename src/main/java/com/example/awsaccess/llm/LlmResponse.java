package com.example.awsaccess.llm;

import java.util.List;
import java.util.Map;

public class LlmResponse {

    private boolean needFollowup;
    private String followupQuestion;

    private List<String> services;
    private List<String> actionGroups;

    private Map<String, Object> resolvedResources;

    public boolean isNeedFollowup() {
        return needFollowup;
    }

    public void setNeedFollowup(boolean needFollowup) {
        this.needFollowup = needFollowup;
    }

    public String getFollowupQuestion() {
        return followupQuestion;
    }

    public void setFollowupQuestion(String followupQuestion) {
        this.followupQuestion = followupQuestion;
    }

    public List<String> getServices() {
        return services;
    }

    public void setServices(List<String> services) {
        this.services = services;
    }

    public List<String> getActionGroups() {
        return actionGroups;
    }

    public void setActionGroups(List<String> actionGroups) {
        this.actionGroups = actionGroups;
    }

    public Map<String, Object> getResolvedResources() {
        return resolvedResources;
    }

    public void setResolvedResources(Map<String, Object> resolvedResources) {
        this.resolvedResources = resolvedResources;
    }
}


