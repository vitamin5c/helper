package org.example;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.example.bean.CalorieRecord;
import org.example.bean.PageResult;
import org.example.mapper.CalorieRecordMapper;
import org.example.service.Impl.CalorieRecordServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CalorieRecordServiceImplTest {

    @Mock
    private CalorieRecordMapper calorieRecordMapper;

    @InjectMocks
    private CalorieRecordServiceImpl calorieRecordService;

    private CalorieRecord testCalorieRecord;

    @BeforeEach
    void setUp() {
        testCalorieRecord = new CalorieRecord();
        testCalorieRecord.setId(1);
        testCalorieRecord.setFoodName("Apple");
        testCalorieRecord.setCalories(95.0);
        testCalorieRecord.setServingSize(150.0);
        testCalorieRecord.setMealType("Snack");
        testCalorieRecord.setDescription("Fresh red apple");
        testCalorieRecord.setRecordDate(LocalDate.now());
    }

    @Test
    void testPage() {
        // Arrange
        String foodName = "Apple";
        String mealType = "Snack";
        LocalDate recordDate = LocalDate.now();
        Integer page = 1;
        Integer pageSize = 10;

        List<CalorieRecord> mockList = Arrays.asList(testCalorieRecord);
        Page<CalorieRecord> mockPage = new Page<>();
        mockPage.addAll(mockList);
        mockPage.setTotal(1L);

        try (MockedStatic<PageHelper> pageHelperMock = mockStatic(PageHelper.class)) {
            when(calorieRecordMapper.list(foodName, mealType, recordDate))
                    .thenReturn(mockPage);

            // Act
            PageResult<CalorieRecord> result = calorieRecordService.page(foodName, mealType, recordDate, page, pageSize);

            // Assert
            assertNotNull(result);
            assertEquals(1L, result.getTotal());
            assertEquals(1, result.getRows().size());
            assertEquals(testCalorieRecord.getId(), result.getRows().get(0).getId());
        }
        
        // Verify interactions
        verify(calorieRecordMapper).list(foodName, mealType, recordDate);
    }

    @Test
    void testPageWithNullParameters() {
        // Arrange
        Integer page = 1;
        Integer pageSize = 10;

        List<CalorieRecord> mockList = Arrays.asList(testCalorieRecord);
        Page<CalorieRecord> mockPage = new Page<>();
        mockPage.addAll(mockList);
        mockPage.setTotal(1L);

        try (MockedStatic<PageHelper> pageHelperMock = mockStatic(PageHelper.class)) {
            when(calorieRecordMapper.list(null, null, null))
                    .thenReturn(mockPage);

            // Act
            PageResult<CalorieRecord> result = calorieRecordService.page(null, null, null, page, pageSize);

            // Assert
            assertNotNull(result);
            assertEquals(1L, result.getTotal());
            assertEquals(1, result.getRows().size());
        }
        
        // Verify interactions
        verify(calorieRecordMapper).list(null, null, null);
    }

    @Test
    void testPageEmptyResult() {
        // Arrange
        String foodName = "NonExistent";
        String mealType = "Breakfast";
        LocalDate recordDate = LocalDate.now();
        Integer page = 1;
        Integer pageSize = 10;

        Page<CalorieRecord> mockPage = new Page<>();
        mockPage.setTotal(0L);

        try (MockedStatic<PageHelper> pageHelperMock = mockStatic(PageHelper.class)) {
            when(calorieRecordMapper.list(foodName, mealType, recordDate))
                    .thenReturn(mockPage);

            // Act
            PageResult<CalorieRecord> result = calorieRecordService.page(foodName, mealType, recordDate, page, pageSize);

            // Assert
            assertNotNull(result);
            assertEquals(0L, result.getTotal());
            assertEquals(0, result.getRows().size());
        }
        
        // Verify interactions
        verify(calorieRecordMapper).list(foodName, mealType, recordDate);
    }

    @Test
    void testAdd() {
        // Act
        calorieRecordService.add(testCalorieRecord);

        // Assert
        verify(calorieRecordMapper).add(testCalorieRecord);
    }

    @Test
    void testDeleteById() {
        // Arrange
        Integer id = 1;

        // Act
        calorieRecordService.deleteById(id);

        // Assert
        verify(calorieRecordMapper).deleteById(id);
    }

    @Test
    void testDeleteByIdWithNullId() {
        // Act & Assert
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            calorieRecordService.deleteById(null);
        });
        
        assertEquals("ID cannot be null", exception.getMessage());
        verify(calorieRecordMapper, never()).deleteById(any());
    }

    @Test
    void testUpdate() {
        // Act
        calorieRecordService.update(testCalorieRecord);

        // Assert
        verify(calorieRecordMapper).updateById(testCalorieRecord);
    }

    @Test
    void testGetById() {
        // Arrange
        Integer id = 1;
        when(calorieRecordMapper.getById(id)).thenReturn(testCalorieRecord);

        // Act
        CalorieRecord result = calorieRecordService.getById(id);

        // Assert
        assertNotNull(result);
        assertEquals(testCalorieRecord.getId(), result.getId());
        assertEquals(testCalorieRecord.getFoodName(), result.getFoodName());
        assertEquals(testCalorieRecord.getCalories(), result.getCalories());
        assertEquals(testCalorieRecord.getServingSize(), result.getServingSize());
        assertEquals(testCalorieRecord.getMealType(), result.getMealType());
        assertEquals(testCalorieRecord.getDescription(), result.getDescription());
        assertEquals(testCalorieRecord.getRecordDate(), result.getRecordDate());
        verify(calorieRecordMapper).getById(id);
    }

    @Test
    void testGetByIdNotFound() {
        // Arrange
        Integer id = 999;
        when(calorieRecordMapper.getById(id)).thenReturn(null);

        // Act
        CalorieRecord result = calorieRecordService.getById(id);

        // Assert
        assertNull(result);
        verify(calorieRecordMapper).getById(id);
    }

    @Test
    void testGetTotalCaloriesByDateRange() {
        // Arrange
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();
        Double expectedTotal = 500.0;
        
        when(calorieRecordMapper.getTotalCaloriesByDateRange(startDate, endDate))
                .thenReturn(expectedTotal);

        // Act
        Double result = calorieRecordService.getTotalCaloriesByDateRange(startDate, endDate);

        // Assert
        assertNotNull(result);
        assertEquals(expectedTotal, result);
        verify(calorieRecordMapper).getTotalCaloriesByDateRange(startDate, endDate);
    }

    @Test
    void testGetTotalCaloriesByDateRangeNoRecords() {
        // Arrange
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();
        Double expectedTotal = 0.0;
        
        when(calorieRecordMapper.getTotalCaloriesByDateRange(startDate, endDate))
                .thenReturn(expectedTotal);

        // Act
        Double result = calorieRecordService.getTotalCaloriesByDateRange(startDate, endDate);

        // Assert
        assertNotNull(result);
        assertEquals(expectedTotal, result);
        verify(calorieRecordMapper).getTotalCaloriesByDateRange(startDate, endDate);
    }

    @Test
    void testGetCaloriesByMealTypeAndDate() {
        // Arrange
        String mealType = "Breakfast";
        LocalDate recordDate = LocalDate.now();
        Double expectedCalories = 350.0;
        
        when(calorieRecordMapper.getCaloriesByMealTypeAndDate(mealType, recordDate))
                .thenReturn(expectedCalories);

        // Act
        Double result = calorieRecordService.getCaloriesByMealTypeAndDate(mealType, recordDate);

        // Assert
        assertNotNull(result);
        assertEquals(expectedCalories, result);
        verify(calorieRecordMapper).getCaloriesByMealTypeAndDate(mealType, recordDate);
    }

    @Test
    void testGetCaloriesByMealTypeAndDateNoRecords() {
        // Arrange
        String mealType = "Dinner";
        LocalDate recordDate = LocalDate.now();
        Double expectedCalories = 0.0;
        
        when(calorieRecordMapper.getCaloriesByMealTypeAndDate(mealType, recordDate))
                .thenReturn(expectedCalories);

        // Act
        Double result = calorieRecordService.getCaloriesByMealTypeAndDate(mealType, recordDate);

        // Assert
        assertNotNull(result);
        assertEquals(expectedCalories, result);
        verify(calorieRecordMapper).getCaloriesByMealTypeAndDate(mealType, recordDate);
    }
}