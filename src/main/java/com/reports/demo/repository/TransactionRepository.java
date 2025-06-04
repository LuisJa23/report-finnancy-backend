// TransactionRepository.java
package com.reports.demo.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.reports.demo.enums.TransactionType;
import com.reports.demo.model.Transaction;

/**
 * Repositorio para operaciones CRUD de transacciones
 */

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {
    
    // Métodos existentes que siguen funcionando con rangos de días
    List<Transaction> findByDateBetween(LocalDateTime start, LocalDateTime end); // <-- Para reportes globales
    
    // Nuevos métodos para filtrar por día completo (mantener compatibilidad)
    @Query("{ 'date': { $gte: ?0, $lt: ?1 } }")
    List<Transaction> findByDateBetweenDays(LocalDateTime startOfDay, LocalDateTime endOfDay);

    // Encontrar transacciones por usuario y tipo
    List<Transaction> findByUserIdAndType(String userId, TransactionType type);
    
    
    // Encontrar transacciones por usuario y rango de fechas (con hora específica)
    List<Transaction> findByUserIdAndDateBetween(String userId, LocalDateTime start, LocalDateTime end);
    
    // Nuevo método para filtrar por día completo para un usuario
    @Query("{ 'userId': ?0, 'date': { $gte: ?1, $lt: ?2 } }")
    List<Transaction> findByUserIdAndDateBetweenDays(String userId, LocalDateTime startOfDay, LocalDateTime endOfDay);
    
    // Encontrar transacciones por usuario, categoría y tipo
    List<Transaction> findByUserIdAndCategoryAndType(String userId, String category, TransactionType type);
    
    List<Transaction> findByUserId(String userId);

    // Nuevos métodos para reportes avanzados con LocalDateTime
    List<Transaction> findByUserIdAndTypeAndDateBetween(String userId, TransactionType type, LocalDateTime start, LocalDateTime end);
    
    List<Transaction> findByTypeAndDateBetween(TransactionType type, LocalDateTime start, LocalDateTime end);
    
    @Query("{ 'userId': ?0, 'type': ?1, 'date': { $gte: ?2, $lte: ?3 } }")
    List<Transaction> findByUserIdAndTypeInDateRange(String userId, TransactionType type, LocalDateTime start, LocalDateTime end);
    
    @Query("{ 'userId': ?0, 'date': { $gte: ?1, $lte: ?2 } }")
    List<Transaction> findAllByUserInDateRange(String userId, LocalDateTime start, LocalDateTime end);

    // Método para obtener las últimas N transacciones de un usuario ordenadas por fecha descendente
    List<Transaction> findByUserIdOrderByDateDesc(String userId);

}