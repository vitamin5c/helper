package org.example.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseRecord {
    private Integer id;         // 主键
    private String category;    //收支类别
    private String description; //收支描述
    private Double amount;      //金额
    private LocalDate createDate;  //时间
}
