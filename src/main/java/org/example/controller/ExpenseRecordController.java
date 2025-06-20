package org.example.controller;

import org.example.bean.ExpenseRecord;
import org.example.bean.PageResult;
import org.example.bean.Result;
import org.example.service.ExpenseRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/expense")
public class ExpenseRecordController {

    @Autowired
    private ExpenseRecordService expenseRecordService;

    /**
     * 分页查询收支记录
     * @param maxAmount  最大金额
     * @param minAmount 最小金额
     * @param category  收支类别
     * @param dateTime  收支日期
     * @return 分页结果
     */
    @GetMapping("/list")
    public Result list(
            @RequestParam(required = false) Integer maxAmount,
            @RequestParam(required = false) Integer minAmount,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) LocalDate dateTime,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        PageResult<ExpenseRecord> pageResult = expenseRecordService.page(maxAmount, minAmount, category, dateTime, page, pageSize);
        return Result.success(pageResult);
    }

    /**
     * 添加收支记录
     * @param expenseRecord 收支记录实体
     * @return 操作结果
     */
    @PostMapping
    public Result add(@RequestBody ExpenseRecord expenseRecord) {
        expenseRecordService.add(expenseRecord);
        return Result.success();
    }

    /**
     * 根据 ID 删除收支记录
     * @param id 记录 ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        expenseRecordService.deleteById(id);
        return Result.success();
    }

    /**
     * 更新收支记录
     * @param expenseRecord 收支记录实体
     * @return 操作结果
     */
    @PutMapping
    public Result update(@RequestBody ExpenseRecord expenseRecord) {
        expenseRecordService.update(expenseRecord);
        return Result.success();
    }

    /**
     * 根据 ID 查询收支记录
     * @param id 记录 ID
     * @return 收支记录实体
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        ExpenseRecord expenseRecord = expenseRecordService.getById(id);
        return Result.success(expenseRecord);
    }
}
