package com.aidan.alblogserver.mapper;


import com.aidan.alblogserver.pojo.Blog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BlogMapper extends BaseMapper<Blog> {

    @Insert("insert into t_blog values(#{id},#{title}, #{content}, #{titleBG}, #{flag}, #{views}, #{isReward}, #{isCopyright}, #{isComment}, #{isPublished}, #{isRecommended}, #{createTime}, #{updateTime}, #{typeId}, #{userId}, #{description})")
    @Options(useGeneratedKeys=true, keyProperty = "id",keyColumn="id")
    int add(Blog blog);

    @Update("update t_blog set typeId = 37 where typeId = #{id}")
    int deleteType(Long id);

    @Select("select * from t_blog where typeId = #{id}")
    List<Blog> findByTypeId(Long id);

    @Select("select * from t_blog where typeId = #{id} and isPublished=true")
    List<Blog> findPublicByTypeId(Long id);

    @Select("select * from t_blog")
    List<Blog> findAll();

    @Select("select date_format(b.createTime,'%Y') as year from t_blog b where b.isPublished=true GROUP by year ORDER BY year DESC")
    List<String> findYears();

    @Select("select * from t_blog b where date_format(b.createTime,'%Y') = #{year} and b.isPublished=true ORDER BY b.createTime DESC")
    List<Blog> findByYear(String year);

    @Update("update t_blog b set b.views = b.views+1 where b.id = #{id}")
    int updateViews(Long id);
}
