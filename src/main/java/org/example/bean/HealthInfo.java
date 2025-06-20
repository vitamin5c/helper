package org.example.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HealthInfo {
    private Integer id;
    // 健康信息描述
    private String description;
    // 记录日期
    private String recordDate;

    private Integer status;
}