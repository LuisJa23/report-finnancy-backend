// FinancialSummaryDTO.java
package com.reports.demo.dto;

public class FinancialSummaryDTO {
    private double totalBalance;
    private double totalIncome;
    private double totalExpenses;
    private int incomeTransactionsCount;
    private int expenseTransactionsCount;

    // Constructor
    public FinancialSummaryDTO(double totalBalance, double totalIncome, double totalExpenses,
                             int incomeTransactionsCount, int expenseTransactionsCount) {
        this.totalBalance = totalBalance;
        this.totalIncome = totalIncome;
        this.totalExpenses = totalExpenses;
        this.incomeTransactionsCount = incomeTransactionsCount;
        this.expenseTransactionsCount = expenseTransactionsCount;
    }

    // Getters
    public double getTotalBalance() {
        return totalBalance;
    }

    public double getTotalIncome() {
        return totalIncome;
    }

    public double getTotalExpenses() {
        return totalExpenses;
    }

    public int getIncomeTransactionsCount() {
        return incomeTransactionsCount;
    }

    public int getExpenseTransactionsCount() {
        return expenseTransactionsCount;
    }
}