# Helper System Database Schema

This document describes the MySQL database schema for the Helper Management System.

## Database Information

- **Database Name**: `db01`
- **Character Set**: `utf8mb4`
- **Collation**: `utf8mb4_unicode_ci`
- **Engine**: InnoDB

## Tables Overview

### 1. calorie_record (卡路里记录表)
Stores calorie and nutrition tracking records.

| Field | Type | Description |
|-------|------|-------------|
| id | INT (PK, AUTO_INCREMENT) | Primary key |
| food_name | VARCHAR(100) | Food name |
| calories | DECIMAL(8,2) | Calorie amount |
| serving_size | DECIMAL(8,2) | Serving size in grams |
| meal_type | VARCHAR(20) | Meal type (早餐/午餐/晚餐/零食) |
| description | TEXT | Description |
| record_date | DATE | Record date |
| created_at | TIMESTAMP | Creation timestamp |
| updated_at | TIMESTAMP | Update timestamp |

**Indexes:**
- `idx_food_name` on food_name
- `idx_meal_type` on meal_type
- `idx_record_date` on record_date
- `idx_calorie_meal_date` on (meal_type, record_date)

### 2. expense_record (收支记录表)
Stores expense and income records.

| Field | Type | Description |
|-------|------|-------------|
| id | INT (PK, AUTO_INCREMENT) | Primary key |
| category | VARCHAR(50) | Expense/Income category |
| description | TEXT | Description |
| amount | DECIMAL(12,2) | Amount (negative for expenses, positive for income) |
| create_date | DATE | Record date |
| created_at | TIMESTAMP | Creation timestamp |
| updated_at | TIMESTAMP | Update timestamp |

**Indexes:**
- `idx_category` on category
- `idx_create_date` on create_date
- `idx_amount` on amount
- `idx_expense_category_date` on (category, create_date)

### 3. life_record (生活记录表)
Stores personal life records and events.

| Field | Type | Description |
|-------|------|-------------|
| id | BIGINT (PK, AUTO_INCREMENT) | Primary key |
| title | VARCHAR(200) | Record title |
| content | TEXT | Record content |
| create_time | DATETIME | Creation time |
| updated_at | TIMESTAMP | Update timestamp |

**Indexes:**
- `idx_title` on title
- `idx_create_time` on create_time
- `idx_content` FULLTEXT on content

### 4. health_info (健康信息表)
Stores health information and status.

| Field | Type | Description |
|-------|------|-------------|
| id | INT (PK, AUTO_INCREMENT) | Primary key |
| description | TEXT | Health information description |
| record_date | VARCHAR(50) | Record date |
| status | INT | Status (1:正常, 2:异常, 3:需关注) |
| created_at | TIMESTAMP | Creation timestamp |
| updated_at | TIMESTAMP | Update timestamp |

**Indexes:**
- `idx_record_date` on record_date
- `idx_status` on status
- `idx_description` FULLTEXT on description
- `idx_health_status_date` on (status, record_date)

## Views

### 1. daily_calorie_summary
Provides daily calorie intake summary with breakdown by meal type.

### 2. monthly_expense_summary
Provides monthly expense summary grouped by category.

### 3. health_status_overview
Provides health status overview with counts and latest records.

## Sample Data

The schema includes sample data for all tables:
- 5 calorie records with various foods and meal types
- 5 expense records including income and various expense categories
- 4 life records with different types of personal entries
- 5 health info records with different status levels

## Setup Instructions

1. **Create Database Schema:**
   ```bash
   mysql -u root -p < database_schema.sql
   ```

2. **Verify Installation:**
   ```sql
   USE db01;
   SHOW TABLES;
   SELECT COUNT(*) FROM calorie_record;
   SELECT COUNT(*) FROM expense_record;
   SELECT COUNT(*) FROM life_record;
   SELECT COUNT(*) FROM health_info;
   ```

3. **View Sample Data:**
   ```sql
   SELECT * FROM daily_calorie_summary;
   SELECT * FROM monthly_expense_summary;
   SELECT * FROM health_status_overview;
   ```

## Application Configuration

Ensure your `application.yml` is configured correctly:

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/db01
    username: root
    password: kalenitid618
```

## Performance Considerations

1. **Indexes**: All frequently queried columns have appropriate indexes
2. **Full-text Search**: Available on content and description fields
3. **Composite Indexes**: Created for common query patterns
4. **Views**: Pre-computed aggregations for analytics

## Data Types Mapping

| Java Type | MySQL Type | Notes |
|-----------|------------|-------|
| Integer | INT | Auto-increment for IDs |
| Long | BIGINT | For large ID ranges |
| String | VARCHAR/TEXT | VARCHAR for limited length, TEXT for long content |
| Double | DECIMAL(8,2) or DECIMAL(12,2) | Precise decimal calculations |
| LocalDate | DATE | Date only |
| LocalDateTime | DATETIME | Date and time |

## Maintenance

1. **Regular Backups**: Schedule regular database backups
2. **Index Optimization**: Monitor query performance and optimize indexes as needed
3. **Data Archival**: Consider archiving old records for performance
4. **Statistics Update**: Keep table statistics updated for optimal query planning

## Security

1. **User Privileges**: Create specific database users with limited privileges
2. **Connection Security**: Use SSL connections in production
3. **Data Validation**: Ensure proper input validation in the application layer
4. **Audit Logging**: Consider enabling MySQL audit logging for sensitive operations