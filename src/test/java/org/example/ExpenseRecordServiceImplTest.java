package org.example;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.example.bean.ExpenseRecord;
import org.example.bean.PageResult;
import org.example.mapper.ExpenseRecordMapper;
import org.example.service.Impl.ExpenseRecordServiceImpl;
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
class ExpenseRecordServiceImplTest {

    @Mock
    private ExpenseRecordMapper expenseRecordMapper;

    @InjectMocks
    private ExpenseRecordServiceImpl expenseRecordService;

    private ExpenseRecord testExpenseRecord;

    @BeforeEach
    void setUp() {
        testExpenseRecord = new ExpenseRecord();
        testExpenseRecord.setId(1);
        testExpenseRecord.setCategory("Food");
        testExpenseRecord.setDescription("Lunch");
        testExpenseRecord.setAmount(25.50);
        testExpenseRecord.setCreateDate(LocalDate.now());
    }

    @Test
    void testPage() {
        // Arrange
        Integer maxAmount = 100;
        Integer minAmount = 10;
        String category = "Food";
        LocalDate dateTime = LocalDate.now();
        Integer page = 1;
        Integer pageSize = 10;

        List<ExpenseRecord> mockList = Arrays.asList(testExpenseRecord);
        Page<ExpenseRecord> mockPage = new Page<>();
        mockPage.addAll(mockList);
        mockPage.setTotal(1L);

        try (MockedStatic<PageHelper> pageHelperMock = mockStatic(PageHelper.class)) {
            when(expenseRecordMapper.list(maxAmount, minAmount, category, dateTime))
                    .thenReturn(mockPage);

            // Act
            PageResult<ExpenseRecord> result = expenseRecordService.page(maxAmount, minAmount, category, dateTime, page, pageSize);

            // Assert
            assertNotNull(result);
            assertEquals(1L, result.getTotal());
            assertEquals(1, result.getRows().size());
            assertEquals(testExpenseRecord.getId(), result.getRows().get(0).getId());
        }
        
        // Verify interactions
        verify(expenseRecordMapper).list(maxAmount, minAmount, category, dateTime);
    }

    @Test
    void testAdd() {
        // Act
        expenseRecordService.add(testExpenseRecord);

        // Assert
        verify(expenseRecordMapper).add(testExpenseRecord);
    }

    @Test
    void testDeleteById() {
        // Arrange
        Integer id = 1;

        // Act
        expenseRecordService.deleteById(id);

        // Assert
        verify(expenseRecordMapper).deleteById(id);
    }

    @Test
    void testDeleteByIdWithNullId() {
        // Act & Assert
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            expenseRecordService.deleteById(null);
        });
        
        assertEquals("ID cannot be null", exception.getMessage());
        verify(expenseRecordMapper, never()).deleteById(any());
    }

    @Test
    void testUpdate() {
        // Act
        expenseRecordService.update(testExpenseRecord);

        // Assert
        verify(expenseRecordMapper).updateById(testExpenseRecord);
    }

    @Test
    void testGetById() {
        // Arrange
        Integer id = 1;
        when(expenseRecordMapper.getById(id)).thenReturn(testExpenseRecord);

        // Act
        ExpenseRecord result = expenseRecordService.getById(id);

        // Assert
        assertNotNull(result);
        assertEquals(testExpenseRecord.getId(), result.getId());
        assertEquals(testExpenseRecord.getCategory(), result.getCategory());
        assertEquals(testExpenseRecord.getDescription(), result.getDescription());
        assertEquals(testExpenseRecord.getAmount(), result.getAmount());
        verify(expenseRecordMapper).getById(id);
    }

    @Test
    void testGetByIdNotFound() {
        // Arrange
        Integer id = 999;
        when(expenseRecordMapper.getById(id)).thenReturn(null);

        // Act
        ExpenseRecord result = expenseRecordService.getById(id);

        // Assert
        assertNull(result);
        verify(expenseRecordMapper).getById(id);
    }
}