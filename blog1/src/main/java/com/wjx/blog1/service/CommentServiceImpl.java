package com.wjx.blog1.service;

import com.wjx.blog1.dao.CommentRepository;
import com.wjx.blog1.po.Comment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Comment> listCommentByBlogId(Long blogId) {
        Sort sort = Sort.by("createTime");
        List<Comment> comments = commentRepository.findByBlogIdAndParentCommentNull(blogId,sort);  //查询到评论集合查询到该博客的所有的评论信息  拿到的数据为第一层的数据
        return eachComment(comments);
    }

    //循环每个顶级评论节点
    private List<Comment> eachComment(List<Comment> comments) {
        //将数据从新copy一份，以免对源数据库中的操作产生影响
        List<Comment> commentsView = new ArrayList<>();
        for (Comment comment : comments){
            Comment c = new Comment();
            BeanUtils.copyProperties(comment, c);
            commentsView.add(c);
        }
        //合并评论的各层子代到第一级子代集合中
        combineChildren(commentsView);
        return commentsView;
    }

    private void combineChildren(List<Comment> comments){
        for (Comment comment : comments){                 //只遍历父亲节点
            List<Comment> replys1 = comment.getReplyComments(); //获取到父亲节点的子节点
            for (Comment reply1 : replys1) {
                //循环迭代，找到子代，存放在tempReplys中
                recursively(reply1);
            }
            //修改顶点节点的reply集合为迭代处理后的集合
            comment.setReplyComments(tempReplys);
            tempReplys = new ArrayList<>();
        }
    }

    //存放迭代找出的所有子代的集合
    private List<Comment> tempReplys = new ArrayList<>();

    //通过一次次的循环，父本以下的数据全部放入到一个集合之中
    private void recursively(Comment comment){
        tempReplys.add(comment);//顶节点添加到临时存放集合
        if (comment.getReplyComments().size()>0) {
            List<Comment> replys = comment.getReplyComments();
            for (Comment reply: replys) {
                tempReplys.add(reply);
                if (reply.getReplyComments().size()>0){
                    recursively(reply);
                }
            }
        }
    }

    @Transactional
    @Override
    public Comment saveComment(Comment comment) {
        Long parentCommentId = comment.getParentComment().getId();    //获得当前评论父亲的id
        if(parentCommentId!=-1){
            comment.setParentComment(commentRepository.getOne(parentCommentId));  //先获得父本的评论，然后将其加到comment内部
        }else {
            comment.setParentComment(null);   //如果没有父本的情况下将其设置为null
        }
        comment.setCreateTime(new Date());
        return commentRepository.save(comment);
    }
}
