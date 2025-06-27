package org.example;

import org.example.bean.CalorieRecord;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CalorieRecordTest {

    @Test
    void testCalorieRecordCreationAndGettersSetters() {
        // Test default constructor
        CalorieRecord calorieRecord = new CalorieRecord();
        assertNotNull(calorieRecord);
        
        // Test setters
        calorieRecord.setId(1);
        calorieRecord.setFoodName("Apple");
        calorieRecord.setCalories(95.0);
        calorieRecord.setServingSize(150.0);
        calorieRecord.setMealType("Snack");
        calorieRecord.setDescription("Fresh red apple");
        calorieRecord.setRecordDate(LocalDate.now());
        
        // Test getters
        assertEquals(Integer.valueOf(1), calorieRecord.getId());
        assertEquals("Apple", calorieRecord.getFoodName());
        assertEquals(95.0, calorieRecord.getCalories());
        assertEquals(150.0, calorieRecord.getServingSize());
        assertEquals("Snack", calorieRecord.getMealType());
        assertEquals("Fresh red apple", calorieRecord.getDescription());
        assertEquals(LocalDate.now(), calorieRecord.getRecordDate());
    }
    
    @Test
    void testCalorieRecordAllArgsConstructor() {
        LocalDate testDate = LocalDate.now();
        CalorieRecord calorieRecord = new CalorieRecord(
            1, 
            "Banana", 
            105.0, 
            120.0, 
            "Breakfast", 
            "Yellow banana", 
            testDate
        );
        
        assertEquals(Integer.valueOf(1), calorieRecord.getId());
        assertEquals("Banana", calorieRecord.getFoodName());
        assertEquals(105.0, calorieRecord.getCalories());
        assertEquals(120.0, calorieRecord.getServingSize());
        assertEquals("Breakfast", calorieRecord.getMealType());
        assertEquals("Yellow banana", calorieRecord.getDescription());
        assertEquals(testDate, calorieRecord.getRecordDate());
    }
}