package org.example.mapper;

import org.apache.ibatis.annotations.*;
import org.example.bean.LifeRecord;

import java.util.List;

@Mapper
public interface LifeRecordMapper {

    /**
     * 分页查询
     * @param title 标题
     * @return 分页结果
     */
    @Select("<script>" +
            "SELECT id, title, content, create_time, updated_at as updateTime FROM life_record " +
            "<where>" +
            "<if test='title != null and title != \"\"'>" +
            "title LIKE CONCAT('%', #{title}, '%')" +
            "</if>" +
            "</where>" +
            "ORDER BY create_time DESC" +
            "</script>")
    List<LifeRecord> list(String title);

    /**
     * 新增
     * @param healthInfo 健康信息
     */
    @Insert("INSERT INTO life_record (title, content, create_time) VALUES (#{title}, #{content}, #{createTime})")
    void insert(LifeRecord healthInfo);

    /**
     * 根据 ID 删除
     * @param id ID
     */
    @Delete("DELETE FROM life_record WHERE id = #{id}")
    void deleteById(Integer id);

    /**
     * 根据 ID 查询
     *
     * @param id ID
     * @return 健康信息
     */
    @Select("SELECT id, title, content, create_time, updated_at as updateTime FROM life_record WHERE id = #{id}")
    LifeRecord selectById(Integer id);

    /**
     * 更新
     * @param lifeRecord 记录生活
     */
    @Update("UPDATE life_record SET title = #{title}, content = #{content}, updated_at = #{updateTime} WHERE id = #{id}")
    void update(LifeRecord lifeRecord);
}
