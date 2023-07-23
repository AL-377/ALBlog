package com.aidan.alblogserver.controller;

import com.aidan.alblogserver.pojo.Blog;
import com.aidan.alblogserver.pojo.Type;
import com.aidan.alblogserver.service.BlogService;
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
public class TypeShowController {

    @Autowired
    private TypeService typeService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private UserService userService;

    @GetMapping("/types/{id}")
    public String types(@RequestParam(defaultValue = "1") int pageNum,@PathVariable Long id, Model model)
    {
        // 拿到全部分类
        List<Type> types = typeService.listTypeTop(1000);
        if(id == -1){
            id = types.get(0).getId();
        }
        // 查询对应type的blog
        Page<Blog> page =  new Page(pageNum,10);
        Page<Blog> blogs = blogService.listPublishedTypeBlog(page,id);
        // 给每个blog加上type、user，tag；因为数据库中存储的仅仅是他们的id
        completeBlogs(blogs);
        int pagesNum = (int) page.getPages();
        model.addAttribute("pagesNum",pagesNum);
        model.addAttribute("types",types);
        model.addAttribute("page",blogs);
        model.addAttribute("activeTypeId",id);
        return "types";
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
