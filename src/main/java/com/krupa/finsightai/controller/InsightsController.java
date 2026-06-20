package com.krupa.finsightai.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.krupa.finsightai.dto.AIAnalysisResponse;
import com.krupa.finsightai.dto.DashboardResponse;
import com.krupa.finsightai.dto.InsightsResponse;
import com.krupa.finsightai.model.TransactionCategory;
import com.krupa.finsightai.service.InsightsService;

@RestController
@RequestMapping("/insights")
public class InsightsController {

    private final InsightsService insightsService;

    public InsightsController(InsightsService insightsService) {
        this.insightsService = insightsService;
    }

    @GetMapping("/monthly/{userId}")
    public InsightsResponse getMonthlyInsights(
            @PathVariable Long userId) {

        return insightsService.getMonthlyInsights(userId);
    }
    @GetMapping("/categories/{userId}")
    public Map<TransactionCategory, Double> getCategoryBreakdown(
        @PathVariable Long userId) {

    return insightsService.getCategoryBreakdown(userId);
    }
    @GetMapping("/dashboard/{userId}")
    public DashboardResponse getDashboardSummary(
        @PathVariable Long userId) {

    return insightsService.getDashboardSummary(userId);
    }
    @GetMapping("/analysis/{userId}")
    public AIAnalysisResponse getAIAnalysis(
        @PathVariable Long userId) {

    return insightsService.getAIAnalysis(userId);
    }
}