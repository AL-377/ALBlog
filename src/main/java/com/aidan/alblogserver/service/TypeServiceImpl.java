package com.aidan.alblogserver.service;

import com.aidan.alblogserver.exceptions.NotFoundException;
import com.aidan.alblogserver.mapper.BlogMapper;
import com.aidan.alblogserver.mapper.TypeMapper;
import com.aidan.alblogserver.pojo.Type;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.lang.Math.min;


@Service
public class TypeServiceImpl implements TypeService{

    @Autowired
    private TypeMapper typeMapper;
    @Autowired
    private BlogMapper blogMapper;

    @Override
    public Type saveType(Type type) {
        int res = typeMapper.add(type);
        if(res == 1)
            return type;
        else return null;
    }

    @Override
    public Type getType(Long id) {

        return typeMapper.selectById(id);
    }

    @Override
    public Page<Type> listType(Page<Type> page) {
        return typeMapper.selectPage(page,null);
    }


    @Override
    public Type updateType(Long id,Type type) {
        int res = typeMapper.updateById(type);
        blogMapper.deleteType(id);
        if(res == 1)
            return type;
        else return null;
    }

    @Override
    public void deleteType(Long id) {
        int res = typeMapper.deleteById(id);
        blogMapper.deleteType(id);
        if  (res == 0)
            throw new NotFoundException("发生错误");
    }

    @Override
    public Type getTypeByName(String name) {

        return typeMapper.findByName(name);
    }

    @Override
    public List<Type> listType() {

        return typeMapper.findAll();
    }

    @Override
    public List<Type> listTypeTop(Integer topSize) {
        // 填充好了type的blogs属性
        List<Type> types = typeMapper.findAllTypePublicBlogs();
        types.sort(Comparator.comparing(Type::getBlogSize).reversed());
        List<Type> ans = new ArrayList<>();
        for(int i = 0;i < min(topSize,types.size());i++){
            ans.add(types.get(i));
        }
        return ans;
    }
}
