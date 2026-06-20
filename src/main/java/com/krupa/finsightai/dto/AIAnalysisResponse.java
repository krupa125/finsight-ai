package com.krupa.finsightai.dto;

import java.util.List;

public class AIAnalysisResponse {

    private List<String> insights;

    public AIAnalysisResponse(List<String> insights) {
        this.insights = insights;
    }

    public List<String> getInsights() {
        return insights;
    }

    public void setInsights(List<String> insights) {
        this.insights = insights;
    }
}