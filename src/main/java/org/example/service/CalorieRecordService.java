package org.example.service;

import org.example.bean.CalorieRecord;
import org.example.bean.PageResult;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public interface CalorieRecordService {
    
    /**
     * 分页查询卡路里记录
     * @param foodName 食物名称
     * @param mealType 餐次类型
     * @param recordDate 记录日期
     * @param page 页码
     * @param pageSize 每页记录数
     * @return 分页结果
     */
    PageResult<CalorieRecord> page(String foodName, String mealType, LocalDate recordDate, Integer page, Integer pageSize);

    /**
     * 添加卡路里记录
     * @param calorieRecord 卡路里记录
     */
    void add(CalorieRecord calorieRecord);

    /**
     * 根据ID删除卡路里记录
     * @param id 记录ID
     */
    void deleteById(Integer id);

    /**
     * 更新卡路里记录
     * @param calorieRecord 卡路里记录
     */
    void update(CalorieRecord calorieRecord);

    /**
     * 根据ID查询卡路里记录
     * @param id 记录ID
     * @return 卡路里记录
     */
    CalorieRecord getById(Integer id);

    /**
     * 根据日期范围统计总卡路里
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 总卡路里
     */
    Double getTotalCaloriesByDateRange(LocalDate startDate, LocalDate endDate);

    /**
     * 根据餐次类型和日期统计卡路里
     * @param mealType 餐次类型
     * @param recordDate 记录日期
     * @return 卡路里总数
     */
    Double getCaloriesByMealTypeAndDate(String mealType, LocalDate recordDate);
}