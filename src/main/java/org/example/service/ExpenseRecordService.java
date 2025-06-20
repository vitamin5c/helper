package org.example.service;

import org.example.bean.ExpenseRecord;
import org.example.bean.PageResult;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public interface ExpenseRecordService {
    PageResult<ExpenseRecord> page(Integer maxAmount, Integer minAmount ,String keyword, LocalDate dateTime, Integer page, Integer pageSize);


    void add(ExpenseRecord expenseRecord);

    void deleteById(Integer id);

    void update(ExpenseRecord expenseRecord);

    ExpenseRecord getById(Integer id);
}
