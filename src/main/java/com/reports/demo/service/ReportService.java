package com.reports.demo.service;

import com.reports.demo.model.Transaction;
import com.reports.demo.model.SavingsHistory;
import com.reports.demo.repository.TransactionRepository;
import com.reports.demo.repository.SavingsHistoryRepository;
import com.reports.demo.dto.*;
import com.reports.demo.enums.TransactionType;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportService {
    private final TransactionRepository transactionRepository;
    private final SavingsHistoryRepository savingsHistoryRepository;

    // Colores predefinidos para las categorías
    private static final String[] CATEGORY_COLORS = {
        "#8884d8", "#82ca9d", "#ffc658", "#ff7300", "#0088fe", "#00c49f", 
        "#ffbb28", "#ff8042", "#8dd1e1", "#d084d0", "#87d068", "#ffa726"
    };

    // Mapeo de meses en español
    private static final String[] MONTH_NAMES = {
        "Ene", "Feb", "Mar", "Abr", "May", "Jun",
        "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"
    };

    public ReportService(TransactionRepository transactionRepository, 
                        SavingsHistoryRepository savingsHistoryRepository) {
        this.transactionRepository = transactionRepository;
        this.savingsHistoryRepository = savingsHistoryRepository;
    }

    public ReportResponseDTO getReport(String userId, String interval, LocalDate now, boolean global, String type) {
        LocalDate start = calculateStartDate(interval, now);
        
        // Obtener datos según el contexto (global o específico del usuario)
        List<Transaction> allTransactions = getTransactions(userId, start, now, global);
        
        // Filtrar por tipo si se especifica
        if (type != null) {
            TransactionType transactionType = TransactionType.valueOf(type.toUpperCase());
            allTransactions = allTransactions.stream()
                .filter(t -> t.getType() == transactionType)
                .toList();
        }

        // Construir la respuesta
        ReportResponseDTO response = new ReportResponseDTO();
        response.setExpensesByCategory(buildExpensesByCategory(allTransactions));
        response.setTotalSavings(buildTotalSavings(userId, start, now, global));
        response.setIncomeVsExpenses(buildIncomeVsExpenses(userId, start, now, global, interval));
        response.setSummary(buildSummary(userId, now, global));

        return response;
    }

    private LocalDate calculateStartDate(String interval, LocalDate now) {
        return switch (interval.toLowerCase()) {
            case "monthly" -> now.withDayOfMonth(1);
            case "semiannual" -> now.minusMonths(6);
            case "annual" -> now.withDayOfYear(1);
            default -> now.minusDays(7); // weekly
        };
    }

    /**
     * Helper para convertir LocalDate a LocalDateTime para rangos completos de días
     */
    private LocalDateTime convertToStartOfDay(LocalDate date) {
        return date.atStartOfDay();
    }
    
    private LocalDateTime convertToEndOfDay(LocalDate date) {
        return date.atTime(23, 59, 59);
    }

    private List<Transaction> getTransactions(String userId, LocalDate start, LocalDate end, boolean global) {
        LocalDateTime startDateTime = convertToStartOfDay(start);
        LocalDateTime endDateTime = convertToEndOfDay(end);
        
        if (global) {
            return transactionRepository.findByDateBetween(startDateTime, endDateTime);
        } else if (userId != null) {
            return transactionRepository.findByUserIdAndDateBetween(userId, startDateTime, endDateTime);
        } else {
            return List.of();
        }
    }

    private List<ExpenseByCategoryDTO> buildExpensesByCategory(List<Transaction> transactions) {
        // Filtrar solo gastos
        List<Transaction> expenses = transactions.stream()
            .filter(t -> t.getType() == TransactionType.EXPENSE)
            .toList();

        if (expenses.isEmpty()) {
            return List.of();
        }

        // Agrupar por categoría y sumar montos
        Map<String, Double> categoryTotals = expenses.stream()
            .collect(Collectors.groupingBy(
                t -> t.getCategoryDisplayName(), // Usar el método helper que obtiene el display name
                Collectors.summingDouble(Transaction::getAmount)
            ));

        // Calcular total para porcentajes
        double totalExpenses = categoryTotals.values().stream().mapToDouble(Double::doubleValue).sum();

        // Convertir a DTOs con colores
        List<ExpenseByCategoryDTO> result = new ArrayList<>();
        int colorIndex = 0;
        
        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            String category = entry.getKey();
            Double amount = entry.getValue();
            Double percentage = (amount / totalExpenses) * 100;
            String color = CATEGORY_COLORS[colorIndex % CATEGORY_COLORS.length];
            
            result.add(new ExpenseByCategoryDTO(category, amount, 
                Math.round(percentage * 10.0) / 10.0, color));
            colorIndex++;
        }

        // Ordenar por monto descendente
        result.sort((a, b) -> Double.compare(b.getAmount(), a.getAmount()));
        return result;
    }

    private TotalSavingsDTO buildTotalSavings(String userId, LocalDate start, LocalDate end, boolean global) {
        if (global || userId == null) {
            // Para reportes globales, retornar datos básicos
            return new TotalSavingsDTO(0.0, 
                new TotalSavingsDTO.SavingsGrowthDTO(0.0, true),
                List.of());
        }

        // Obtener historial de ahorros de los últimos 6 meses
        List<SavingsHistory> savingsHistory = savingsHistoryRepository
            .findTop6ByUserIdOrderByDateDesc(userId);
        
        Collections.reverse(savingsHistory); // Ordenar cronológicamente

        // Calcular ahorros actuales
        Double currentSavings = calculateCurrentSavings(userId);
        
        // Calcular crecimiento
        TotalSavingsDTO.SavingsGrowthDTO growth = calculateSavingsGrowth(savingsHistory, currentSavings);
        
        // Convertir historial a DTOs
        List<TotalSavingsDTO.SavingsHistoryDTO> historyDTOs = savingsHistory.stream()
            .map(sh -> new TotalSavingsDTO.SavingsHistoryDTO(
                sh.getDate().toString(), 
                sh.getTotalSavings()
            ))
            .toList();

        return new TotalSavingsDTO(currentSavings, growth, historyDTOs);
    }

    private Double calculateCurrentSavings(String userId) {
        // Obtener todas las transacciones del usuario
        List<Transaction> allTransactions = transactionRepository.findByUserId(userId);
        
        double totalIncome = allTransactions.stream()
            .filter(t -> t.getType() == TransactionType.INCOME)
            .mapToDouble(Transaction::getAmount)
            .sum();
            
        double totalExpenses = allTransactions.stream()
            .filter(t -> t.getType() == TransactionType.EXPENSE)
            .mapToDouble(Transaction::getAmount)
            .sum();
            
        return totalIncome - totalExpenses;
    }

    private TotalSavingsDTO.SavingsGrowthDTO calculateSavingsGrowth(List<SavingsHistory> history, Double currentSavings) {
        if (history.size() < 2) {
            return new TotalSavingsDTO.SavingsGrowthDTO(0.0, true);
        }

        SavingsHistory previousMonth = history.get(history.size() - 2);
        double previousAmount = previousMonth.getTotalSavings();
        
        if (previousAmount == 0) {
            return new TotalSavingsDTO.SavingsGrowthDTO(0.0, currentSavings >= 0);
        }

        double growthPercentage = ((currentSavings - previousAmount) / Math.abs(previousAmount)) * 100;
        return new TotalSavingsDTO.SavingsGrowthDTO(
            Math.round(growthPercentage * 10.0) / 10.0,
            growthPercentage >= 0
        );
    }

    private IncomeVsExpensesDTO buildIncomeVsExpenses(String userId, LocalDate start, LocalDate end, boolean global, String interval) {
        List<IncomeVsExpensesDTO.IncomeExpenseDataDTO> monthlyData = new ArrayList<>();
        double totalIncome = 0;
        double totalExpenses = 0;

        // Generar datos mensuales para los últimos 6 meses
        for (int i = 5; i >= 0; i--) {
            LocalDate monthStart = end.minusMonths(i).withDayOfMonth(1);
            LocalDate monthEnd = monthStart.plusMonths(1).minusDays(1);
            
            List<Transaction> monthTransactions = getTransactions(userId, monthStart, monthEnd, global);
            
            double monthIncome = monthTransactions.stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .mapToDouble(Transaction::getAmount)
                .sum();
                
            double monthExpenses = monthTransactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .mapToDouble(Transaction::getAmount)
                .sum();

            String monthName = MONTH_NAMES[monthStart.getMonthValue() - 1];
            monthlyData.add(new IncomeVsExpensesDTO.IncomeExpenseDataDTO(monthName, monthIncome, monthExpenses));
            
            totalIncome += monthIncome;
            totalExpenses += monthExpenses;
        }

        return new IncomeVsExpensesDTO(monthlyData, totalIncome, totalExpenses, totalIncome - totalExpenses);
    }

    private SummaryDTO buildSummary(String userId, LocalDate now, boolean global) {
        if (global || userId == null) {
            return new SummaryDTO(0.0, 0.0, 0.0, 0.0);
        }

        // Calcular balance total
        Double totalBalance = calculateCurrentSavings(userId);
        
        // Obtener datos del mes actual
        LocalDate monthStart = now.withDayOfMonth(1);
        LocalDateTime monthStartDateTime = convertToStartOfDay(monthStart);
        LocalDateTime nowDateTime = convertToEndOfDay(now);
        List<Transaction> monthTransactions = transactionRepository
            .findByUserIdAndDateBetween(userId, monthStartDateTime, nowDateTime);
            
        double monthlyIncome = monthTransactions.stream()
            .filter(t -> t.getType() == TransactionType.INCOME)
            .mapToDouble(Transaction::getAmount)
            .sum();
            
        double monthlyExpenses = monthTransactions.stream()
            .filter(t -> t.getType() == TransactionType.EXPENSE)
            .mapToDouble(Transaction::getAmount)
            .sum();

        // Calcular tasa de ahorro
        double savingsRate = monthlyIncome > 0 ? ((monthlyIncome - monthlyExpenses) / monthlyIncome) * 100 : 0;
        savingsRate = Math.round(savingsRate * 10.0) / 10.0;

        return new SummaryDTO(totalBalance, monthlyIncome, monthlyExpenses, savingsRate);
    }

    /**
     * Genera un reporte financiero optimizado para dashboards frontend
     * 🔧 SOLUCIÓN COMPLETA: Usa TODAS las transacciones del usuario (sin filtro de fecha restrictivo)
     */
    public ReportResponseDTO generateFinancialReport(String userId) {
        LocalDate now = LocalDate.now();
        
        // 🚨 CORRECCIÓN: Obtener TODAS las transacciones del usuario sin filtro de fecha
        List<Transaction> allUserTransactions = transactionRepository.findByUserId(userId);
        
        // Construir la respuesta con datos completos
        ReportResponseDTO response = new ReportResponseDTO();
        response.setExpensesByCategory(buildExpensesByCategoryComplete(allUserTransactions));
        response.setTotalSavings(buildTotalSavingsComplete(userId, allUserTransactions));
        response.setIncomeVsExpenses(buildIncomeVsExpensesComplete(userId, now));
        response.setSummary(buildSummaryComplete(userId, allUserTransactions, now));

        return response;
    }

    /**
     * 🔧 VERSIÓN COMPLETA: Construye gastos por categoría usando TODAS las transacciones
     * Sin filtros de fecha restrictivos
     */
    private List<ExpenseByCategoryDTO> buildExpensesByCategoryComplete(List<Transaction> allTransactions) {
        // Filtrar solo gastos
        List<Transaction> expenses = allTransactions.stream()
            .filter(t -> t.getType() == TransactionType.EXPENSE)
            .toList();

        if (expenses.isEmpty()) {
            return List.of();
        }

        // 🎯 SOLUCIÓN: Agrupar por categoría correctamente usando getCategoryDisplayName()
        Map<String, Double> categoryTotals = expenses.stream()
            .collect(Collectors.groupingBy(
                Transaction::getCategoryDisplayName,
                Collectors.summingDouble(Transaction::getAmount)
            ));

        // Debug logging para verificar categorías encontradas
        System.out.println("🔍 DEBUG: Categorías encontradas en todas las transacciones:");
        categoryTotals.forEach((cat, amount) -> 
            System.out.println("  - " + cat + ": $" + amount));

        // Calcular total para porcentajes
        double totalExpenses = categoryTotals.values().stream().mapToDouble(Double::doubleValue).sum();
        System.out.println("💰 DEBUG: Total de gastos: $" + totalExpenses);

        // Convertir a DTOs con colores
        List<ExpenseByCategoryDTO> result = new ArrayList<>();
        int colorIndex = 0;
        
        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            String category = entry.getKey();
            Double amount = entry.getValue();
            Double percentage = (amount / totalExpenses) * 100;
            String color = CATEGORY_COLORS[colorIndex % CATEGORY_COLORS.length];
            
            result.add(new ExpenseByCategoryDTO(category, amount, 
                Math.round(percentage * 10.0) / 10.0, color));
            colorIndex++;
        }

        // Ordenar por monto descendente
        result.sort((a, b) -> Double.compare(b.getAmount(), a.getAmount()));
        
        System.out.println("✅ DEBUG: Resultado final con " + result.size() + " categorías");
        return result;
    }

    /**
     * 🔧 VERSIÓN COMPLETA: Calcula ahorros totales usando todas las transacciones
     */
    private TotalSavingsDTO buildTotalSavingsComplete(String userId, List<Transaction> allTransactions) {
        // Calcular ahorros actuales basado en todas las transacciones
        double totalIncome = allTransactions.stream()
            .filter(t -> t.getType() == TransactionType.INCOME)
            .mapToDouble(Transaction::getAmount)
            .sum();
            
        double totalExpenses = allTransactions.stream()
            .filter(t -> t.getType() == TransactionType.EXPENSE)
            .mapToDouble(Transaction::getAmount)
            .sum();
            
        Double currentSavings = totalIncome - totalExpenses;

        // Obtener historial de ahorros de los últimos 6 meses
        List<SavingsHistory> savingsHistory = savingsHistoryRepository
            .findTop6ByUserIdOrderByDateDesc(userId);
        
        Collections.reverse(savingsHistory); // Ordenar cronológicamente

        // Calcular crecimiento
        TotalSavingsDTO.SavingsGrowthDTO growth = calculateSavingsGrowth(savingsHistory, currentSavings);
        
        // Convertir historial a DTOs
        List<TotalSavingsDTO.SavingsHistoryDTO> historyDTOs = savingsHistory.stream()
            .map(sh -> new TotalSavingsDTO.SavingsHistoryDTO(
                sh.getDate().toString(), 
                sh.getTotalSavings()
            ))
            .toList();

        return new TotalSavingsDTO(currentSavings, growth, historyDTOs);
    }

    /**
     * 🔧 VERSIÓN COMPLETA: Construye ingresos vs gastos de los últimos 6 meses
     */
    private IncomeVsExpensesDTO buildIncomeVsExpensesComplete(String userId, LocalDate now) {
        List<IncomeVsExpensesDTO.IncomeExpenseDataDTO> monthlyData = new ArrayList<>();
        double totalIncome = 0;
        double totalExpenses = 0;

        // Generar datos mensuales para los últimos 6 meses
        for (int i = 5; i >= 0; i--) {
            LocalDate monthStart = now.minusMonths(i).withDayOfMonth(1);
            LocalDate monthEnd = monthStart.plusMonths(1).minusDays(1);
            
            LocalDateTime monthStartDateTime = convertToStartOfDay(monthStart);
            LocalDateTime monthEndDateTime = convertToEndOfDay(monthEnd);
            
            List<Transaction> monthTransactions = transactionRepository
                .findByUserIdAndDateBetween(userId, monthStartDateTime, monthEndDateTime);
            
            double monthIncome = monthTransactions.stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .mapToDouble(Transaction::getAmount)
                .sum();
                
            double monthExpenses = monthTransactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .mapToDouble(Transaction::getAmount)
                .sum();

            String monthName = MONTH_NAMES[monthStart.getMonthValue() - 1];
            monthlyData.add(new IncomeVsExpensesDTO.IncomeExpenseDataDTO(monthName, monthIncome, monthExpenses));
            
            totalIncome += monthIncome;
            totalExpenses += monthExpenses;
        }

        return new IncomeVsExpensesDTO(monthlyData, totalIncome, totalExpenses, totalIncome - totalExpenses);
    }

    /**
     * 🔧 VERSIÓN COMPLETA: Construye resumen usando todas las transacciones
     */
    private SummaryDTO buildSummaryComplete(String userId, List<Transaction> allTransactions, LocalDate now) {
        // Calcular balance total usando todas las transacciones
        double totalIncome = allTransactions.stream()
            .filter(t -> t.getType() == TransactionType.INCOME)
            .mapToDouble(Transaction::getAmount)
            .sum();
            
        double totalExpenses = allTransactions.stream()
            .filter(t -> t.getType() == TransactionType.EXPENSE)
            .mapToDouble(Transaction::getAmount)
            .sum();

        Double totalBalance = totalIncome - totalExpenses;
        
        // Obtener datos del mes actual para ingresos/gastos mensuales
        LocalDate monthStart = now.withDayOfMonth(1);
        LocalDateTime monthStartDateTime = convertToStartOfDay(monthStart);
        LocalDateTime nowDateTime = convertToEndOfDay(now);
        List<Transaction> monthTransactions = transactionRepository
            .findByUserIdAndDateBetween(userId, monthStartDateTime, nowDateTime);
            
        double monthlyIncome = monthTransactions.stream()
            .filter(t -> t.getType() == TransactionType.INCOME)
            .mapToDouble(Transaction::getAmount)
            .sum();
            
        double monthlyExpenses = monthTransactions.stream()
            .filter(t -> t.getType() == TransactionType.EXPENSE)
            .mapToDouble(Transaction::getAmount)
            .sum();

        // Calcular tasa de ahorro
        double savingsRate = monthlyIncome > 0 ? ((monthlyIncome - monthlyExpenses) / monthlyIncome) * 100 : 0;
        savingsRate = Math.round(savingsRate * 10.0) / 10.0;

        return new SummaryDTO(totalBalance, monthlyIncome, monthlyExpenses, savingsRate);
    }

    // Métodos de compatibilidad con la API anterior
    public Map<String, Object> getReport(String userId, LocalDate start, LocalDate end) {
        LocalDateTime startDateTime = convertToStartOfDay(start);
        LocalDateTime endDateTime = convertToEndOfDay(end);
        List<Transaction> transactions = transactionRepository.findByUserIdAndDateBetween(userId, startDateTime, endDateTime);

        double totalIncome = transactions.stream()
            .filter(t -> t.getType().toString().equalsIgnoreCase("income"))
            .mapToDouble(Transaction::getAmount)
            .sum();

        double totalExpense = transactions.stream()
            .filter(t -> t.getType().toString().equalsIgnoreCase("expense"))
            .mapToDouble(Transaction::getAmount)
            .sum();

        Map<String, Object> result = new HashMap<>();
        result.put("totalIncome", totalIncome);
        result.put("totalExpense", totalExpense);

        return result;
    }
}
