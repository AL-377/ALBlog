package com.aidan.alblogserver.service;

import com.aidan.alblogserver.pojo.Blog;
import com.aidan.alblogserver.pojo.Tag;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Map;

public interface BlogService {
    Blog getBlog(Long id);

    Blog getAndConvert(Long id);


    Page<Blog> listBlog(Page<Blog> page,Blog blog);

    Page<Blog> listBlog(Page<Blog> page);

    Page<Blog> listPublishedBlog(Page<Blog> page);

    Page<Blog> listPublishedTypeBlog(Page<Blog> page, Long typeId);

    Page<Blog> listPublishedQueryBlog(Page<Blog> page, String query);

    Page<Blog> listPublishedTagBlog(Page<Blog> page, Long tagId);

    Blog saveBlog(Blog blog);

    Blog updateBlog(Long id,Blog blog);

    void deleteBlog(Long id);

    List<Tag> getTags(Long id);

    List<Blog> listRecommendBlog(Integer topSize);

    Map<String,List<Blog>> archiveBlog();

    Integer countBlog();





}
