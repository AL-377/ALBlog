package com.aidan.alblogserver.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import javax.persistence.Basic;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// 设置表名
@TableName("t_blog")
public class Blog{
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String content;
    @TableField("titleBG")
    private String titleBG;
    private String flag;
    private Integer views;
    @TableField("isReward")
    private boolean isReward;
    @TableField("isCopyright")
    private boolean isCopyright;
    @TableField("isComment")
    private boolean isComment;
    @TableField("isPublished")
    private boolean isPublished;
    @TableField("isRecommended")
    private boolean isRecommended;

    private String description;
    @TableField("createTime")
    private Date createTime;
    @TableField("updateTime")
    private Date updateTime;

    // 博客属于1个类型
    @TableField(exist = false)
    private Type type;
    // 博客属于n个标签
    @TableField(exist = false)
    private List<Tag> tags = new ArrayList<>();
    // 博客下面有多个评论
    @TableField(exist = false)
    private List<Comment> comments = new ArrayList<>();
    // 博客所属用户
    @TableField(exist = false)
    private User user;

    // 所属的用户id
    @TableField("userId")
    private Long userId;

    // 所属的类型的id
    @TableField("typeId")
    private Long typeId;

    // 包含的标签连城一个串串
    @TableField(exist = false)
    private String tagIds;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getTagIds() {
        return tagIds;
    }

    public void setTagIds(String tagIds) {
        this.tagIds = tagIds;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public Blog() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitleBG() {
        return titleBG;
    }

    public void setTitleBG(String titleBG) {
        this.titleBG = titleBG;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public boolean getIsReward() {
        return isReward;
    }

    public void setIsReward(boolean reward) {
        isReward = reward;
    }

    public boolean getIsCopyright() {
        return isCopyright;
    }

    public void setIsCopyright(boolean copyright) {
        isCopyright = copyright;
    }

    public boolean getIsComment() {
        return isComment;
    }

    public void setIsComment(boolean comment) {
        isComment = comment;
    }

    public boolean getIsPublished() {
        return isPublished;
    }

    public void setIsPublished(boolean published) {
        isPublished = published;
    }

    public boolean getIsRecommended() {
        return isRecommended;
    }

    public void setIsRecommended(boolean recommended) {
        isRecommended = recommended;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", titleBG='" + titleBG + '\'' +
                ", flag='" + flag + '\'' +
                ", views=" + views +
                ", isReward=" + isReward +
                ", isCopyright=" + isCopyright +
                ", isComment=" + isComment +
                ", isPublished=" + isPublished +
                ", isRecommended=" + isRecommended +
                ", description='" + description + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", type=" + type +
                ", tags=" + tags +
                ", comments=" + comments +
                ", user=" + user +
                ", tagIds='" + tagIds + '\'' +
                ", typeId=" + typeId +
                '}';
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {

        this.tags = tags;
        //顺便更新tagIds
        if(tags != null && !tags.isEmpty())
        {
            StringBuffer sb = new StringBuffer();
            boolean flag=false;
            for(Tag tag:tags)
            {
                if(flag)sb.append(",");
                else flag=true;
                sb.append(tag.getId());
            }
            this.tagIds = sb.toString();
        }
        else tagIds = "";
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
