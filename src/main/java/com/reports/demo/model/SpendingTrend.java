package com.reports.demo.model;

import java.math.BigDecimal;

/**
 * Modelo para representar tendencias financieras (gastos e ingresos) en el tiempo
 */
public class SpendingTrend {
    
    private String period; // Formato: "YYYY-MM"
    private String categoryId;
    private String categoryName;
    private String type; // "EXPENSE" o "INCOME"
    private BigDecimal totalAmount;
    private Integer transactionCount;
    private Double growthPercentage; // Porcentaje de crecimiento respecto al periodo anterior
    
    // Constructor vacío
    public SpendingTrend() {}
    
    // Constructor completo
    public SpendingTrend(String period, String categoryId, String categoryName, String type,
                        BigDecimal totalAmount, Integer transactionCount, Double growthPercentage) {
        this.period = period;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.type = type;
        this.totalAmount = totalAmount;
        this.transactionCount = transactionCount;
        this.growthPercentage = growthPercentage;
    }
    
    // Getters y Setters
    public String getPeriod() {
        return period;
    }
    
    public void setPeriod(String period) {
        this.period = period;
    }
    
    public String getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
    
    public String getCategoryName() {
        return categoryName;
    }
    
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public Integer getTransactionCount() {
        return transactionCount;
    }
    
    public void setTransactionCount(Integer transactionCount) {
        this.transactionCount = transactionCount;
    }
    
    public Double getGrowthPercentage() {
        return growthPercentage;
    }
    
    public void setGrowthPercentage(Double growthPercentage) {
        this.growthPercentage = growthPercentage;
    }
}
