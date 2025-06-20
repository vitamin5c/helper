package org.example.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LifeRecord {
    private Long id; // 记录 ID
    private String title; // 记录标题
    private String content; // 记录内容
    private LocalDateTime createTime; // 记录创建时间
}