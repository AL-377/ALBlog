package com.aidan.alblogserver.mapper;

import com.aidan.alblogserver.pojo.Blog;
import com.aidan.alblogserver.pojo.Tag;
import com.aidan.alblogserver.vo.BlogTag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BlogTagMapper extends BaseMapper<BlogTag> {

    @Insert("<script>"+
            "insert into t_blog_tag (blogId,tagId) values "+
            "<foreach collection='tags' item='tag' separator=','>"+
            "(#{blogId},#{tag.id})" +
            "</foreach>" +
            "</script>"
    )
    int add(@Param("blogId") Long blogId,@Param("tags") List<Tag> tags);

    @Delete("delete from t_blog_tag where blogId=#{blogId}")
    int deleteBlog(Long blogId);

    @Delete("delete from t_blog_tag where tagId=#{tagId}")
    int deleteTag(Long tagId);

    @Select("select * from t_blog where id in (select blogId from t_blog_tag where tagId=#{tagId})")
    List<Blog> findBlogByTag(Long tagId);

    @Select("select * from t_blog where id in (select blogId from t_blog_tag where tagId=#{tagId}) and isPublished=true")
    List<Blog> findPublicBlogByTag(Long tagId);

    @Select("select id from t_blog where id in (select blogId from t_blog_tag where tagId=#{tagId}) and isPublished=true")
    List<Long> findPublicBlogIdByTag(Long tagId);


    @Select("select * from t_tag where id in (select tagId from t_blog_tag where blogId=#{blogId})")
    List<Tag> findTagByBlog(Long blogId);






}
