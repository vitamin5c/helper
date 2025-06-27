package org.example.bean;

import java.time.LocalDateTime;

public class LifeRecord {
    private Long id; // 记录 ID
    private String title; // 记录标题
    private String content; // 记录内容
    private LocalDateTime createTime; // 记录创建时间
    private LocalDateTime updateTime; // 记录更新时间

    public LifeRecord() {}

    public LifeRecord(Long id, String title, String content, LocalDateTime createTime) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}