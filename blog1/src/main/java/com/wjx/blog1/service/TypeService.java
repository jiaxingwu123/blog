package com.wjx.blog1.service;

import com.wjx.blog1.po.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TypeService {

    Type saveType(Type type);   //保存的类型，将其保存

    Type getType(Long id);  //根据id来查询type

    Type getTypeByName(String name);       //通过名称来查询方法

    Page<Type> listType(Pageable pageable);   //根据分页来查询

    List<Type> listType();

    List<Type> listTypeTop(Integer size);   //前台页面右侧的标签   根据值来取数据列表的大小

    Type updateType(Long id,Type type);    //更新type类型，也就是编辑更新

    void deleteType(Long id);    //删除type类型
}
