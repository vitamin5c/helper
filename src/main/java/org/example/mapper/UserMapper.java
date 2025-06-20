package org.example.mapper;

import org.apache.ibatis.annotations.*;
import org.example.bean.User;

import java.util.List;

@Mapper//会自动创建一个实现类对象，并自动将对象存入IOC容器
public interface UserMapper {
    @Select("select * from user")
    public List<User> findAll();

    @Delete("delete from user where id = #{id}")
    public Integer deleteById(Integer id);

    @Insert("insert into user(id, username, password, name, age) values (#{id}, #{username}, #{password}, #{name}, #{age});")
    public Integer insert(User user);

    @Update("update user set username = #{username},password = #{password},name = #{name},age = #{age} where id = #{id}")
    public Integer update(User user);

    @Select("select * from user where username = #{username} and password = #{password}")
    public User findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
}
