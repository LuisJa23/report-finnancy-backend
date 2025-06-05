package com.reports.demo.service;

import com.reports.demo.model.SpendingTrend;
import com.reports.demo.model.Transaction;
import com.reports.demo.repository.TransactionRepository;
import com.reports.demo.enums.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio para análisis de tendencias financieras
 */
@Service
public class SpendingTrendsService {

    @Autowired
    private TransactionRepository transactionRepository;    /**
     * Analiza las tendencias de gasto de un usuario en un rango de fechas específico
     */
    public List<SpendingTrend> getSpendingTrends(String userId, LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
        
        List<Transaction> transactions = transactionRepository.findByUserIdAndDateBetween(userId, startDateTime, endDateTime);
        
        // Filtrar solo gastos
        List<Transaction> expenses = transactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .collect(Collectors.toList());

        return calculateTrends(expenses);
    }

    /**
     * Analiza las tendencias financieras completas (gastos e ingresos) de un usuario
     */
    public List<SpendingTrend> getFinancialTrends(String userId, LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
        
        List<Transaction> transactions = transactionRepository.findByUserIdAndDateBetween(userId, startDateTime, endDateTime);
        return calculateTrends(transactions);
    }

    /**
     * Calcula las tendencias basadas en las transacciones proporcionadas
     */
    private List<SpendingTrend> calculateTrends(List<Transaction> transactions) {
        Map<String, List<Transaction>> groupedByPeriodAndCategory = transactions.stream()
                .collect(Collectors.groupingBy(this::generatePeriodCategoryKey));

        List<SpendingTrend> trends = new ArrayList<>();

        for (Map.Entry<String, List<Transaction>> entry : groupedByPeriodAndCategory.entrySet()) {
            String[] keyParts = entry.getKey().split("_");
            String period = keyParts[0];
            String categoryName = keyParts[1];
            String type = keyParts[2];

            List<Transaction> periodTransactions = entry.getValue();
            
            BigDecimal totalAmount = periodTransactions.stream()
                    .map(Transaction::getAmount)
                    .map(BigDecimal::valueOf)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            SpendingTrend trend = new SpendingTrend(
                    period,
                    categoryName, // categoryId
                    categoryName, // categoryName
                    type,
                    totalAmount,
                    periodTransactions.size(),
                    0.0 // growthPercentage - se calculará más adelante si es necesario
            );

            trends.add(trend);
        }

        return trends.stream()
                .sorted(Comparator.comparing(SpendingTrend::getPeriod)
                        .thenComparing(SpendingTrend::getCategoryName))
                .collect(Collectors.toList());
    }

    /**
     * Genera una clave única para agrupar por período, categoría y tipo
     */
    private String generatePeriodCategoryKey(Transaction transaction) {
        YearMonth yearMonth = YearMonth.from(transaction.getDate());
        String period = yearMonth.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        String categoryName = transaction.getCategoryDisplayName();
        String type = transaction.getType().toString();
        
        return period + "_" + categoryName + "_" + type;
    }
}
