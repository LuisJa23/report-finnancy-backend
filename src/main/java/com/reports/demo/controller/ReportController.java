package com.reports.demo.controller;

import com.reports.demo.service.ReportService;
import com.reports.demo.dto.ReportResponseDTO;
import com.reports.demo.model.Transaction;
import com.reports.demo.repository.TransactionRepository;
import com.reports.demo.enums.TransactionType;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportService reportService;
    private final TransactionRepository transactionRepository;
    private final MongoTemplate mongoTemplate;

    public ReportController(ReportService reportService, TransactionRepository transactionRepository, MongoTemplate mongoTemplate) {
        this.reportService = reportService;
        this.transactionRepository = transactionRepository;
        this.mongoTemplate = mongoTemplate;
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
    
    @GetMapping("/enhanced-debug/{userId}")
    public Map<String, Object> enhancedDebug(@PathVariable String userId) {
        Map<String, Object> debug = new HashMap<>();
        
        try {
            // 1. Verificar conectividad con MongoDB
            boolean isConnected = false;
            String connectionStatus = "";
            try {
                mongoTemplate.getCollection("transactions").countDocuments();
                isConnected = true;
                connectionStatus = "Conectado correctamente a MongoDB";
            } catch (Exception e) {
                connectionStatus = "Error de conexión: " + e.getMessage();
            }
            debug.put("mongoConnected", isConnected);
            debug.put("connectionStatus", connectionStatus);
            
            // 2. Contar total de documentos en la colección
            long totalDocuments = mongoTemplate.getCollection("transactions").countDocuments();
            debug.put("totalDocumentsInCollection", totalDocuments);
            
            // 3. Buscar documentos con diferentes variaciones del userId
            List<Transaction> exactMatch = transactionRepository.findByUserId(userId);
            debug.put("exactMatchCount", exactMatch.size());
            debug.put("searchedUserId", userId);
            debug.put("userIdLength", userId.length());
            
            // 4. Buscar todos los userId únicos en la colección para comparar
            List<String> allUserIds = mongoTemplate.getCollection("transactions")
                .distinct("userId", String.class)
                .into(new ArrayList<>());
            debug.put("uniqueUserIdsInDB", allUserIds);
            debug.put("totalUniqueUsers", allUserIds.size());
            
            // 5. Verificar si hay userId similares
            List<String> similarUserIds = allUserIds.stream()
                .filter(id -> id != null && (id.contains(userId) || userId.contains(id)))
                .collect(Collectors.toList());
            debug.put("similarUserIds", similarUserIds);
            
            // 6. Buscar documentos con userId que contenga parte del string buscado
            Query partialQuery = new Query(Criteria.where("userId").regex(userId.substring(0, Math.min(10, userId.length())), "i"));
            List<Transaction> partialMatches = mongoTemplate.find(partialQuery, Transaction.class);
            debug.put("partialMatchCount", partialMatches.size());
            
            // 7. Mostrar algunos documentos de ejemplo de la colección
            List<Transaction> sampleTransactions = mongoTemplate.find(
                new Query().limit(3), 
                Transaction.class
            );
            List<Map<String, Object>> sampleData = sampleTransactions.stream()
                .map(t -> {
                    Map<String, Object> tMap = new HashMap<>();
                    tMap.put("id", t.getId());
                    tMap.put("userId", t.getUserId());
                    tMap.put("amount", t.getAmount());
                    tMap.put("type", t.getType());
                    tMap.put("description", t.getDescription());
                    return tMap;
                })
                .collect(Collectors.toList());
            debug.put("sampleTransactions", sampleData);
            
            // 8. Información del entorno
            debug.put("timestamp", new Date());
            debug.put("environment", System.getProperty("java.version"));
            
        } catch (Exception e) {
            debug.put("error", "Error durante el debug: " + e.getMessage());
            debug.put("stackTrace", Arrays.toString(e.getStackTrace()));
        }
        
        return debug;
    }
}