package org.example;

import org.example.mapper.UserMapper;
import org.example.bean.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SpringbootMybatisDemoApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testFindAll(){
        List<User> userList = userMapper.findAll();
        userList.forEach(System.out::println);
    }

    @Test
    public void testDeleteById() {
        Integer i = userMapper.deleteById(6);
        System.out.println("删除操作影响的记录数： " + i);
    }

    @Test
    public void testInsert() {
        User user = new User(6,"zhouyu", "12345678", "周瑜", 20);
        Integer i = userMapper.insert(user);
        System.out.println("增加操作影响的记录数： " + i);
    }

    @Test
    public void testUpdate() {
        User user = new User(1,"zhouyu", "12345678", "周瑜", 20);
        Integer i = userMapper.update(user);
        System.out.println("修改操作影响的记录数： " + i);
    }

    @Test
    public void testFindByUsernameAndPassword(){
        User user = userMapper.findByUsernameAndPassword("zhouyu", "12345678");
        System.out.println(user);
    }
}
