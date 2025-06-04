package com.reports.demo.repository;

import com.reports.demo.model.SavingsHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SavingsHistoryRepository extends MongoRepository<SavingsHistory, String> {
    
    List<SavingsHistory> findByUserIdOrderByDateAsc(String userId);
    
    List<SavingsHistory> findByUserIdAndDateBetweenOrderByDateAsc(String userId, LocalDate start, LocalDate end);
    
    Optional<SavingsHistory> findByUserIdAndDate(String userId, LocalDate date);
    
    List<SavingsHistory> findTop6ByUserIdOrderByDateDesc(String userId);
}
