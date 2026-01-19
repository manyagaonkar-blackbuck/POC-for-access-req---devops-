package com.example.awsaccess.llm;

import java.util.Map;
import java.util.List;

public class LlmRequest {

    private String reason;
    private List<String> services;
    private List<String> actionGroups;

    // key = resource type (S3), value = details
    private Map<String, Object> resolvedResources;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

