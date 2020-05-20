package com.wjx.blog1.dao;

import com.wjx.blog1.po.Type;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TypeRepository extends JpaRepository<Type,Long> {

    Type findByName(String name);

    @Query("select t from Type t")  //将Type的别名设置为t
    List<Type> findTop(Pageable pageable);
}
