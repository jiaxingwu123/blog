package com.wjx.blog1.service;

import com.wjx.blog1.po.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> listCommentByBlogId(Long blogId);   //获取评论区的列表

    Comment saveComment(Comment comment);   //保存评论的信息
}
