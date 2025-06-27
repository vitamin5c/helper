package org.example.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.example.bean.CalorieRecord;
import org.example.bean.PageResult;
import org.example.mapper.CalorieRecordMapper;
import org.example.service.CalorieRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CalorieRecordServiceImpl implements CalorieRecordService {

    @Autowired
    private CalorieRecordMapper calorieRecordMapper;

    @Override
    public PageResult<CalorieRecord> page(String foodName, String mealType, LocalDate recordDate, Integer page, Integer pageSize) {
        //1. 设置分页参数
        PageHelper.startPage(page, pageSize);

        //2. 执行查询
        List<CalorieRecord> calorieRecordList = calorieRecordMapper.list(foodName, mealType, recordDate);
        Page<CalorieRecord> p = (Page<CalorieRecord>) calorieRecordList;

        //3. 封装结果
        PageResult<CalorieRecord> pageResult = new PageResult<>();
        pageResult.setTotal(p.getTotal());
        pageResult.setRows(p.getResult());
        return pageResult;
    }

    @Override
    public void add(CalorieRecord calorieRecord) {
        calorieRecordMapper.add(calorieRecord);
    }

    @Override
    public void deleteById(Integer id) {
        if (id == null) {
            throw new NullPointerException("ID cannot be null");
        }
        calorieRecordMapper.deleteById(id);
    }

    @Override
    public void update(CalorieRecord calorieRecord) {
        calorieRecordMapper.updateById(calorieRecord);
    }

    @Override
    public CalorieRecord getById(Integer id) {
        return calorieRecordMapper.getById(id);
    }

    @Override
    public Double getTotalCaloriesByDateRange(LocalDate startDate, LocalDate endDate) {
        return calorieRecordMapper.getTotalCaloriesByDateRange(startDate, endDate);
    }

    @Override
    public Double getCaloriesByMealTypeAndDate(String mealType, LocalDate recordDate) {
        return calorieRecordMapper.getCaloriesByMealTypeAndDate(mealType, recordDate);
    }
}