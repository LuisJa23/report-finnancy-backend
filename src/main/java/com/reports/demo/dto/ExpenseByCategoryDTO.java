package com.reports.demo.dto;

public class ExpenseByCategoryDTO {
    private String category;
    private Double amount;
    private Double percentage;
    private String color;

    public ExpenseByCategoryDTO() {}

    public ExpenseByCategoryDTO(String category, Double amount, Double percentage, String color) {
        this.category = category;
        this.amount = amount;
        this.percentage = percentage;
        this.color = color;
    }

    // Getters and Setters
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
