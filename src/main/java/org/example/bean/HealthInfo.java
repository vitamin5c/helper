package org.example.bean;

public class HealthInfo {
    private Integer id;
    // 健康信息描述
    private String description;
    // 记录日期
    private String recordDate;

    private Integer status;

    public HealthInfo() {}

    public HealthInfo(Integer id, String description, String recordDate, Integer status) {
        this.id = id;
        this.description = description;
        this.recordDate = recordDate;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}