package org.example;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.example.bean.LifeRecord;
import org.example.bean.PageResult;
import org.example.mapper.LifeRecordMapper;
import org.example.service.Impl.LifeRecordServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LifeRecordServiceImplTest {

    @Mock
    private LifeRecordMapper lifeRecordMapper;

    @InjectMocks
    private LifeRecordServiceImpl lifeRecordService;

    private LifeRecord testLifeRecord;

    @BeforeEach
    void setUp() {
        testLifeRecord = new LifeRecord();
        testLifeRecord.setId(1L);
        testLifeRecord.setTitle("Daily Reflection");
        testLifeRecord.setContent("Today was a productive day. Completed all planned tasks.");
        testLifeRecord.setCreateTime(LocalDateTime.now());
    }

    @Test
    void testPage() {
        // Arrange
        String title = "Daily";
        Integer page = 1;
        Integer pageSize = 10;

        List<LifeRecord> mockList = Arrays.asList(testLifeRecord);
        Page<LifeRecord> mockPage = new Page<>();
        mockPage.addAll(mockList);
        mockPage.setTotal(1L);

        try (MockedStatic<PageHelper> pageHelperMock = mockStatic(PageHelper.class)) {
            when(lifeRecordMapper.list(title)).thenReturn(mockPage);

            // Act
            PageResult<LifeRecord> result = lifeRecordService.page(title, page, pageSize);

            // Assert
            assertNotNull(result);
            assertEquals(1L, result.getTotal());
            assertEquals(1, result.getRows().size());
            assertEquals(testLifeRecord.getId(), result.getRows().get(0).getId());
            assertEquals(testLifeRecord.getTitle(), result.getRows().get(0).getTitle());
            assertEquals(testLifeRecord.getContent(), result.getRows().get(0).getContent());
        }
        verify(lifeRecordMapper).list(title);
    }

    @Test
    void testPageWithNullTitle() {
        // Arrange
        String title = null;
        Integer page = 1;
        Integer pageSize = 5;

        List<LifeRecord> mockList = Arrays.asList(testLifeRecord);
        Page<LifeRecord> mockPage = new Page<>();
        mockPage.addAll(mockList);
        mockPage.setTotal(1L);

        try (MockedStatic<PageHelper> pageHelperMock = mockStatic(PageHelper.class)) {
            when(lifeRecordMapper.list(title)).thenReturn(mockPage);

            // Act
            PageResult<LifeRecord> result = lifeRecordService.page(title, page, pageSize);

            // Assert
            assertNotNull(result);
            assertEquals(1L, result.getTotal());
            assertEquals(1, result.getRows().size());
        }
        verify(lifeRecordMapper).list(title);
    }

    @Test
    void testPageEmptyResult() {
        // Arrange
        String title = "NonExistent";
        Integer page = 1;
        Integer pageSize = 10;

        Page<LifeRecord> mockPage = new Page<>();
        mockPage.setTotal(0L);

        try (MockedStatic<PageHelper> pageHelperMock = mockStatic(PageHelper.class)) {
            when(lifeRecordMapper.list(title)).thenReturn(mockPage);

            // Act
            PageResult<LifeRecord> result = lifeRecordService.page(title, page, pageSize);

            // Assert
            assertNotNull(result);
            assertEquals(0L, result.getTotal());
            assertEquals(0, result.getRows().size());
        }
        verify(lifeRecordMapper).list(title);
    }

    @Test
    void testAdd() {
        // Act
        lifeRecordService.add(testLifeRecord);

        // Assert
        verify(lifeRecordMapper).insert(testLifeRecord);
    }

    @Test
    void testDeleteById() {
        // Arrange
        Integer id = 1;

        // Act
        lifeRecordService.deleteById(id);

        // Assert
        verify(lifeRecordMapper).deleteById(id);
    }

    @Test
    void testUpdate() {
        // Act
        lifeRecordService.update(testLifeRecord);

        // Assert
        verify(lifeRecordMapper).update(testLifeRecord);
    }

    @Test
    void testGetById() {
        // Arrange
        Integer id = 1;
        when(lifeRecordMapper.selectById(id)).thenReturn(testLifeRecord);

        // Act
        LifeRecord result = lifeRecordService.getById(id);

        // Assert
        assertNotNull(result);
        assertEquals(testLifeRecord.getId(), result.getId());
        assertEquals(testLifeRecord.getTitle(), result.getTitle());
        assertEquals(testLifeRecord.getContent(), result.getContent());
        assertEquals(testLifeRecord.getCreateTime(), result.getCreateTime());
        verify(lifeRecordMapper).selectById(id);
    }

    @Test
    void testGetByIdNotFound() {
        // Arrange
        Integer id = 999;
        when(lifeRecordMapper.selectById(id)).thenReturn(null);

        // Act
        LifeRecord result = lifeRecordService.getById(id);

        // Assert
        assertNull(result);
        verify(lifeRecordMapper).selectById(id);
    }

    @Test
    void testAddWithNullLifeRecord() {
        // Act
        lifeRecordService.add(null);

        // Assert
        verify(lifeRecordMapper).insert(null);
    }

    @Test
    void testUpdateWithNullLifeRecord() {
        // Act
        lifeRecordService.update(null);

        // Assert
        verify(lifeRecordMapper).update(null);
    }

    @Test
    void testDeleteByIdWithNullId() {
        // Act
        lifeRecordService.deleteById(null);

        // Assert
        verify(lifeRecordMapper).deleteById(null);
    }

    @Test
    void testGetByIdWithNullId() {
        // Arrange
        when(lifeRecordMapper.selectById(null)).thenReturn(null);

        // Act
        LifeRecord result = lifeRecordService.getById(null);

        // Assert
        assertNull(result);
        verify(lifeRecordMapper).selectById(null);
    }
}