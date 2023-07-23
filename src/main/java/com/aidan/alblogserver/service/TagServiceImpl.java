package com.aidan.alblogserver.service;


import com.aidan.alblogserver.exceptions.NotFoundException;
import com.aidan.alblogserver.mapper.BlogTagMapper;
import com.aidan.alblogserver.mapper.TagMapper;
import com.aidan.alblogserver.pojo.Tag;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.min;

@Service
public class TagServiceImpl implements TagService{
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private BlogTagMapper blogTagMapper;

    @Override
    public Tag saveTag(Tag tag) {
        int res = tagMapper.add(tag);
        if(res == 1)
            return tag;
        else return null;
    }

    @Override
    public Tag getTag(Long id) {
        return tagMapper.selectById(id);
    }

    @Override
    public Page<Tag> listTag(Page<Tag> page) {

        return tagMapper.selectPage(page,null);
    }


    @Override
    public Tag updateTag(Long id,Tag Tag) {
        int res = tagMapper.updateById(Tag);
        // 删除原本的id-blog对，因为已经不存在原本的tag了
        blogTagMapper.deleteTag(id);
        if(res == 1)
            return Tag;
        else return null;
    }

    @Override
    public void deleteTag(Long id) {
        int res = tagMapper.deleteById(id);
        blogTagMapper.deleteTag(id);
        if  (res == 0)
            throw new NotFoundException("发生错误");
    }

    @Override
    public Tag getTagByName(String name) {

        return tagMapper.findByName(name);
    }

    @Override
    public List<Tag> listTag() {

        return tagMapper.findAll();
    }

    //  ids里面是逗号隔开的id
    @Override
    public List<Tag> listTag(String ids) {
        List<Tag> tags = new ArrayList<>();
        if(ids.isEmpty()){
            return tags;
        }
        QueryWrapper<Tag> qt = new QueryWrapper<>();
        qt.in("id",splitCommas(ids));
        tags = tagMapper.selectList(qt);
        return tags;
    }

    @Override
    public List<Tag> listTagTop(Integer topSize) {
        // 找公开的blog且，填充好了tag的blogs属性
        List<Tag> tags = tagMapper.findAllTagPublicBlogs();
        tags.sort(Comparator.comparing(Tag::getBlogSize).reversed());
        List<Tag> ans = new ArrayList<>();
        for(int i = 0;i < min(topSize,tags.size());i++){
            ans.add(tags.get(i));
        }
        return ans;
    }


    //按照逗号分割
    private List<Long> splitCommas(String ids)
    {

        List<Long> longList = Arrays.stream(ids.split(","))
                .map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        return longList;
    }



}
