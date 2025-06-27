package org.example.exception;

import org.example.bean.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler
    public Result handleException(Exception e) {
        log.error("程序发生错误：" ,e);
        return Result.error("程序出错，请重试");
    }

    // 处理唯一索引冲突异常
    @ExceptionHandler(DuplicateKeyException.class)
    public Result handleDuplicateKeyException(DuplicateKeyException de) {
        log.error("唯一索引冲突异常：", de);
        String message = de.getMessage();
        if (message != null) {
            // 尝试匹配常见的唯一索引冲突错误信息格式
            java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("Duplicate entry '([^']+)' for key '([^']+)'").matcher(message);
            if (matcher.find()) {
                String duplicateValue = matcher.group(1);

                return Result.error(duplicateValue + " 已存在~");
            }
        }
        // 若未匹配到特定格式，返回通用错误信息
        return Result.error("存在重复的唯一索引值，请检查输入");
    }


    // 处理外键约束导致无法删除的异常
    @ExceptionHandler(DataIntegrityViolationException.class)
    public Result handleForeignKeyConstraintException(DataIntegrityViolationException e) {
        log.error("数据完整性违规异常：", e);
        String errorMessage = e.getMessage();
        if (errorMessage != null) {
            errorMessage = errorMessage.toLowerCase();
            // 检查是否为外键约束冲突
            if (errorMessage.contains("foreign key") ||
                    (e.getCause() != null && e.getCause().getMessage().toLowerCase().contains("foreign key"))) {
                // 判断是否为添加不存在客户的情况
                if (errorMessage.contains("insert") && errorMessage.contains("customer")) {
                    return Result.error("添加失败，关联的客户不存在~");
                }
                // 判断是否为删除客户时存在关联数据的情况
                if (errorMessage.contains("delete") && errorMessage.contains("customer")) {
                    return Result.error("操作失败，该客户有相关联的数据，无法更改~");
                }
                if (errorMessage.contains("insert") && errorMessage.contains("order_id")) {
                    return Result.error("操作失败，关联的订单不存在~");
                }
                return Result.error("该数据有相关联的记录，暂时无法操作~");
            }
        }
        // 若不是外键约束异常，使用通用错误处理
        return Result.error("程序出错，请重试");
    }
}