package org.example.service;

import org.example.bean.LifeRecord;
import org.example.bean.PageResult;
import org.springframework.stereotype.Service;

@Service
public interface LifeRecordService {
    PageResult<LifeRecord> page(String title, Integer page, Integer pageSize) ;

    void add(LifeRecord healthInfo);

    void deleteById(Integer id);

    void update(LifeRecord healthInfo);

    LifeRecord getById(Integer id);
}
