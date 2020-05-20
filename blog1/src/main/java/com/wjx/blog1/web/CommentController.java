package com.wjx.blog1.web;

import com.wjx.blog1.dao.CommentRepository;
import com.wjx.blog1.po.Comment;
import com.wjx.blog1.po.User;
import com.wjx.blog1.service.BlogService;
import com.wjx.blog1.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogService blogService;

    @Value("${comment.avatar}")    //使用value来获得其值
    private String avatar;


    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable Long blogId, Model model){
        model.addAttribute("comments",commentService.listCommentByBlogId(blogId));  //获取父本的评论然后将其体现在评论区
        return "blog :: commentList";
    }

    @PostMapping("/comments")
    public String post(Comment comment, HttpSession session){              //获取comment对象和session值
        Long blogId = comment.getBlog().getId();            //获取当前博客的id
        comment.setBlog(blogService.getBlog(blogId));   //查询到的信息放到comment中
        User user = (User) session.getAttribute("user");   //查询当前后台是否登录
        if (user != null) {
            comment.setAvatar(user.getAvatar());       //如果登录，重新设定头像
            comment.setAdminComment(true);            //将其标记为管理员信息
      //      comment.setNickname(user.getNickname());
        }else {
            comment.setAvatar(avatar);

        }
        commentService.saveComment(comment);
        return "redirect:/comments/" + comment.getBlog().getId();
    }
}
