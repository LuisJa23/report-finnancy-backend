package com.reports.demo.dto;

import java.util.List;

public class TotalSavingsDTO {
    private Double currentSavings;
    private SavingsGrowthDTO savingsGrowth;
    private List<SavingsHistoryDTO> savingsHistory;

    public TotalSavingsDTO() {}

    public TotalSavingsDTO(Double currentSavings, SavingsGrowthDTO savingsGrowth, List<SavingsHistoryDTO> savingsHistory) {
        this.currentSavings = currentSavings;
        this.savingsGrowth = savingsGrowth;
        this.savingsHistory = savingsHistory;
    }

    // Getters and Setters
    public Double getCurrentSavings() {
        return currentSavings;
    }

    public void setCurrentSavings(Double currentSavings) {
        this.currentSavings = currentSavings;
    }

    public SavingsGrowthDTO getSavingsGrowth() {
        return savingsGrowth;
    }

    public void setSavingsGrowth(SavingsGrowthDTO savingsGrowth) {
        this.savingsGrowth = savingsGrowth;
    }

    public List<SavingsHistoryDTO> getSavingsHistory() {
        return savingsHistory;
    }

    public void setSavingsHistory(List<SavingsHistoryDTO> savingsHistory) {
        this.savingsHistory = savingsHistory;
    }

    public static class SavingsGrowthDTO {
        private Double percentage;
        private Boolean isPositive;

        public SavingsGrowthDTO() {}

        public SavingsGrowthDTO(Double percentage, Boolean isPositive) {
            this.percentage = percentage;
            this.isPositive = isPositive;
        }

        public Double getPercentage() {
            return percentage;
        }

        public void setPercentage(Double percentage) {
            this.percentage = percentage;
        }

        public Boolean getIsPositive() {
            return isPositive;
        }

        public void setIsPositive(Boolean isPositive) {
            this.isPositive = isPositive;
        }
    }

    public static class SavingsHistoryDTO {
        private String date;
        private Double amount;

        public SavingsHistoryDTO() {}

        public SavingsHistoryDTO(String date, Double amount) {
            this.date = date;
            this.amount = amount;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public Double getAmount() {
            return amount;
        }

        public void setAmount(Double amount) {
            this.amount = amount;
        }
    }
}
