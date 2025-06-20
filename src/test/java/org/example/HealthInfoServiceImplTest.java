package org.example;
//
//import org.example.bean.HealthInfo;
//import org.example.bean.PageResult;
//import org.example.mapper.HealthInfoMapper;
//import org.example.service.Impl.HealthInfoServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//
//import java.time.LocalDateTime;
//import java.util.Collections;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class HealthInfoServiceImplTest {
//
//    @InjectMocks
//    private HealthInfoServiceImpl service;
//
//    @Mock
//    private HealthInfoMapper mapper;
//
//    private HealthInfo sample;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        sample = new HealthInfo(1, "Fever", "2024-06-01", 1);
//    }
//
//    // ---------- add() ----------
//    @Test
//    void add_validHealthInfo() {
//        doNothing().when(mapper).insert(sample);
//        service.add(sample);
//        verify(mapper).insert(sample);
//    }
//
//    @Test
//    void add_emptyDescription() {
//        HealthInfo info = new HealthInfo(2, "", "2024-06-01", 0);
//        service.add(info);
//        verify(mapper).insert(info);
//    }
//
//    @Test
//    void add_nullDate() {
//        HealthInfo info = new HealthInfo(3, "Cough", null, 1);
//        service.add(info);
//        verify(mapper).insert(info);
//    }
//
//    @Test
//    void add_extremeStatus() {
//        HealthInfo info = new HealthInfo(4, "Healthy", "2024-06-01", Integer.MAX_VALUE);
//        service.add(info);
//        verify(mapper).insert(info);
//    }
//
//    @Test
//    void add_negativeStatus() {
//        HealthInfo info = new HealthInfo(5, "Sick", "2024-06-01", -1);
//        service.add(info);
//        verify(mapper).insert(info);
//    }
//
//    // ---------- deleteById() ----------
//    @Test
//    void delete_validId() {
//        service.deleteById(1);
//        verify(mapper).deleteById(1);
//    }
//
//    @Test
//    void delete_zeroId() {
//        service.deleteById(0);
//        verify(mapper).deleteById(0);
//    }
//
//    @Test
//    void delete_negativeId() {
//        service.deleteById(-5);
//        verify(mapper).deleteById(-5);
//    }
//
//    @Test
//    void delete_largeId() {
//        service.deleteById(Integer.MAX_VALUE);
//        verify(mapper).deleteById(Integer.MAX_VALUE);
//    }
//
//    @Test
//    void delete_nullId() {
//        assertThrows(NullPointerException.class, () -> service.deleteById(null));
//    }
//
//    // ---------- update() ----------
//    @Test
//    void update_normalUpdate() {
//        service.update(sample);
//        verify(mapper).update(sample);
//    }
//
//    @Test
//    void update_nullDescription() {
//        HealthInfo info = new HealthInfo(1, null, "2024-06-01", 1);
//        service.update(info);
//        verify(mapper).update(info);
//    }
//
//    @Test
//    void update_nullId() {
//        HealthInfo info = new HealthInfo(null, "Sleep Issue", "2024-06-01", 1);
//        service.update(info);
//        verify(mapper).update(info);
//    }
//
//    @Test
//    void update_longDescription() {
//        String longDesc = "x".repeat(1000);
//        HealthInfo info = new HealthInfo(1, longDesc, "2024-06-01", 1);
//        service.update(info);
//        verify(mapper).update(info);
//    }
//
//    @Test
//    void update_emptyDate() {
//        HealthInfo info = new HealthInfo(1, "OK", "", 0);
//        service.update(info);
//        verify(mapper).update(info);
//    }
//
//    // ---------- getById() ----------
//    @Test
//    void get_existingId() {
//        when(mapper.selectById(1)).thenReturn(sample);
//        assertEquals(sample, service.getById(1));
//    }
//
//    @Test
//    void get_zeroId() {
//        when(mapper.selectById(0)).thenReturn(null);
//        assertNull(service.getById(0));
//    }
//
//    @Test
//    void get_negativeId() {
//        when(mapper.selectById(-3)).thenReturn(null);
//        assertNull(service.getById(-3));
//    }
//
//    @Test
//    void get_largeId() {
//        when(mapper.selectById(Integer.MAX_VALUE)).thenReturn(null);
//        assertNull(service.getById(Integer.MAX_VALUE));
//    }
//
//    @Test
//    void get_nullId() {
//        assertThrows(NullPointerException.class, () -> service.getById(null));
//    }
//
//    // ---------- page() ----------
//    @Test
//    void page_validRequest() {
//        when(mapper.list(any())).thenReturn(List.of(sample));
//        PageResult<HealthInfo> result = service.page(LocalDateTime.now(), 1, 10);
//        assertEquals(1, result.getRows().size());
//    }
//
//    @Test
//    void page_emptyResult() {
//        when(mapper.list(any())).thenReturn(Collections.emptyList());
//        PageResult<HealthInfo> result = service.page(LocalDateTime.now(), 1, 10);
//        assertTrue(result.getRows().isEmpty());
//    }
//
//    @Test
//    void page_invalidPageZero() {
//        assertThrows(ArithmeticException.class, () -> service.page(LocalDateTime.now(), 0, 10));
//    }
//
//    @Test
//    void page_invalidPageSizeZero() {
//        assertThrows(ArithmeticException.class, () -> service.page(LocalDateTime.now(), 1, 0));
//    }
//
//    @Test
//    void page_futureDateTime() {
//        when(mapper.list(any())).thenReturn(List.of(sample));
//        PageResult<HealthInfo> result = service.page(LocalDateTime.now().plusYears(1), 1, 5);
//        assertNotNull(result);
//    }
//}
