package com.wjx.blog1.dao;

import com.wjx.blog1.po.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog,Long>, JpaSpecificationExecutor<Blog> {  //JpaSpecificationExecutor这个接口可以帮助我们动态的查询

    @Query("select b from Blog b where b.recommend=true")
    List<Blog> findTop(Pageable pageable);      //前端页面推荐栏是否推荐内容


    //select * from t_blog where title like '%内容%'
    @Query("select b from Blog b where b.title like ?1 or b.content like ?1")    //?1代表第一个参数
    Page<Blog> findByQuery(String query,Pageable pageable);

    @Transactional
    @Modifying
    @Query("update Blog b set b.views = b.views+1 where b.id = ?1")//?1代表只有一个参数传过来
    int updateViews(Long id);

    @Query("select function('date_format', b.updateTime, '%Y') as year from Blog b group by function('date_format', b.updateTime, '%Y') order by year desc ")
    List<String> findGroupYear();

    @Query("select b from Blog b where function('date_format', b.updateTime, '%Y') = ?1")
    List<Blog> findByYear(String year);
}
