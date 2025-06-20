package org.example;

import org.example.bean.ExpenseRecord;
import org.example.bean.PageResult;
import org.example.mapper.ExpenseRecordMapper;
import org.example.service.Impl.ExpenseRecordServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ExpenseRecordServiceImplTest {

    @InjectMocks
    private ExpenseRecordServiceImpl service;

    @Mock
    private ExpenseRecordMapper mapper;

    private ExpenseRecord record;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        record = new ExpenseRecord(1, "Food", "Lunch", 15.0, LocalDate.of(2024, 6, 20));
    }

    // ---------- Test: page() ----------
    @Test
    void page_validRange() {
        when(mapper.list(100, 10, "Food", LocalDate.of(2024, 6, 20)))
                .thenReturn(Collections.singletonList(record));
        PageResult<ExpenseRecord> result = service.page(10, 100, "Food", LocalDate.of(2024, 6, 20), 1, 10);
        assertEquals(1, result.getRows().size());
    }

    @Test
    void page_emptyResult() {
        when(mapper.list(100, 10, "Travel", LocalDate.of(2024, 1, 1)))
                .thenReturn(Collections.emptyList());
        PageResult<ExpenseRecord> result = service.page(10, 100, "Travel", LocalDate.of(2024, 1, 1), 1, 10);
        assertTrue(result.getRows().isEmpty());
    }

    @Test
    void page_boundaryMinMax() {
        when(mapper.list(100, 0, "Food", LocalDate.now()))
                .thenReturn(Arrays.asList(record, record));
        PageResult<ExpenseRecord> result = service.page(0, 0, "Food", LocalDate.now(), 1, 2);
        assertEquals(2, result.getRows().size());
    }

    @Test
    void page_invalidPageSizeZero() {
        assertThrows(ArithmeticException.class, () ->
                service.page(0, 100, "Food", LocalDate.now(), 1, 0)); // simulate failure
    }

    @Test
    void page_nullCategory() {
        when(mapper.list(100, 50, null, LocalDate.now())).thenReturn(List.of());
        PageResult<ExpenseRecord> result = service.page(100, 50, null, LocalDate.now(), 1, 10);
        assertTrue(result.getRows().isEmpty());
    }

    // ---------- Test: add() ----------
    @Test
    void add_validExpense() {
        doNothing().when(mapper).add(record);
        service.add(record);
        verify(mapper, times(1)).add(record);
    }

    @Test
    void add_nullDescription() {
        ExpenseRecord noDesc = new ExpenseRecord(2, "Bills", null, 30.0, LocalDate.now());
        service.add(noDesc);
        verify(mapper).add(noDesc);
    }

    @Test
    void add_negativeAmount() {
        ExpenseRecord neg = new ExpenseRecord(3, "Refund", "Return", -50.0, LocalDate.now());
        service.add(neg);
        verify(mapper).add(neg);
    }

    @Test
    void add_zeroAmount() {
        ExpenseRecord zero = new ExpenseRecord(4, "Promo", "Free", 0.0, LocalDate.now());
        service.add(zero);
        verify(mapper).add(zero);
    }

    @Test
    void add_futureDate() {
        ExpenseRecord future = new ExpenseRecord(5, "Misc", "Planned", 100.0, LocalDate.now().plusDays(1));
        service.add(future);
        verify(mapper).add(future);
    }

    // ---------- Test: deleteById() ----------
    @Test
    void delete_existingId() {
        service.deleteById(1);
        verify(mapper).deleteById(1);
    }

    @Test
    void delete_zeroId() {
        service.deleteById(0);
        verify(mapper).deleteById(0);
    }

    @Test
    void delete_negativeId() {
        service.deleteById(-1);
        verify(mapper).deleteById(-1);
    }

    @Test
    void delete_largeId() {
        service.deleteById(Integer.MAX_VALUE);
        verify(mapper).deleteById(Integer.MAX_VALUE);
    }

    @Test
    void delete_nullId() {
        assertThrows(NullPointerException.class, () -> service.deleteById(null));
    }

    // ---------- Test: update() ----------
    @Test
    void update_existingRecord() {
        service.update(record);
        verify(mapper).updateById(record);
    }

    @Test
    void update_nullCategory() {
        ExpenseRecord nullCat = new ExpenseRecord(1, null, "Desc", 20.0, LocalDate.now());
        service.update(nullCat);
        verify(mapper).updateById(nullCat);
    }

    @Test
    void update_largeAmount() {
        record.setAmount(9999999.99);
        service.update(record);
        verify(mapper).updateById(record);
    }

    @Test
    void update_nullId() {
        ExpenseRecord nullId = new ExpenseRecord(null, "Food", "Something", 10.0, LocalDate.now());
        service.update(nullId);
        verify(mapper).updateById(nullId);
    }

    @Test
    void update_emptyDescription() {
        record.setDescription("");
        service.update(record);
        verify(mapper).updateById(record);
    }

    // ---------- Test: getById() ----------
    @Test
    void get_existingId() {
        when(mapper.getById(1)).thenReturn(record);
        ExpenseRecord result = service.getById(1);
        assertEquals(record, result);
    }

    @Test
    void get_nonexistentId() {
        when(mapper.getById(999)).thenReturn(null);
        assertNull(service.getById(999));
    }

    @Test
    void get_zeroId() {
        when(mapper.getById(0)).thenReturn(null);
        assertNull(service.getById(0));
    }

    @Test
    void get_negativeId() {
        when(mapper.getById(-1)).thenReturn(null);
        assertNull(service.getById(-1));
    }

    @Test
    void get_largeId() {
        when(mapper.getById(Integer.MAX_VALUE)).thenReturn(null);
        assertNull(service.getById(Integer.MAX_VALUE));
    }
}
