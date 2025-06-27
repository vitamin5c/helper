package org.example.bean;

import java.time.LocalDate;

public class ExpenseRecord {
    private Integer id;         // 主键
    private String category;    //收支类别
    private String description; //收支描述
    private Double amount;      //金额
    private LocalDate createDate;  //时间

    public ExpenseRecord() {}

    public ExpenseRecord(Integer id, String category, String description, Double amount, LocalDate createDate) {
        this.id = id;
        this.category = category;
        this.description = description;
        this.amount = amount;
        this.createDate = createDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }
}
