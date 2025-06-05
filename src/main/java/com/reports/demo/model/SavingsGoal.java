package com.reports.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

/**
 * Representa una meta de ahorro del usuario
 */
@Document(collection = "savings_goals")
public class SavingsGoal {
    @Id
    private String id;
    private String userId;
    private String name;
    private Double targetAmount;
    private Double currentAmount;
    private LocalDate targetDate;
    private boolean completed;

    // Constructor vacío necesario para Spring Data
    public SavingsGoal() {
        this.currentAmount = 0.0;
        this.completed = false;
    }

    // Constructor con parámetros
    public SavingsGoal(String userId, String name, Double targetAmount, LocalDate targetDate) {
        this();
        this.userId = userId;
        this.name = name;
        this.targetAmount = targetAmount;
        this.targetDate = targetDate;
    }

    // Calcula el progreso como porcentaje
    public double getProgressPercentage() {
        if (targetAmount == null || targetAmount <= 0) return 0;
        return (currentAmount / targetAmount) * 100;
    }

    // Getters y Setters
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(Double targetAmount) {
        this.targetAmount = targetAmount;
    }

    public Double getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(Double currentAmount) {
        this.currentAmount = currentAmount;
    }

    public LocalDate getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(LocalDate targetDate) {
        this.targetDate = targetDate;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}