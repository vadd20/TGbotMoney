package com.example.TGbotMoney.repository;

import com.example.TGbotMoney.model.Expense;
import com.example.TGbotMoney.model.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {
    @Query("SELECT i FROM Income i WHERE i.user_id = :userId AND i.date > :startOfDay")
    List<Income> findAllAfterDate(@Param("userId") long userId, @Param("startOfDay") LocalDateTime startOfDay);
}
