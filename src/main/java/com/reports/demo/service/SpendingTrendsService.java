package com.reports.demo.service;

import com.reports.demo.model.SpendingTrend;
import com.reports.demo.model.Transaction;
import com.reports.demo.repository.TransactionRepository;
import com.reports.demo.enums.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio para analizar tendencias financieras (gastos e ingresos) de los usuarios
 */
@Service
public class SpendingTrendsService {

    @Autowired
    private TransactionRepository transactionRepository;

    private static final DateTimeFormatter PERIOD_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    /**
     * Obtiene las tendencias financieras para un usuario en un rango de fechas
     */
    public List<SpendingTrend> getFinancialTrends(String userId, LocalDate startDate, LocalDate endDate) {
        // Obtener todas las transacciones del usuario en el rango de fechas (gastos e ingresos)
        List<Transaction> allTransactions = transactionRepository.findByUserIdAndDateBetween(
            userId, startDate.atStartOfDay(), endDate.atTime(23, 59, 59)
        );

        // Agrupar transacciones por período, tipo y categoría
        Map<String, Map<String, Map<String, List<Transaction>>>> groupedData = allTransactions.stream()
            .collect(Collectors.groupingBy(
                t -> t.getDate().format(PERIOD_FORMATTER), // Período (YYYY-MM)
                Collectors.groupingBy(
                    t -> t.getType().name(), // Tipo: EXPENSE o INCOME
                    Collectors.groupingBy(t -> getTransactionCategoryKey(t)) // Categoría/tipo como String
                )
            ));

        // Convertir a SpendingTrends
        List<SpendingTrend> trends = new ArrayList<>();
        for (Map.Entry<String, Map<String, Map<String, List<Transaction>>>> periodEntry : groupedData.entrySet()) {
            String period = periodEntry.getKey();
            
            for (Map.Entry<String, Map<String, List<Transaction>>> typeEntry : periodEntry.getValue().entrySet()) {
                String transactionType = typeEntry.getKey();
                
                for (Map.Entry<String, List<Transaction>> categoryEntry : typeEntry.getValue().entrySet()) {
                    String categoryKey = categoryEntry.getKey();
                    List<Transaction> categoryTransactions = categoryEntry.getValue();
                    
                    SpendingTrend trend = new SpendingTrend();
                    trend.setPeriod(period);
                    trend.setType(transactionType);
                    trend.setCategoryId(categoryKey);
                    trend.setCategoryName(getDisplayName(categoryKey, transactionType));
                    
                    // Calcular totales
                    BigDecimal totalAmount = categoryTransactions.stream()
                        .map(t -> BigDecimal.valueOf(t.getAmount()))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                    
                    trend.setTotalAmount(totalAmount);
                    trend.setTransactionCount(categoryTransactions.size());
                    
                    trends.add(trend);
                }
            }
        }

        // Calcular porcentajes de crecimiento
        return calculateGrowthPercentages(trends);
    }

    /**
     * Obtiene las tendencias de gasto para un usuario (método de compatibilidad)
     */
    public List<SpendingTrend> getSpendingTrends(String userId, LocalDate startDate, LocalDate endDate) {
        return getFinancialTrends(userId, startDate, endDate).stream()
            .filter(trend -> "EXPENSE".equals(trend.getType()))
            .collect(Collectors.toList());
    }

    /**
     * Calcula los porcentajes de crecimiento entre períodos consecutivos
     */
    private List<SpendingTrend> calculateGrowthPercentages(List<SpendingTrend> trends) {
        Map<String, List<SpendingTrend>> trendsByCategory = trends.stream()
            .collect(Collectors.groupingBy(t -> t.getCategoryId() + "-" + t.getType()));

        trendsByCategory.forEach((categoryKey, categoryTrends) -> {
            // Ordenar por período
            categoryTrends.sort(Comparator.comparing(SpendingTrend::getPeriod));

            for (int i = 1; i < categoryTrends.size(); i++) {
                SpendingTrend current = categoryTrends.get(i);
                SpendingTrend previous = categoryTrends.get(i - 1);

                if (previous.getTotalAmount().compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal growth = current.getTotalAmount()
                        .subtract(previous.getTotalAmount())
                        .divide(previous.getTotalAmount(), 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100));
                    
                    current.setGrowthPercentage(growth.doubleValue());
                } else {
                    current.setGrowthPercentage(0.0);
                }
            }
        });

        return trends;
    }

    /**
     * Obtiene la clave de categoría para una transacción
     */
    private String getTransactionCategoryKey(Transaction transaction) {
        if (transaction.getType() == TransactionType.EXPENSE) {
            return transaction.getCategory().name();
        } else {
            return "INCOME"; // Los ingresos no tienen categorías específicas
        }
    }

    /**
     * Obtiene el nombre de display para una categoría según el tipo
     */
    private String getDisplayName(String categoryKey, String transactionType) {
        if ("INCOME".equals(transactionType)) {
            return "Ingresos";
        } else {
            return getCategoryDisplayName(categoryKey);
        }
    }

    /**
     * Obtiene el nombre de display para una categoría de gasto
     */
    private String getCategoryDisplayName(String categoryId) {
        try {
            return com.reports.demo.enums.ExpenseCategory.valueOf(categoryId).getDisplayName();
        } catch (Exception e) {
            return categoryId; // Retorna el ID si no se puede convertir
        }
    }
}
