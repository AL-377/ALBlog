package com.aidan.alblogserver.mapper;

import com.aidan.alblogserver.pojo.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TagMapper extends BaseMapper<Tag> {

    @Insert("insert into t_tag values(#{id},#{name})")
    @Options(useGeneratedKeys=true, keyProperty = "id",keyColumn="id")
    int add(Tag tag);

    @Select("select * from t_tag where name=#{name}")
    Tag findByName(String name);

    @Select("select * from t_tag")
    List<Tag> findAll();

    @Select("select * from t_tag")
    @Results({
            @Result(column = "id",property = "id"),
            @Result(column = "name",property = "name"),
            @Result(column = "id",property = "blogs",javaType = List.class,
                    many=@Many(select = "com.aidan.alblogserver.mapper.BlogTagMapper.findBlogByTag")
            )
    })
    List<Tag> findAllTagBlogs();

    @Select("select * from t_tag")
    @Results({
            @Result(column = "id",property = "id"),
            @Result(column = "name",property = "name"),
            @Result(column = "id",property = "blogs",javaType = List.class,
                    many=@Many(select = "com.aidan.alblogserver.mapper.BlogTagMapper.findPublicBlogByTag")
            )
    })
    List<Tag> findAllTagPublicBlogs();







}
