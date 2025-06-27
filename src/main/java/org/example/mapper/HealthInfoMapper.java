package org.example.mapper;

import org.apache.ibatis.annotations.*;
import org.example.bean.HealthInfo;

import java.util.List;

@Mapper
public interface HealthInfoMapper {

    /**
     * 插入健康信息记录
     * @param healthInfo 健康信息实体
     * @return 插入成功的记录数
     */
    @Insert("INSERT INTO health_info (description, record_date, status) VALUES (#{description}, #{recordDate}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insert(HealthInfo healthInfo);

    /**
     * 根据 ID 删除健康信息记录
     * @param id 记录 ID
     * @return 删除成功的记录数
     */
    @Delete("DELETE FROM health_info WHERE id = #{id}")
    int deleteById(Integer id);

    /**
     * 更新健康信息记录
     * @param healthInfo 健康信息实体
     * @return 更新成功的记录数
     */
    @Update("UPDATE health_info SET description = #{description}, record_date = #{recordDate}, status = #{status} WHERE id = #{id}")
    int update(HealthInfo healthInfo);

    /**
     * 根据 ID 查询健康信息记录
     * @param id 记录 ID
     * @return 健康信息实体
     */
    @Select("SELECT * FROM health_info WHERE id = #{id}")
    HealthInfo selectById(Integer id);

    /**
     * 查询指定日期之后的健康信息记录
     *
     * @param recordDate 记录日期
     * @param status 状态
     * @return 健康信息记录列表
     */
    @Select("<script>" +
            "SELECT * FROM health_info " +
            "<where>" +
            "<if test='recordDate != null and recordDate != \"\"'>" +
            "record_date >= #{recordDate}" +
            "</if>" +
            "<if test='status != null'>" +
            "<if test='recordDate != null and recordDate != \"\"'> AND </if>" +
            "status = #{status}" +
            "</if>" +
            "</where>" +
            "ORDER BY created_at DESC" +
            "</script>")
    List<HealthInfo> list(String recordDate, Integer status);
}