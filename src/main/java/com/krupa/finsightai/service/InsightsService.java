package com.krupa.finsightai.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.krupa.finsightai.dto.AIAnalysisResponse;
import com.krupa.finsightai.dto.DashboardResponse;
import com.krupa.finsightai.dto.InsightsResponse;
import com.krupa.finsightai.model.Transaction;
import com.krupa.finsightai.model.TransactionCategory;
import com.krupa.finsightai.model.TransactionType;
import com.krupa.finsightai.repository.TransactionRepository;

@Service
public class InsightsService {

    private final TransactionRepository transactionRepository;

    public InsightsService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }
    

    public InsightsResponse getMonthlyInsights(Long userId) {

        List<Transaction> transactions =
                transactionRepository
                        .findByFromUserIdOrToUserIdAndTimestampBetween(
                                userId,
                                userId,
                                LocalDateTime.now().withDayOfMonth(1).toLocalDate().atStartOfDay(),
                                LocalDateTime.now(),
                                org.springframework.data.domain.PageRequest.of(0, 1000)
                        )
                        .getContent();

        double income = transactions.stream()
                .filter(t -> t.getType() == TransactionType.CREDIT)
                .mapToDouble(Transaction::getAmount)
                .sum();

        double expenses = transactions.stream()
                .filter(t -> t.getType() == TransactionType.DEBIT)
                .mapToDouble(Transaction::getAmount)
                .sum();

        double savings = income - expenses;

        long totalTransactions = transactions.size();

        return new InsightsResponse(
                income,
                expenses,
                savings,
                totalTransactions
        );
    }
    public Map<TransactionCategory, Double> getCategoryBreakdown(Long userId) {

    LocalDateTime startDate =
            LocalDateTime.now()
                    .withDayOfMonth(1)
                    .toLocalDate()
                    .atStartOfDay();

    LocalDateTime endDate = LocalDateTime.now();

    List<Object[]> results =
            transactionRepository.getCategoryBreakdown(
                    userId,
                    startDate,
                    endDate
            );

    Map<TransactionCategory, Double> breakdown = new HashMap<>();

    for (Object[] row : results) {

        TransactionCategory category =
                (TransactionCategory) row[0];

        Double amount =
                ((Number) row[1]).doubleValue();

        breakdown.put(category, amount);
    }

    return breakdown;
        }
        public DashboardResponse getDashboardSummary(Long userId) {

    InsightsResponse insights = getMonthlyInsights(userId);

    Map<TransactionCategory, Double> categories =
            getCategoryBreakdown(userId);

    String topCategory = "NONE";
    double maxAmount = 0;

    for (Map.Entry<TransactionCategory, Double> entry : categories.entrySet()) {

        if (entry.getValue() > maxAmount) {
            maxAmount = entry.getValue();
            topCategory = entry.getKey().name();
        }
    }

    return new DashboardResponse(
            insights.getIncome(),
            insights.getExpenses(),
            insights.getSavings(),
            insights.getTotalTransactions(),
            topCategory
    );
        }
        public AIAnalysisResponse getAIAnalysis(Long userId) {

    DashboardResponse dashboard =
            getDashboardSummary(userId);

    ArrayList<String> insights = new ArrayList<>();

    insights.add(
            "Your highest spending category is "
                    + dashboard.getTopCategory() + "."
    );

    insights.add(
            "You spent ₹"
                    + dashboard.getExpenses()
                    + " this month."
    );

    insights.add(
            "Your savings are ₹"
                    + dashboard.getSavings() + "."
    );

    if (dashboard.getIncome() > 0) {

        double expensePercentage =
                (dashboard.getExpenses()
                        / dashboard.getIncome()) * 100;

        insights.add(
                "Expenses represent "
                        + String.format("%.2f", expensePercentage)
                        + "% of your income."
        );
    }

    return new AIAnalysisResponse(insights);
        }
}