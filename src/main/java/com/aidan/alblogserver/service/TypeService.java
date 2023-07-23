package com.aidan.alblogserver.service;


import com.aidan.alblogserver.pojo.Type;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface TypeService {

    Type saveType(Type type);

    Type getType(Long id);

    Page<Type> listType(Page<Type> page);

    Type updateType(Long id,Type type);

    void deleteType(Long id);

    Type getTypeByName(String name);

    List<Type> listType();

    List<Type> listTypeTop(Integer topSize);
}
