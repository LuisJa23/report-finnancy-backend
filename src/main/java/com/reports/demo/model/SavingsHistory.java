package com.reports.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

/**
 * Representa el historial mensual de ahorros del usuario
 * Se calcula automáticamente al final de cada mes
 */
@Document(collection = "savings_history")
public class SavingsHistory {
    @Id
    private String id;
    private String userId;
    private LocalDate date;
    private Double totalSavings; // Total de ahorros al final del mes
    private Double monthlyIncome; // Ingresos del mes
    private Double monthlyExpenses; // Gastos del mes
    private Double netSavings; // Ahorros netos del mes

    public SavingsHistory() {}

    public SavingsHistory(String userId, LocalDate date, Double totalSavings, Double monthlyIncome, Double monthlyExpenses) {
        this.userId = userId;
        this.date = date;
        this.totalSavings = totalSavings;
        this.monthlyIncome = monthlyIncome;
        this.monthlyExpenses = monthlyExpenses;
        this.netSavings = monthlyIncome - monthlyExpenses;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getTotalSavings() {
        return totalSavings;
    }

    public void setTotalSavings(Double totalSavings) {
        this.totalSavings = totalSavings;
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

    public Double getNetSavings() {
        return netSavings;
    }

    public void setNetSavings(Double netSavings) {
        this.netSavings = netSavings;
    }
}
