package org.example.bean;

import java.time.LocalDate;

public class CalorieRecord {
    private Integer id;              // 主键
    private String foodName;         // 食物名称
    private Double calories;         // 卡路里数量
    private Double servingSize;      // 份量大小(克)
    private String mealType;         // 餐次类型(早餐/午餐/晚餐/零食)
    private String description;      // 描述
    private LocalDate recordDate;    // 记录日期
    
    public CalorieRecord() {}
    
    public CalorieRecord(Integer id, String foodName, Double calories, Double servingSize, String mealType, String description, LocalDate recordDate) {
        this.id = id;
        this.foodName = foodName;
        this.calories = calories;
        this.servingSize = servingSize;
        this.mealType = mealType;
        this.description = description;
        this.recordDate = recordDate;
    }
    
    // Explicit getters and setters to ensure they are available
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getFoodName() {
        return foodName;
    }
    
    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }
    
    public Double getCalories() {
        return calories;
    }
    
    public void setCalories(Double calories) {
        this.calories = calories;
    }
    
    public Double getServingSize() {
        return servingSize;
    }
    
    public void setServingSize(Double servingSize) {
        this.servingSize = servingSize;
    }
    
    public String getMealType() {
        return mealType;
    }
    
    public void setMealType(String mealType) {
        this.mealType = mealType;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDate getRecordDate() {
        return recordDate;
    }
    
    public void setRecordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
    }
}