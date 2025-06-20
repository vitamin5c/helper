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

    @RequestMapping("/add")
    public Result add(@RequestBody LifeRecord lifeRecord) {
        lifeRecordService.add(lifeRecord);
        return Result.success();
    }

    @RequestMapping("/delete")
    public Result delete(Integer id) {
        lifeRecordService.deleteById(id);
        return Result.success();
    }

    @RequestMapping("/update")
    public Result update(@RequestBody LifeRecord lifeRecord) {
        lifeRecordService.update(lifeRecord);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        LifeRecord lifeRecord = lifeRecordService.getById(id);
        return Result.success(lifeRecord);
    }
}
