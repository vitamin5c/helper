package org.example.mapper;

import org.apache.ibatis.annotations.*;
import org.example.bean.ExpenseRecord;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ExpenseRecordMapper {

    /**
     * 分页查询收支记录
     * @param maxAmount
     * @param minAmount
     * @param category
     * @param dateTime
     * @return
     */

    List<ExpenseRecord> list(Integer maxAmount, Integer minAmount, String category, LocalDate dateTime);

    /**
     * 添加收支记录
     * @param expenseRecord
     */
    @Insert("INSERT INTO expense_record (amount, category, create_date, description) VALUES (#{amount}, #{category}, #{createDate}, #{description})")
    void add(ExpenseRecord expenseRecord);

    /**
     * 根据 ID 删除收支记录
     * @param id
     */
    @Delete("DELETE FROM expense_record WHERE id = #{id}")
    void deleteById(Integer id);

    /**
     * 根据 ID 更新收支记录
     * @param expenseRecord
     */
    @Update("UPDATE expense_record SET description = #{description}, amount = #{amount}, category = #{category} WHERE id = #{id}")
    void updateById(ExpenseRecord expenseRecord);

    /**
     * 根据 ID 查询收支记录
     * @param id
     * @return
     */
    @Select("SELECT * FROM expense_record WHERE id = #{id}")
    ExpenseRecord getById(Integer id);

}