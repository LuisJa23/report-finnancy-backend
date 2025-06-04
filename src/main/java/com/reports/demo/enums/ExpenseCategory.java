package com.reports.demo.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ExpenseCategory {
    HOGAR("Hogar"),
    TARJETA_CREDITO("Tarjeta de crédito"),
    TRANSPORTE("Transporte"),
    SUPERMERCADO("Supermercado"),
    TIENDAS("Tiendas"),
    SALARIO("Salario"), // Categoría para ingresos por salario
    VENTA("Venta"), // Categoría para ingresos por ventas
    OTRO("Otro"); // Categoría por defecto

    private final String displayName;

    ExpenseCategory(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    // Método para obtener enum desde string
    @JsonCreator
    public static ExpenseCategory fromString(String category) {
        if (category == null || category.trim().isEmpty()) {
            return OTRO;
        }
        
        // Normalizar entrada para comparación
        String normalizedCategory = category.trim();
        
        System.out.println("DEBUG fromString: Buscando categoría para: '" + normalizedCategory + "'");
        
        // Primero intentar buscar por nombre del enum (SALARIO, VENTA, etc.)
        try {
            ExpenseCategory enumByName = ExpenseCategory.valueOf(normalizedCategory.toUpperCase());
            System.out.println("DEBUG fromString: Encontrado por nombre del enum: " + enumByName);
            return enumByName;
        } catch (IllegalArgumentException e) {
            // Si no se encuentra por nombre, continuar con displayName
        }
        
        // Buscar coincidencia exacta con displayName
        for (ExpenseCategory expenseCategory : ExpenseCategory.values()) {
            System.out.println("DEBUG fromString: Comparando '" + normalizedCategory + "' con '" + expenseCategory.displayName + "'");
            if (expenseCategory.displayName.equalsIgnoreCase(normalizedCategory)) {
                System.out.println("DEBUG fromString: ¡Encontrada coincidencia por displayName! Devolviendo: " + expenseCategory);
                return expenseCategory;
            }
        }
        
        System.out.println("DEBUG fromString: No se encontró coincidencia para '" + category + "', usando OTRO por defecto");
        return OTRO;
    }
}
