package com.reports.demo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.reports.demo.model.SavingsGoal;

import java.time.LocalDate;

/**
 * Repositorio para operaciones CRUD de metas de ahorro
 */
@Repository
public interface SavingsGoalRepository extends MongoRepository<SavingsGoal, String> {
    List<SavingsGoal> findByUserId(String userId);
    List<SavingsGoal> findByUserIdAndCompletedFalse(String userId);
    List<SavingsGoal> findByUserIdAndCompletedTrue(String userId);
    List<SavingsGoal> findByTargetDateBetween(LocalDate start, LocalDate end); // <-- Para reportes globales
}