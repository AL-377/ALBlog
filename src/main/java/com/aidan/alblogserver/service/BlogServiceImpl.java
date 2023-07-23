package com.aidan.alblogserver.service;

import com.aidan.alblogserver.exceptions.NotFoundException;
import com.aidan.alblogserver.mapper.BlogMapper;
import com.aidan.alblogserver.mapper.BlogTagMapper;
import com.aidan.alblogserver.pojo.Blog;
import com.aidan.alblogserver.pojo.Tag;
import com.aidan.alblogserver.pojo.Type;
import com.aidan.alblogserver.util.MarkdownUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mysql.jdbc.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class BlogServiceImpl implements BlogService{

    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private BlogTagMapper blogTagMapper;

    @Override
    public Blog getBlog(Long id) {

        return blogMapper.selectById(id);

    }

    @Override
    public Blog getAndConvert(Long id) {
        Blog blog = blogMapper.selectById(id);
        if(blog==null)
            throw new NotFoundException("博客不存在");
        String content = blog.getContent();
        blog.setContent( MarkdownUtil.markdownToHtmlExtensions(content));
        blogMapper.updateViews(id);
        return blog;
    }


    @Override
    public Page<Blog> listPublishedBlog(Page<Blog> page)
    {
        QueryWrapper<Blog> qp = new QueryWrapper<>();
        // 根据发表时间排序
        qp.orderByDesc("createTime");
        qp.eq("isPublished",true);
        return blogMapper.selectPage(page,qp);
    }


    @Override
    public Page<Blog> listPublishedTypeBlog(Page<Blog> page,Long typeId)
    {
        QueryWrapper<Blog> qp = new QueryWrapper<>();
        // 根据发表时间排序
        qp.orderByDesc("createTime");
        qp.eq("isPublished",true);
        qp.eq("typeId",typeId);
        return blogMapper.selectPage(page,qp);
    }


    @Override
    public Page<Blog> listPublishedQueryBlog(Page<Blog> page, String query) {
        QueryWrapper<Blog> qp = new QueryWrapper<>();
        // 根据发表时间排序
        qp.orderByDesc("createTime");
        //模糊查询
        qp.and((wrapper) -> {
            wrapper.like("title", query).or().like("content", query).or().like("description", query);
        });
        qp.eq("isPublished",true);

        return blogMapper.selectPage(page,qp);
    }

    @Override
    public Page<Blog> listPublishedTagBlog(Page<Blog> page, Long tagId) {

        QueryWrapper<Blog> qp = new QueryWrapper<>();
        // 对应tag的blog id
        List<Long> blogIds = blogTagMapper.findPublicBlogIdByTag(tagId);


        // 根据发表时间排序
        qp.orderByDesc("createTime");
        qp.eq("isPublished",true);
        if(blogIds==null || blogIds.isEmpty())
            qp.eq("id",-37);
        else qp.in("id",blogIds);
        return blogMapper.selectPage(page,qp);
    }

    // 设置动态条件的分页查询
    @Override
    public Page<Blog> listBlog(Page<Blog> page, Blog blog) {
        QueryWrapper<Blog> qp = new QueryWrapper<>();
        // 根据发表时间排序
        qp.orderByDesc("createTime");

        String title = blog.getTitle();
        Type type = blog.getType();
        boolean isRecommended = blog.getIsRecommended();

        if(!StringUtils.isNullOrEmpty(title)){
            qp.like("title",title);
        }
        if(type != null){
            qp.eq("typeId",type.getId());
        }
        qp.eq("isRecommended",isRecommended);

        return blogMapper.selectPage(page,qp);
    }

    @Override
    public Page<Blog> listBlog(Page<Blog> page) {
        QueryWrapper<Blog> qp = new QueryWrapper<>();
        // 根据创建时间排序
        qp.orderByDesc("createTime");
        return blogMapper.selectPage(page,qp);
    }

    @Override
    public Blog saveBlog(Blog blog) {

        if (blog.getId() == null) {
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
        } else {
            blog.setUpdateTime(new Date());
        }

        int res =  blogMapper.add(blog);
        // 保存tag-blog映射
        if(blog.getTags()!=null && !blog.getTags().isEmpty())
            blogTagMapper.add(blog.getId(),blog.getTags());
        if(res == 1)return blog;
        else return null;
    }

    @Override
    public Blog updateBlog(Long id, Blog blog) {
        // 更新时间
        blog.setUpdateTime(new Date());
        int res =  blogMapper.updateById(blog);
        // 先删除原本的,然后重新添加
        blogTagMapper.deleteBlog(blog.getId());
        if(blog.getTags()!=null && !blog.getTags().isEmpty())
            blogTagMapper.add(blog.getId(),blog.getTags());
        if(res == 1)return blog;
        else return null;
    }

    @Override
    public void deleteBlog(Long id) {
        int res =  blogMapper.deleteById(id);
        // 删除对应的tag-blog对
        blogTagMapper.deleteBlog(id);
        if  (res == 0)
            throw new NotFoundException("发生错误");
    }

    @Override
    public List<Tag> getTags(Long id) {

        return blogTagMapper.findTagByBlog(id);
    }

    @Override
    public List<Blog> listRecommendBlog(Integer topSize) {

        List<Blog> blogs = blogMapper.findAll();
        blogs.sort(Comparator.comparing(Blog::getUpdateTime).reversed());
        List<Blog> ans = new ArrayList<>();
        for(int i = 0;i < blogs.size();i++){
            if(blogs.get(i).getIsRecommended() && blogs.get(i).getIsPublished()){
                ans.add(blogs.get(i));
            }
            if(ans.size()==topSize)
                break;
        }
        return ans;
    }

    @Override
    public Map<String, List<Blog>> archiveBlog() {


        List<String> years = blogMapper.findYears();
        Map<String,List<Blog>> map = new HashMap<>();
        for(String year:years)
        {
            map.put(year,blogMapper.findByYear(year));
        }
        return map;
    }

    @Override
    public Integer countBlog() {
        QueryWrapper<Blog> qp = new QueryWrapper<>();
        qp.eq("isPublished",true);
        return blogMapper.selectCount(qp);
    }


}
