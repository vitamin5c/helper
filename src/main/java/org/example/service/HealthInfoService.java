package org.example.service;

import org.example.bean.HealthInfo;
import org.example.bean.PageResult;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public interface HealthInfoService {
    PageResult<HealthInfo> page(LocalDateTime dateTime, Integer status, Integer page, Integer pageSize);

    void add(HealthInfo healthInfo);

    void deleteById(Integer id);

    void update(HealthInfo healthInfo);

    HealthInfo getById(Integer id);
}
