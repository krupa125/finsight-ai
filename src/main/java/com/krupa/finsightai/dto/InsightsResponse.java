package com.krupa.finsightai.dto;

public class InsightsResponse {

    private Double income;
    private Double expenses;
    private Double savings;
    private Long totalTransactions;

    public InsightsResponse() {
    }

    public InsightsResponse(Double income,
                            Double expenses,
                            Double savings,
                            Long totalTransactions) {
        this.income = income;
        this.expenses = expenses;
        this.savings = savings;
        this.totalTransactions = totalTransactions;
    }

    public Double getIncome() {
        return income;
    }

    public void setIncome(Double income) {
        this.income = income;
    }

    public Double getExpenses() {
        return expenses;
    }

    public void setExpenses(Double expenses) {
        this.expenses = expenses;
    }

    public Double getSavings() {
        return savings;
    }

    public void setSavings(Double savings) {
        this.savings = savings;
    }

    public Long getTotalTransactions() {
        return totalTransactions;
    }

    public void setTotalTransactions(Long totalTransactions) {
        this.totalTransactions = totalTransactions;
    }
}