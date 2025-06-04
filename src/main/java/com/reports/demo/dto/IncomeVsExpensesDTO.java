package com.reports.demo.dto;

import java.util.List;

public class IncomeVsExpensesDTO {
    private List<IncomeExpenseDataDTO> data;
    private Double totalIncome;
    private Double totalExpenses;
    private Double netAmount;

    public IncomeVsExpensesDTO() {}

    public IncomeVsExpensesDTO(List<IncomeExpenseDataDTO> data, Double totalIncome, Double totalExpenses, Double netAmount) {
        this.data = data;
        this.totalIncome = totalIncome;
        this.totalExpenses = totalExpenses;
        this.netAmount = netAmount;
    }

    // Getters and Setters
    public List<IncomeExpenseDataDTO> getData() {
        return data;
    }

    public void setData(List<IncomeExpenseDataDTO> data) {
        this.data = data;
    }

    public Double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(Double totalIncome) {
        this.totalIncome = totalIncome;
    }

    public Double getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(Double totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public Double getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(Double netAmount) {
        this.netAmount = netAmount;
    }

    public static class IncomeExpenseDataDTO {
        private String period;
        private Double ingresos;
        private Double egresos;

        public IncomeExpenseDataDTO() {}

        public IncomeExpenseDataDTO(String period, Double ingresos, Double egresos) {
            this.period = period;
            this.ingresos = ingresos;
            this.egresos = egresos;
        }

        public String getPeriod() {
            return period;
        }

        public void setPeriod(String period) {
            this.period = period;
        }

        public Double getIngresos() {
            return ingresos;
        }

        public void setIngresos(Double ingresos) {
            this.ingresos = ingresos;
        }

        public Double getEgresos() {
            return egresos;
        }

        public void setEgresos(Double egresos) {
            this.egresos = egresos;
        }
    }
}
