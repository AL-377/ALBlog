package com.aidan.alblogserver.controller;

import com.aidan.alblogserver.pojo.Blog;
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

@Controller
public class IndexController {
    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index(@RequestParam(defaultValue = "1") int pageNum,Model model)
    {

        //规定页面大小
        Page<Blog> page =  new Page(pageNum,10);
        Page<Blog> blogs = blogService.listPublishedBlog(page);
        // 给每个blog加上type、user，tag；因为数据库中存储的仅仅是他们的id
        completeBlogs(blogs);
        blogs.getRecords().forEach(System.out::println);

        int pagesNum = (int) page.getPages();
        model.addAttribute("page",blogs);
        model.addAttribute("pagesNum",pagesNum);
        // 首页展示的type
        model.addAttribute("types",typeService.listTypeTop(6));
        // 首页展示的tag
        model.addAttribute("tags",tagService.listTagTop(10));
        // 首页推荐博客（推荐且更新时间最近）
        model.addAttribute("recommendBlogs",blogService.listRecommendBlog(8));

        return "index";
    }
    @GetMapping("/blog")
    public String blog()
    {
        return "blog";
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

    @GetMapping("/search")
    public String search(@RequestParam(defaultValue = "1") int pageNum,@RequestParam String query,Model model)
    {

        Page<Blog> page =  new Page(pageNum,10);
        // 可能有sql注入问题
        Page<Blog> blogs =blogService.listPublishedQueryBlog(page,"%"+query+"%");
        completeBlogs(blogs);
        blogs.getRecords().forEach(System.out::println);

        int pagesNum = (int) page.getPages();
        model.addAttribute("pagesNum",pagesNum);
        model.addAttribute("page",blogs);
        model.addAttribute("query",query);

        return "search";
    }

    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id,Model model)
    {
        Blog blog = blogService.getAndConvert(id);
        completeBlog(blog);
        model.addAttribute("blog",blog);
        return "blog";
    }

    @GetMapping("/footer/newBlogs")
    public String newBlogs(Model model)
    {
        model.addAttribute("newBlogs",blogService.listRecommendBlog(3));
        return "_fragments :: newBlogList";
    }








}
