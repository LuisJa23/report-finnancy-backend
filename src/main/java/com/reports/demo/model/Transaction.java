package com.reports.demo.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Field;
import com.reports.demo.enums.ExpenseType;
import com.reports.demo.enums.ExpenseCategory;
import com.reports.demo.enums.PaymentMethod;
import com.reports.demo.enums.TransactionType;
import java.time.LocalDateTime;

// Entidad principal para transacciones
@Document(collection = "transactions")
public class Transaction {
    @Id
    private String id;
    private Double amount;            // Monto de la transacción
    @Transient
    private ExpenseCategory category; // Categoría del gasto (Hogar, Transporte, etc.)
    @Field("category")
    private String categoryString;    // String representation of category for MongoDB
    private LocalDateTime date;           // Fecha de la transacción
    private String description;       // Descripción opcional
    private TransactionType type;      // INCOME/EXPENSE
    private ExpenseType expenseType;   // FIXED/VARIABLE (solo para gastos)
    private PaymentMethod paymentMethod; // Efectivo, tarjeta, transferencia
    private String userId;            // Usuario dueño de la transacción
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Double getAmount() {
        return amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    public ExpenseCategory getCategory() {
        if (category == null && categoryString != null) {
            category = ExpenseCategory.fromString(categoryString);
        }
        return category != null ? category : ExpenseCategory.OTRO;
    }
    
    public void setCategory(ExpenseCategory category) {
        this.category = category;
        this.categoryString = category != null ? category.getDisplayName() : ExpenseCategory.OTRO.getDisplayName();
    }
    
    // Método helper para obtener el nombre de display de la categoría
    public String getCategoryDisplayName() {
        ExpenseCategory cat = getCategory();
        return cat != null ? cat.getDisplayName() : ExpenseCategory.OTRO.getDisplayName();
    }
    
    // Método helper para establecer categoría desde string
    public void setCategoryFromString(String categoryStr) {
        ExpenseCategory parsedCategory = ExpenseCategory.fromString(categoryStr);
        this.category = parsedCategory;
        this.categoryString = parsedCategory.getDisplayName();
    }
    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public TransactionType getType() {
        return type;
    }
    public void setType(TransactionType type) {
        this.type = type;
    }
    public ExpenseType getExpenseType() {
        return expenseType;
    }
    public void setExpenseType(ExpenseType expenseType) {
        this.expenseType = expenseType;
    }
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }
    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
   
}


