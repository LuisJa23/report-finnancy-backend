package com.reports.demo.controller;

import com.reports.demo.model.SpendingTrend;
import com.reports.demo.service.SpendingTrendsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador REST para análisis de tendencias financieras
 */
@RestController
@RequestMapping("/api/insights")
public class SpendingTrendsController {

    @Autowired
    private SpendingTrendsService spendingTrendsService;

    /**
     * Analiza las tendencias de gasto de un usuario en un rango de fechas específico.
     * 
     * Funcionalidades:
     * - Agrupa gastos por categoría y período (mes)
     * - Calcula totales y conteo de transacciones
     * - Muestra porcentaje de crecimiento entre períodos consecutivos
     * - Si no se proporcionan fechas, analiza los últimos 12 meses
     * 
     * @param userId ID del usuario (requerido)
     * @param startDate Fecha de inicio (opcional, formato: YYYY-MM-DD)
     * @param endDate Fecha de fin (opcional, formato: YYYY-MM-DD)
     * @return Lista de tendencias de gasto
     */
    @GetMapping("/spending-trends")
    public ResponseEntity<List<SpendingTrend>> getSpendingTrends(
            @RequestParam String userId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate
    ) {
        try {
            // Si no se proporcionan fechas, usar los últimos 12 meses
            if (startDate == null) {
                startDate = LocalDate.now().minusMonths(12);
            }
            if (endDate == null) {
                endDate = LocalDate.now();
            }

            // Validar que startDate no sea posterior a endDate
            if (startDate.isAfter(endDate)) {
                return ResponseEntity.badRequest().build();
            }

            List<SpendingTrend> trends = spendingTrendsService.getSpendingTrends(userId, startDate, endDate);
            return ResponseEntity.ok(trends);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Analiza las tendencias financieras completas (gastos e ingresos) de un usuario en un rango de fechas.
     * 
     * Funcionalidades:
     * - Agrupa gastos por categoría y período (mes)
     * - Agrupa ingresos por período (mes)
     * - Calcula totales y conteo de transacciones
     * - Muestra porcentaje de crecimiento entre períodos consecutivos
     * - Si no se proporcionan fechas, analiza los últimos 12 meses
     * 
     * @param userId ID del usuario (requerido)
     * @param startDate Fecha de inicio (opcional, formato: YYYY-MM-DD)
     * @param endDate Fecha de fin (opcional, formato: YYYY-MM-DD)
     * @return Lista de tendencias financieras
     */
    @GetMapping("/financial-trends")
    public ResponseEntity<List<SpendingTrend>> getFinancialTrends(
            @RequestParam String userId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate
    ) {
        try {
            // Si no se proporcionan fechas, usar los últimos 12 meses
            if (startDate == null) {
                startDate = LocalDate.now().minusMonths(12);
            }
            if (endDate == null) {
                endDate = LocalDate.now();
            }

            // Validar que startDate no sea posterior a endDate
            if (startDate.isAfter(endDate)) {
                return ResponseEntity.badRequest().build();
            }

            List<SpendingTrend> trends = spendingTrendsService.getFinancialTrends(userId, startDate, endDate);
            return ResponseEntity.ok(trends);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
