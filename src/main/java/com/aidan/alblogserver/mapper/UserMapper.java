package com.aidan.alblogserver.mapper;

import com.aidan.alblogserver.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Insert("insert into t_user values(#{id},#{username},#{password},#{email},#{usertype},#{avatar},#{createtime},#{updatetime})")
    @Options(useGeneratedKeys=true, keyProperty = "id",keyColumn="id")
    int add(User user);
    @Update("update t_user set username=#{username},password=# {password},email=#{email}，updatetime=#{updatetime} where id=#{id}")
    int update(User user);
    @Delete("delete from t_user where id=#{id}")
    int delete(int id);
    @Select("select * from t_user where username=#{username} and password=#{password}")
    User findByNameAndPassword(String username, String password);
    @Select("select * from t_user")
    List<User> getAll();
}

