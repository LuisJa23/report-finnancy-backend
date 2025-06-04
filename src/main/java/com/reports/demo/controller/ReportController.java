package com.reports.demo.controller;

import com.reports.demo.service.ReportService;
import com.reports.demo.dto.ReportResponseDTO;
import com.reports.demo.model.Transaction;
import com.reports.demo.repository.TransactionRepository;
import com.reports.demo.enums.TransactionType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportService reportService;
    private final TransactionRepository transactionRepository;

    public ReportController(ReportService reportService, TransactionRepository transactionRepository) {
        this.reportService = reportService;
        this.transactionRepository = transactionRepository;
    }

    @GetMapping("/financial-report/{userId}")
    public ReportResponseDTO getFinancialReport(@PathVariable String userId) {
        return reportService.generateFinancialReport(userId);
    }

    @GetMapping
    public ReportResponseDTO getReport(
            @RequestParam(required = false) String userId,
            @RequestParam(defaultValue = "weekly") String interval,
            @RequestParam(defaultValue = "false") boolean global,
            @RequestParam(required = false) String type
    ) {
        LocalDate now = LocalDate.now();
        return reportService.getReport(userId, interval, now, global, type);
    }

    @GetMapping("/debug-transactions/{userId}")
    public Map<String, Object> debugUserTransactions(@PathVariable String userId) {
        List<Transaction> allTransactions = transactionRepository.findByUserId(userId);
        
        Map<String, Object> debug = new HashMap<>();
        debug.put("totalTransactions", allTransactions.size());
        debug.put("userId", userId);
        
        // Agrupar por tipo
        Map<String, Long> byType = allTransactions.stream()
            .collect(Collectors.groupingBy(
                t -> t.getType().toString(),
                Collectors.counting()
            ));
        debug.put("transactionsByType", byType);
        
        // Agrupar gastos por categoría
        Map<String, Double> expensesByCategory = allTransactions.stream()
            .filter(t -> t.getType() == TransactionType.EXPENSE)
            .collect(Collectors.groupingBy(
                Transaction::getCategoryDisplayName,
                Collectors.summingDouble(Transaction::getAmount)
            ));
        debug.put("expensesByCategory", expensesByCategory);
        
        // Listar todas las transacciones
        List<Map<String, Object>> transactionList = allTransactions.stream()
            .map(t -> {
                Map<String, Object> tMap = new HashMap<>();
                tMap.put("id", t.getId());
                tMap.put("amount", t.getAmount());
                tMap.put("category", t.getCategoryDisplayName());
                tMap.put("type", t.getType());
                tMap.put("date", t.getDate());
                tMap.put("description", t.getDescription());
                return tMap;
            })
            .toList();
        debug.put("allTransactions", transactionList);
        
        return debug;
    }
}