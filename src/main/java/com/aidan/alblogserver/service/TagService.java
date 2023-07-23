package com.aidan.alblogserver.service;

import com.aidan.alblogserver.pojo.Tag;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TagService{

    Tag saveTag(Tag tag);

    Tag getTag(Long id);

    Page<Tag> listTag(Page<Tag> page);

    Tag updateTag(Long id,Tag type);

    void deleteTag(Long id);

    Tag getTagByName(String name);

    List<Tag> listTag();
    //逗号隔开的多个id
    List<Tag> listTag(String ids);

    List<Tag> listTagTop(Integer topSize);



}
