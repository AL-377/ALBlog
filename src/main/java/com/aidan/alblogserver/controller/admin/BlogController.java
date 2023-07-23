package com.aidan.alblogserver.controller.admin;

import com.aidan.alblogserver.pojo.Blog;
import com.aidan.alblogserver.pojo.Type;
import com.aidan.alblogserver.pojo.User;
import com.aidan.alblogserver.service.BlogService;
import com.aidan.alblogserver.service.TagService;
import com.aidan.alblogserver.service.TypeService;
import com.aidan.alblogserver.service.UserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class BlogController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;
    @Autowired
    private UserService userService;

    // 搜索页进入
    @GetMapping("/blogs")
    public String blogs(@RequestParam(defaultValue = "1") int pageNum, Blog blog, Model model)
    {
        //规定页面大小
        Page<Blog> page =  new Page(pageNum,5);
        Page<Blog> blogs = blogService.listBlog(page,blog);
        // 给每个blog加上type、user，tag；因为数据库中存储的仅仅是他们的id
        completeBlogs(blogs);
        blogs.getRecords().forEach(System.out::println);
        // 然后等下给前端
        int pagesNum = (int) page.getPages();
        model.addAttribute("types",typeService.listType());
        model.addAttribute("page",blogs);
        model.addAttribute("pagesNum",pagesNum);
        return "admin/blogs";
    }

    // 搜索页,查询键，部分更新
    @PostMapping("/blogs/search")
    public String search(@RequestParam(defaultValue = "1") int pageNum, String title,Long typeId,boolean isRecommended, Model model)
    {
        Page<Blog> page =  new Page(pageNum,5);
        // 设置查询条件
        Blog blog = new Blog();
        if(title!=null)
            blog.setTitle(title);
        if(typeId!=null){
            Type type = new Type();
            type.setId(typeId);
            blog.setType(type);
        }
        blog.setIsRecommended(isRecommended);

        Page<Blog> blogs = blogService.listBlog(page,blog);
        // 根据id补充blog的属性
        completeBlogs(blogs);
        blogs.getRecords().forEach(System.out::println);

        // 然后等下给前端
        int pagesNum = (int) page.getPages();
        model.addAttribute("pagesNum",pagesNum);
        model.addAttribute("page",blogs);

        return "admin/blogs :: blogList";
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

    // 获取所有的type和tag，供编辑页进行选择
    private void setTypeAndTag(Model model){
        model.addAttribute("tags",tagService.listTag());
        model.addAttribute("types",typeService.listType());
    }

    // 增加新的博客
    @GetMapping("/blogs/input")
    public String input(Model model)
    {
        setTypeAndTag(model);
        model.addAttribute("blog",new Blog());
        return "admin/blogs-input";
    }
    // 编辑博客
    @GetMapping("/blogs/{id}/input")
    public String editInput(@PathVariable Long id,Model model)
    {
        setTypeAndTag(model);
        Blog blog = blogService.getBlog(id);
        completeBlog(blog);
        model.addAttribute("blog",blog);
        System.out.println("to edit"+blogService.getBlog(id));
        return "admin/blogs-input";
    }


    // 提交和删除一起
    @PostMapping("/blogs")
    public String post(Blog blog, RedirectAttributes attributes,HttpSession session)
    {
        System.out.println("编辑页返回");
        System.out.println("blogs:"+blog);
        System.out.println("tagIds:"+blog.getTagIds());
        // 拿到当前的用户
        blog.setUser((User)session.getAttribute("user"));
        // 给一下用户id属性值
        blog.setUserId(blog.getUser().getId());
        // 由于从前端过来的只有type的id，所以要重新给他一个setType
        blog.setType(typeService.getType(blog.getTypeId()));
        // 传入blog的tagids,进行字符串处理
        blog.setTags(tagService.listTag(blog.getTagIds()));

        // 保存
        Blog b;
        if (blog.getId() == null) {
            b =  blogService.saveBlog(blog);
        } else {
            b = blogService.updateBlog(blog.getId(), blog);
        }

        if(b==null){
            attributes.addFlashAttribute("message","操作失败");
        }else{
            attributes.addFlashAttribute("message","操作成功");
        }
        return "redirect:/admin/blogs";
    }

    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes)
    {
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message","删除成功");

        return "redirect:/admin/blogs";
    }


}
