package org.example.mapper;

import org.apache.ibatis.annotations.*;
import org.example.bean.CalorieRecord;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface CalorieRecordMapper {

    /**
     * 分页查询卡路里记录
     * @param foodName 食物名称
     * @param mealType 餐次类型
     * @param recordDate 记录日期
     * @return 卡路里记录列表
     */
    List<CalorieRecord> list(String foodName, String mealType, LocalDate recordDate);

    /**
     * 添加卡路里记录
     * @param calorieRecord 卡路里记录
     */
    @Insert("INSERT INTO calorie_record (food_name, calories, serving_size, meal_type, description, record_date) " +
            "VALUES (#{foodName}, #{calories}, #{servingSize}, #{mealType}, #{description}, #{recordDate})")
    void add(CalorieRecord calorieRecord);

    /**
     * 根据 ID 删除卡路里记录
     * @param id 记录ID
     */
    @Delete("DELETE FROM calorie_record WHERE id = #{id}")
    void deleteById(Integer id);

    /**
     * 根据 ID 更新卡路里记录
     * @param calorieRecord 卡路里记录
     */
    @Update("UPDATE calorie_record SET food_name = #{foodName}, calories = #{calories}, " +
            "serving_size = #{servingSize}, meal_type = #{mealType}, description = #{description}, " +
            "record_date = #{recordDate} WHERE id = #{id}")
    void updateById(CalorieRecord calorieRecord);

    /**
     * 根据 ID 查询卡路里记录
     * @param id 记录ID
     * @return 卡路里记录
     */
    @Select("SELECT * FROM calorie_record WHERE id = #{id}")
    CalorieRecord getById(Integer id);

    /**
     * 根据日期范围统计总卡路里
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 总卡路里
     */
    @Select("SELECT COALESCE(SUM(calories), 0) FROM calorie_record WHERE record_date BETWEEN #{startDate} AND #{endDate}")
    Double getTotalCaloriesByDateRange(LocalDate startDate, LocalDate endDate);

    /**
     * 根据餐次类型和日期统计卡路里
     * @param mealType 餐次类型
     * @param recordDate 记录日期
     * @return 卡路里总数
     */
    @Select("SELECT COALESCE(SUM(calories), 0) FROM calorie_record WHERE meal_type = #{mealType} AND record_date = #{recordDate}")
    Double getCaloriesByMealTypeAndDate(String mealType, LocalDate recordDate);
}