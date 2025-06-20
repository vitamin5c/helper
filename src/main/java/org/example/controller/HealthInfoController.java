package org.example.controller;

import org.example.bean.HealthInfo;
import org.example.bean.PageResult;
import org.example.bean.Result;
import org.example.service.HealthInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/health")
public class HealthInfoController {

    @Autowired
    private HealthInfoService healthInfoService;

    /**
     * 分页查询健康信息记录
     * @param dateTime 搜索关键词
     * @param page 页码
     * @param pageSize 每页记录数
     * @return 分页结果
     */
    @GetMapping("/list")
    public Result list(
            @RequestParam(required = false) LocalDateTime dateTime,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        PageResult<HealthInfo> pageResult = healthInfoService.page(dateTime, page, pageSize);
        return Result.success(pageResult);
    }

    /**
     * 添加健康信息记录
     * @param healthInfo 健康信息实体
     * @return 操作结果
     */
    @PostMapping
    public Result add(@RequestBody HealthInfo healthInfo) {
        healthInfoService.add(healthInfo);
        return Result.success();
    }

    /**
     * 根据 ID 删除健康信息记录
     * @param id 记录 ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        healthInfoService.deleteById(id);
        return Result.success();
    }

    /**
     * 更新健康信息记录
     * @param healthInfo 健康信息实体
     * @return 操作结果
     */
    @PutMapping
    public Result update(@RequestBody HealthInfo healthInfo) {
        healthInfoService.update(healthInfo);
        return Result.success();
    }

    /**
     * 根据 ID 查询健康信息记录
     * @param id 记录 ID
     * @return 健康信息实体
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        HealthInfo healthInfo = healthInfoService.getById(id);
        return Result.success(healthInfo);
    }
}