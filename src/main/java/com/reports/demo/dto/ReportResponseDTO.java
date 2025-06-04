package com.reports.demo.dto;

import java.util.List;

public class ReportResponseDTO {
    private List<ExpenseByCategoryDTO> expensesByCategory;
    private TotalSavingsDTO totalSavings;
    private IncomeVsExpensesDTO incomeVsExpenses;
    private SummaryDTO summary;

    public ReportResponseDTO() {}

    public ReportResponseDTO(List<ExpenseByCategoryDTO> expensesByCategory, 
                            TotalSavingsDTO totalSavings, 
                            IncomeVsExpensesDTO incomeVsExpenses, 
                            SummaryDTO summary) {
        this.expensesByCategory = expensesByCategory;
        this.totalSavings = totalSavings;
        this.incomeVsExpenses = incomeVsExpenses;
        this.summary = summary;
    }

    // Getters and Setters
    public List<ExpenseByCategoryDTO> getExpensesByCategory() {
        return expensesByCategory;
    }

    public void setExpensesByCategory(List<ExpenseByCategoryDTO> expensesByCategory) {
        this.expensesByCategory = expensesByCategory;
    }

    public TotalSavingsDTO getTotalSavings() {
        return totalSavings;
    }

    public void setTotalSavings(TotalSavingsDTO totalSavings) {
        this.totalSavings = totalSavings;
    }

    public IncomeVsExpensesDTO getIncomeVsExpenses() {
        return incomeVsExpenses;
    }

    public void setIncomeVsExpenses(IncomeVsExpensesDTO incomeVsExpenses) {
        this.incomeVsExpenses = incomeVsExpenses;
    }

    public SummaryDTO getSummary() {
        return summary;
    }

    public void setSummary(SummaryDTO summary) {
        this.summary = summary;
    }
}
