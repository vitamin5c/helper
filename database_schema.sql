-- Helper System Database Schema
-- MySQL Database: db01
-- Created for the Helper Management System

-- Create database if not exists
CREATE DATABASE IF NOT EXISTS db01 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE db01;

-- ========================================
-- Table: calorie_record
-- Description: Stores calorie and nutrition records
-- ========================================
CREATE TABLE IF NOT EXISTS calorie_record (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    food_name VARCHAR(100) NOT NULL COMMENT '食物名称',
    calories DECIMAL(8,2) NOT NULL COMMENT '卡路里数量',
    serving_size DECIMAL(8,2) COMMENT '份量大小(克)',
    meal_type VARCHAR(20) NOT NULL COMMENT '餐次类型(早餐/午餐/晚餐/零食)',
    description TEXT COMMENT '描述',
    record_date DATE NOT NULL COMMENT '记录日期',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_food_name (food_name),
    INDEX idx_meal_type (meal_type),
    INDEX idx_record_date (record_date),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='卡路里记录表';

-- ========================================
-- Table: expense_record
-- Description: Stores expense and income records
-- ========================================
CREATE TABLE IF NOT EXISTS expense_record (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    category VARCHAR(50) NOT NULL COMMENT '收支类别',
    description TEXT COMMENT '收支描述',
    amount DECIMAL(12,2) NOT NULL COMMENT '金额',
    create_date DATE NOT NULL COMMENT '记录日期',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_category (category),
    INDEX idx_create_date (create_date),
    INDEX idx_amount (amount),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='收支记录表';

-- ========================================
-- Table: life_record
-- Description: Stores personal life records and events
-- ========================================
CREATE TABLE IF NOT EXISTS life_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '记录ID',
    title VARCHAR(200) NOT NULL COMMENT '记录标题',
    content TEXT COMMENT '记录内容',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_title (title),
    INDEX idx_create_time (create_time),
    FULLTEXT idx_content (content)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='生活记录表';

-- ========================================
-- Table: health_info
-- Description: Stores health information and status
-- ========================================
CREATE TABLE IF NOT EXISTS health_info (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    description TEXT NOT NULL COMMENT '健康信息描述',
    record_date VARCHAR(50) NOT NULL COMMENT '记录日期',
    status INT NOT NULL DEFAULT 1 COMMENT '状态(1:正常, 2:异常, 3:需关注)',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_record_date (record_date),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at),
    FULLTEXT idx_description (description)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='健康信息表';

-- ========================================
-- Insert Sample Data
-- ========================================

-- Sample data for calorie_record
INSERT INTO calorie_record (food_name, calories, serving_size, meal_type, description, record_date) VALUES
('苹果', 95.0, 150.0, '零食', '新鲜红苹果', '2024-01-15'),
('白米饭', 205.0, 150.0, '午餐', '蒸白米饭', '2024-01-15'),
('鸡胸肉', 231.0, 100.0, '晚餐', '烤鸡胸肉', '2024-01-15'),
('燕麦粥', 158.0, 200.0, '早餐', '燕麦片粥', '2024-01-16'),
('香蕉', 105.0, 120.0, '零食', '成熟香蕉', '2024-01-16');

-- Sample data for expense_record
INSERT INTO expense_record (category, description, amount, create_date) VALUES
('餐饮', '午餐费用', -25.50, '2024-01-15'),
('交通', '地铁费用', -5.00, '2024-01-15'),
('工资', '月薪收入', 8000.00, '2024-01-01'),
('购物', '日用品采购', -120.00, '2024-01-14'),
('娱乐', '电影票', -45.00, '2024-01-13');

-- Sample data for life_record
INSERT INTO life_record (title, content, create_time) VALUES
('今日工作总结', '完成了项目的前端开发，解决了几个重要的bug，团队协作很顺利。', '2024-01-15 18:30:00'),
('周末计划', '计划去公园跑步，然后和朋友聚餐，晚上看一部电影放松一下。', '2024-01-14 20:00:00'),
('学习笔记', '今天学习了Vue.js的组件通信，掌握了props和emit的使用方法。', '2024-01-13 21:15:00'),
('健身记录', '今天在健身房锻炼了1小时，主要练习胸肌和手臂，感觉很有成就感。', '2024-01-12 19:45:00');

-- Sample data for health_info
INSERT INTO health_info (description, record_date, status) VALUES
('今日体检结果正常，血压120/80，心率72次/分钟', '2024-01-15', 1),
('轻微感冒症状，已服用感冒药，多休息多喝水', '2024-01-10', 2),
('血糖略高，需要控制饮食，减少糖分摄入', '2024-01-08', 3),
('定期体检，各项指标正常，继续保持健康生活方式', '2024-01-05', 1),
('牙齿检查，发现一颗蛀牙，已预约牙医治疗', '2024-01-03', 2);

-- ========================================
-- Create Views for Analytics
-- ========================================

-- View: Daily calorie summary
CREATE OR REPLACE VIEW daily_calorie_summary AS
SELECT 
    record_date,
    SUM(calories) as total_calories,
    COUNT(*) as food_count,
    SUM(CASE WHEN meal_type = '早餐' THEN calories ELSE 0 END) as breakfast_calories,
    SUM(CASE WHEN meal_type = '午餐' THEN calories ELSE 0 END) as lunch_calories,
    SUM(CASE WHEN meal_type = '晚餐' THEN calories ELSE 0 END) as dinner_calories,
    SUM(CASE WHEN meal_type = '零食' THEN calories ELSE 0 END) as snack_calories
FROM calorie_record
GROUP BY record_date
ORDER BY record_date DESC;

-- View: Monthly expense summary
CREATE OR REPLACE VIEW monthly_expense_summary AS
SELECT 
    DATE_FORMAT(create_date, '%Y-%m') as month,
    category,
    SUM(amount) as total_amount,
    COUNT(*) as transaction_count,
    AVG(amount) as avg_amount
FROM expense_record
GROUP BY DATE_FORMAT(create_date, '%Y-%m'), category
ORDER BY month DESC, category;

-- View: Health status overview
CREATE OR REPLACE VIEW health_status_overview AS
SELECT 
    status,
    CASE 
        WHEN status = 1 THEN '正常'
        WHEN status = 2 THEN '异常'
        WHEN status = 3 THEN '需关注'
        ELSE '未知'
    END as status_name,
    COUNT(*) as record_count,
    MAX(STR_TO_DATE(record_date, '%Y-%m-%d')) as latest_record
FROM health_info
GROUP BY status
ORDER BY status;

-- ========================================
-- Create Indexes for Performance
-- ========================================

-- Additional composite indexes for common queries
CREATE INDEX idx_calorie_meal_date ON calorie_record(meal_type, record_date);
CREATE INDEX idx_expense_category_date ON expense_record(category, create_date);
CREATE INDEX idx_health_status_date ON health_info(status, record_date);

-- ========================================
-- Database Setup Complete
-- ========================================

-- Show table information
SELECT 
    TABLE_NAME as '表名',
    TABLE_COMMENT as '表注释',
    TABLE_ROWS as '估计行数'
FROM INFORMATION_SCHEMA.TABLES 
WHERE TABLE_SCHEMA = 'db01' 
AND TABLE_TYPE = 'BASE TABLE'
ORDER BY TABLE_NAME;

SELECT 'Database schema created successfully!' as Status;