package org.example.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.example.bean.LifeRecord;
import org.example.bean.PageResult;
import org.example.mapper.LifeRecordMapper;
import org.example.service.LifeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LifeRecordServiceImpl implements LifeRecordService {

    @Autowired
    private LifeRecordMapper lifeRecordMapper;

    @Override
    public PageResult<LifeRecord> page(String title, Integer page, Integer pageSize) {
        //1. 设置分页参数
        PageHelper.startPage(page, pageSize);

        //2. 执行查询
        List<LifeRecord> lifeRecordsList = lifeRecordMapper.list(title);
        Page<LifeRecord> p = (Page<LifeRecord>) lifeRecordsList;

        //3. 封装结果
        PageResult<LifeRecord> pageResult = new PageResult<>();
        pageResult.setTotal(p.getTotal());
        pageResult.setRows(p.getResult());
        return pageResult;
    }

    @Override
    public void add(LifeRecord lifeRecord) {
        lifeRecordMapper.insert(lifeRecord);
    }

    @Override
    public void deleteById(Integer id) {
        lifeRecordMapper.deleteById(id);
    }

    @Override
    public void update(LifeRecord lifeRecord) {
        lifeRecordMapper.update(lifeRecord);
    }

    @Override
    public LifeRecord getById(Integer id) {
        return lifeRecordMapper.selectById(id);
    }


}
