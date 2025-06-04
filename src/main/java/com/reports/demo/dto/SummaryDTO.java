package com.reports.demo.dto;

public class SummaryDTO {
    private Double totalBalance;
    private Double monthlyIncome;
    private Double monthlyExpenses;
    private Double savingsRate;

    public SummaryDTO() {}

    public SummaryDTO(Double totalBalance, Double monthlyIncome, Double monthlyExpenses, Double savingsRate) {
        this.totalBalance = totalBalance;
        this.monthlyIncome = monthlyIncome;
        this.monthlyExpenses = monthlyExpenses;
        this.savingsRate = savingsRate;
    }

    // Getters and Setters
    public Double getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(Double totalBalance) {
        this.totalBalance = totalBalance;
    }

    public Double getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(Double monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public Double getMonthlyExpenses() {
        return monthlyExpenses;
    }

    public void setMonthlyExpenses(Double monthlyExpenses) {
        this.monthlyExpenses = monthlyExpenses;
    }

    public Double getSavingsRate() {
        return savingsRate;
    }

    public void setSavingsRate(Double savingsRate) {
        this.savingsRate = savingsRate;
    }
}
