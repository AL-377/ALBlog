package com.aidan.alblogserver.mapper;


import com.aidan.alblogserver.pojo.Type;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TypeMapper extends BaseMapper<Type> {

    @Insert("insert into t_type values(#{id},#{name})")
    @Options(useGeneratedKeys=true, keyProperty = "id",keyColumn="id")
    int add(Type type);

    @Select("select * from t_type where name=#{name}")
    Type findByName(String name);

    @Select("select * from t_type")
    List<Type> findAll();

    @Select("select * from t_type")
    @Results({
            @Result(column = "id",property = "id"),
            @Result(column = "name",property = "name"),
            @Result(column = "id",property = "blogs",javaType = List.class,
                    many=@Many(select = "com.aidan.alblogserver.mapper.BlogMapper.findByTypeId")
            )
    })
    List<Type> findAllTypeBlogs();

    @Select("select * from t_type")
    @Results({
            @Result(column = "id",property = "id"),
            @Result(column = "name",property = "name"),
            @Result(column = "id",property = "blogs",javaType = List.class,
                    many=@Many(select = "com.aidan.alblogserver.mapper.BlogMapper.findPublicByTypeId")
            )
    })
    List<Type> findAllTypePublicBlogs();



}
