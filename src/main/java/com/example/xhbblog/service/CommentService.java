package com.example.xhbblog.service;

import com.example.xhbblog.pojo.Comment;
import com.example.xhbblog.pojo.User;

import java.io.IOException;
import java.util.List;

/**
 * 评论业务接口
 */
public interface CommentService {
    public void add(Comment comment);
    public void update(Comment comment);
    public Comment get(Integer id);
    public List<Comment> list();
    public List<Comment> findByAid(Integer aid,Integer start,Integer count);
    public Integer countOfArticle(Integer aid);
    public List<Comment> lastComment();
    public List<Comment> listByAid(Integer aid);
    public List<Comment> listByUid(Integer aid);
    public List<Comment> findChilds(Integer cid);
    public void sendComment(Comment comment,User user) throws IOException;            //暂时存储评论信息
    public void delete(Comment comment);
    public void deleteCids(List<Comment> comments);
    public Integer count();
    List<Comment> listAnonymousByUid(Integer uid);
    List<Comment> listAnonymousComment();

    public Integer countOfComment(Integer aid);              //显示的总数
}
