package com.aidan.alblogserver.controller;


import com.aidan.alblogserver.pojo.Blog;
import com.aidan.alblogserver.pojo.Tag;
import com.aidan.alblogserver.service.BlogService;
import com.aidan.alblogserver.service.TagService;
import com.aidan.alblogserver.service.TypeService;
import com.aidan.alblogserver.service.UserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class TagShowController {

    @Autowired
    private TagService tagService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private UserService userService;

    @GetMapping("/tags/{id}")
    public String tags(@RequestParam(defaultValue = "1") int pageNum, @PathVariable Long id, Model model)
    {
        // 拿到全部tag
        List<Tag> tags = tagService.listTagTop(1000);
        if(id == -1){
            id = tags.get(0).getId();
        }
        // 查询对应tag的blog
        Page<Blog> page =  new Page(pageNum,10);
        Page<Blog> blogs = blogService.listPublishedTagBlog(page,id);
        // 给每个blog加上type、user，tag；因为数据库中存储的仅仅是他们的id
        completeBlogs(blogs);
        int pagesNum = (int) page.getPages();
        model.addAttribute("pagesNum",pagesNum);
        model.addAttribute("tags",tags);
        model.addAttribute("page",blogs);
        model.addAttribute("activeTagId",id);


        return "tags";
    }


    // 补全blog的属性（进入搜索页）
    private void completeBlogs(Page<Blog> blogs)
    {
        for(Blog tp:blogs.getRecords()){
            completeBlog(tp);
        }
    }

    // 补全blog的属性（进入编辑）
    private void completeBlog(Blog blog)
    {
        blog.setType(typeService.getType(blog.getTypeId()));
        blog.setUser(userService.getUser(blog.getUserId()));
        // 利用blog和tag的id查询
        blog.setTags(blogService.getTags(blog.getId()));
    }




}
