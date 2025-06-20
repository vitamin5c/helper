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
    @Select("SELECT * FROM life_record WHERE title LIKE CONCAT('%', #{title}, '%')")
    List<LifeRecord> list(String title);

    /**
     * 新增
     * @param healthInfo 健康信息
     */
    @Insert("INSERT INTO life_record (title, content, create_time) VALUES (#{title}, #{content}, #{createTime}, #{updateTime})")
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
    @Select("SELECT * FROM life_record WHERE id = #{id}")
    LifeRecord selectById(Integer id);

    /**
     * 更新
     * @param lifeRecord 记录生活
     */
    @Update("UPDATE life_record SET title = #{title}, content = #{content}, update_time = #{updateTime} WHERE id = #{id}")
    void update(LifeRecord lifeRecord);
}
