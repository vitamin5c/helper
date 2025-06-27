package org.example;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.example.bean.HealthInfo;
import org.example.bean.PageResult;
import org.example.mapper.HealthInfoMapper;
import org.example.service.Impl.HealthInfoServiceImpl;
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
class HealthInfoServiceImplTest {

    @Mock
    private HealthInfoMapper healthInfoMapper;

    @InjectMocks
    private HealthInfoServiceImpl healthInfoService;

    private HealthInfo testHealthInfo;

    @BeforeEach
    void setUp() {
        testHealthInfo = new HealthInfo();
        testHealthInfo.setId(1);
        testHealthInfo.setDescription("Regular checkup completed");
        testHealthInfo.setRecordDate("2024-01-15");
        testHealthInfo.setStatus(1);
    }

    @Test
    void testAdd() {
        // Act
        healthInfoService.add(testHealthInfo);

        // Assert
        verify(healthInfoMapper).insert(testHealthInfo);
    }

    @Test
    void testDeleteById() {
        // Arrange
        Integer id = 1;

        // Act
        healthInfoService.deleteById(id);

        // Assert
        verify(healthInfoMapper).deleteById(id);
    }

    @Test
    void testUpdate() {
        // Act
        healthInfoService.update(testHealthInfo);

        // Assert
        verify(healthInfoMapper).update(testHealthInfo);
    }

    @Test
    void testGetById() {
        // Arrange
        Integer id = 1;
        when(healthInfoMapper.selectById(id)).thenReturn(testHealthInfo);

        // Act
        HealthInfo result = healthInfoService.getById(id);

        // Assert
        assertNotNull(result);
        assertEquals(testHealthInfo.getId(), result.getId());
        assertEquals(testHealthInfo.getDescription(), result.getDescription());
        assertEquals(testHealthInfo.getRecordDate(), result.getRecordDate());
        assertEquals(testHealthInfo.getStatus(), result.getStatus());
        verify(healthInfoMapper).selectById(id);
    }

    @Test
    void testGetByIdNotFound() {
        // Arrange
        Integer id = 999;
        when(healthInfoMapper.selectById(id)).thenReturn(null);

        // Act
        HealthInfo result = healthInfoService.getById(id);

        // Assert
        assertNull(result);
        verify(healthInfoMapper).selectById(id);
    }

    @Test
    void testPage() {
        // Arrange
        LocalDateTime dateTime = LocalDateTime.now();
        Integer status = 1;
        Integer page = 1;
        Integer pageSize = 10;

        List<HealthInfo> mockList = Arrays.asList(testHealthInfo);
        Page<HealthInfo> mockPage = new Page<>();
        mockPage.addAll(mockList);
        mockPage.setTotal(1L);

        try (MockedStatic<PageHelper> pageHelperMock = mockStatic(PageHelper.class)) {
            when(healthInfoMapper.list(dateTime, status)).thenReturn(mockPage);

            // Act
            PageResult<HealthInfo> result = healthInfoService.page(dateTime, status, page, pageSize);

            // Assert
            assertNotNull(result);
            assertEquals(1L, result.getTotal());
            assertEquals(1, result.getRows().size());
            assertEquals(testHealthInfo.getId(), result.getRows().get(0).getId());
            assertEquals(testHealthInfo.getDescription(), result.getRows().get(0).getDescription());
        }
        verify(healthInfoMapper).list(dateTime, status);
    }

    @Test
    void testPageWithNullParameters() {
        // Arrange
        LocalDateTime dateTime = null;
        Integer status = null;
        Integer page = 1;
        Integer pageSize = 5;

        List<HealthInfo> mockList = Arrays.asList(testHealthInfo);
        Page<HealthInfo> mockPage = new Page<>();
        mockPage.addAll(mockList);
        mockPage.setTotal(1L);

        try (MockedStatic<PageHelper> pageHelperMock = mockStatic(PageHelper.class)) {
            when(healthInfoMapper.list(dateTime, status)).thenReturn(mockPage);

            // Act
            PageResult<HealthInfo> result = healthInfoService.page(dateTime, status, page, pageSize);

            // Assert
            assertNotNull(result);
            assertEquals(1L, result.getTotal());
            assertEquals(1, result.getRows().size());
        }
        verify(healthInfoMapper).list(dateTime, status);
    }

    @Test
    void testPageEmptyResult() {
        // Arrange
        LocalDateTime dateTime = LocalDateTime.now();
        Integer status = 1;
        Integer page = 1;
        Integer pageSize = 10;

        Page<HealthInfo> mockPage = new Page<>();
        mockPage.setTotal(0L);

        try (MockedStatic<PageHelper> pageHelperMock = mockStatic(PageHelper.class)) {
            when(healthInfoMapper.list(dateTime, status)).thenReturn(mockPage);

            // Act
            PageResult<HealthInfo> result = healthInfoService.page(dateTime, status, page, pageSize);

            // Assert
            assertNotNull(result);
            assertEquals(0L, result.getTotal());
            assertEquals(0, result.getRows().size());
        }
        verify(healthInfoMapper).list(dateTime, status);
    }
}