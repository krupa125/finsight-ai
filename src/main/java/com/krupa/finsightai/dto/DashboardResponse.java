package com.krupa.finsightai.dto;

public class DashboardResponse {

    private final double income;
    private final double expenses;
    private final double savings;
    private final long totalTransactions;
    private final String topCategory;

    public DashboardResponse(
            double income,
            double expenses,
            double savings,
            long totalTransactions,
            String topCategory) {

        this.income = income;
        this.expenses = expenses;
        this.savings = savings;
        this.totalTransactions = totalTransactions;
        this.topCategory = topCategory;
    }

    public double getIncome() {
        return income;
    }

    public double getExpenses() {
        return expenses;
    }

    public double getSavings() {
        return savings;
    }

    public long getTotalTransactions() {
        return totalTransactions;
    }

    public String getTopCategory() {
        return topCategory;
    }
}