package org.example.controller;

import org.example.bean.CalorieRecord;
import org.example.bean.PageResult;
import org.example.bean.Result;
import org.example.service.CalorieRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/calorie")
public class CalorieRecordController {

    @Autowired
    private CalorieRecordService calorieRecordService;

    /**
     * 分页查询卡路里记录
     * @param foodName 食物名称
     * @param mealType 餐次类型
     * @param recordDate 记录日期
     * @param page 页码
     * @param pageSize 每页记录数
     * @return 分页结果
     */
    @GetMapping("/list")
    public Result list(
            @RequestParam(required = false) String foodName,
            @RequestParam(required = false) String mealType,
            @RequestParam(required = false) LocalDate recordDate,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        PageResult<CalorieRecord> pageResult = calorieRecordService.page(foodName, mealType, recordDate, page, pageSize);
        return Result.success(pageResult);
    }

    /**
     * 添加卡路里记录
     * @param calorieRecord 卡路里记录
     * @return 操作结果
     */
    @PostMapping
    public Result add(@RequestBody CalorieRecord calorieRecord) {
        calorieRecordService.add(calorieRecord);
        return Result.success();
    }

    /**
     * 根据ID删除卡路里记录
     * @param id 记录ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        calorieRecordService.deleteById(id);
        return Result.success();
    }

    /**
     * 更新卡路里记录
     * @param calorieRecord 卡路里记录
     * @return 操作结果
     */
    @PutMapping
    public Result update(@RequestBody CalorieRecord calorieRecord) {
        calorieRecordService.update(calorieRecord);
        return Result.success();
    }

    /**
     * 获取卡路里统计分析数据
     * @return 统计分析数据
     */
    @GetMapping("/analytics")
    public Result getAnalytics() {
        // 获取今日卡路里
        LocalDate today = LocalDate.now();
        Double todayCalories = calorieRecordService.getTotalCaloriesByDateRange(today, today);
        
        // 获取本周卡路里
        LocalDate weekStart = today.minusDays(today.getDayOfWeek().getValue() - 1);
        Double weekCalories = calorieRecordService.getTotalCaloriesByDateRange(weekStart, today);
        
        // 获取本月卡路里
        LocalDate monthStart = today.withDayOfMonth(1);
        Double monthCalories = calorieRecordService.getTotalCaloriesByDateRange(monthStart, today);
        
        // 计算日均卡路里（基于本月数据）
        int daysInMonth = today.getDayOfMonth();
        Double dailyAverage = monthCalories != null ? monthCalories / daysInMonth : 0.0;
        
        // 构建返回数据
        java.util.Map<String, Object> analytics = new java.util.HashMap<>();
        analytics.put("todayCalories", todayCalories != null ? todayCalories : 0.0);
        analytics.put("weekCalories", weekCalories != null ? weekCalories : 0.0);
        analytics.put("monthCalories", monthCalories != null ? monthCalories : 0.0);
        analytics.put("dailyAverage", dailyAverage);
        
        return Result.success(analytics);
    }

    /**
     * 根据ID查询卡路里记录
     * @param id 记录ID
     * @return 卡路里记录
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        CalorieRecord calorieRecord = calorieRecordService.getById(id);
        return Result.success(calorieRecord);
    }

    /**
     * 根据日期范围统计总卡路里
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 总卡路里
     */
    @GetMapping("/total")
    public Result getTotalCalories(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        Double totalCalories = calorieRecordService.getTotalCaloriesByDateRange(startDate, endDate);
        return Result.success(totalCalories);
    }

    /**
     * 根据餐次类型和日期统计卡路里
     * @param mealType 餐次类型
     * @param recordDate 记录日期
     * @return 卡路里总数
     */
    @GetMapping("/meal-calories")
    public Result getMealCalories(
            @RequestParam String mealType,
            @RequestParam LocalDate recordDate
    ) {
        Double calories = calorieRecordService.getCaloriesByMealTypeAndDate(mealType, recordDate);
        return Result.success(calories);
    }
}