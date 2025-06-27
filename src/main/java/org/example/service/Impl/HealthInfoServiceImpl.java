package org.example.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.example.bean.HealthInfo;
import org.example.bean.PageResult;
import org.example.mapper.HealthInfoMapper;
import org.example.service.HealthInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HealthInfoServiceImpl implements HealthInfoService {

    @Autowired
    private HealthInfoMapper healthInfoMapper;

    @Override
    public void add(HealthInfo healthInfo) {
        healthInfoMapper.insert(healthInfo);
    }

    @Override
    public void deleteById(Integer id) {
        healthInfoMapper.deleteById(id);
    }

    @Override
    public void update(HealthInfo healthInfo) {
        healthInfoMapper.update(healthInfo);
    }

    @Override
    public HealthInfo getById(Integer id) {
        return healthInfoMapper.selectById(id);
    }

    @Override
    public PageResult<HealthInfo> page(String recordDate, Integer status, Integer page, Integer pageSize) {
        // 设置分页参数
        PageHelper.startPage(page, pageSize);

        // 执行查询
        List<HealthInfo>  healthInfosList = healthInfoMapper.list(recordDate, status);
        Page<HealthInfo> p = (Page<HealthInfo>) healthInfosList;

        //3. 封装结果
        PageResult<HealthInfo> pageResult = new PageResult<>();
        pageResult.setTotal(p.getTotal());
        pageResult.setRows(p.getResult());
        return pageResult;
    }
}