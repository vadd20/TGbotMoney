package com.example.TGbotMoney.repository;

import com.example.TGbotMoney.model.Expense;
import com.example.TGbotMoney.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
}
