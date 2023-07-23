package com.aidan.alblogserver.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("t_blog_tag")
public class BlogTag {
    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField("blogId")
    private Long blogId;
    @TableField("tagId")
    private Long tagId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public BlogTag() {
    }


    @Override
    public String toString() {
        return "BlogTag{" +
                "id=" + id +
                ", blogId=" + blogId +
                ", tagId=" + tagId +
                '}';
    }

}
