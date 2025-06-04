package com.reports.demo.dto;

import java.time.LocalDate;

/**
 * DTO para representar una transacción con su balance acumulado
 */
public class TransactionBalanceDTO {
    private String id;
    private Double amount;
    private String category;
    private LocalDate date;
    private String description;
    private String type; // "INCOME" o "EXPENSE"
    private Double accumulatedBalance; // Balance acumulado hasta esta transacción

    public TransactionBalanceDTO() {}

    public TransactionBalanceDTO(String id, Double amount, String category, LocalDate date, 
                                String description, String type, Double accumulatedBalance) {
        this.id = id;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.description = description;
        this.type = type;
        this.accumulatedBalance = accumulatedBalance;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public Double getAccumulatedBalance() { return accumulatedBalance; }
    public void setAccumulatedBalance(Double accumulatedBalance) { this.accumulatedBalance = accumulatedBalance; }
}
