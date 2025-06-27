package org.example.controller;

import org.example.bean.*;
import org.example.service.LifeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/life")
public class LifeRecordController {

    @Autowired
    private LifeRecordService lifeRecordService;

    @RequestMapping("/list")
    public Result list(@RequestParam(required = false) String title ,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer pageSize) {
        PageResult<LifeRecord> pageResult = lifeRecordService.page(title, page, pageSize);
        return Result.success(pageResult);
    }

    @PostMapping
    public Result add(@RequestBody LifeRecord lifeRecord) {
        lifeRecord.setCreateTime(LocalDateTime.now());
        lifeRecordService.add(lifeRecord);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        lifeRecordService.deleteById(id);
        return Result.success();
    }

    @PutMapping
    public Result update(@RequestBody LifeRecord lifeRecord) {
        lifeRecord.setUpdateTime(LocalDateTime.now());
        lifeRecordService.update(lifeRecord);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        LifeRecord lifeRecord = lifeRecordService.getById(id);
        return Result.success(lifeRecord);
    }
}
