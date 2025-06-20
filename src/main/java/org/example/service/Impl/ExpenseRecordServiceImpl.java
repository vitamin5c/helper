package org.example.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.example.bean.ExpenseRecord;
import org.example.bean.PageResult;
import org.example.mapper.ExpenseRecordMapper;
import org.example.service.ExpenseRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExpenseRecordServiceImpl implements ExpenseRecordService {

    @Autowired
    private ExpenseRecordMapper expenseRecordMapper;

    @Override
    public PageResult<ExpenseRecord> page(Integer amount, String catagory, LocalDate dateTime, Integer page, Integer pageSize) {
        //1. 设置分页参数
        PageHelper.startPage(page, pageSize);

        //2. 执行查询
        List<ExpenseRecord> commodityList = expenseRecordMapper.list(amount, catagory, dateTime);
        Page<ExpenseRecord> p = (Page<ExpenseRecord>) commodityList;

        //3. 封装结果
        return new PageResult<>(p.getTotal(), p.getResult());
    }

    @Override
    public void add(ExpenseRecord expenseRecord) {
        expenseRecordMapper.add(expenseRecord);
    }

    @Override
    public void deleteById(Integer id) {
        expenseRecordMapper.deleteById(id);
    }

    @Override
    public void update(ExpenseRecord expenseRecord) {
        expenseRecordMapper.updateById(expenseRecord);
    }

    @Override
    public ExpenseRecord getById(Integer id) {
        return expenseRecordMapper.getById(id);
    }
}
