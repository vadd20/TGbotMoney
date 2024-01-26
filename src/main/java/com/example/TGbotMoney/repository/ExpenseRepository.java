package com.example.TGbotMoney.repository;

import com.example.TGbotMoney.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    @Query("SELECT e FROM Expense e WHERE e.user_id = :userId AND e.date > :startOfDay")
    List<Expense> findAllAfterDate(@Param("userId") long userId,@Param("startOfDay") LocalDateTime startOfDay);
}
