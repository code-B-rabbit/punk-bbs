package com.example.xhbblog.Service;

import com.example.xhbblog.pojo.Comment;

import java.util.List;

public interface CommentService {
    public void add(Comment comment);
    public void delete(Integer id);
    public void update(Comment comment);
    public Comment get(Integer id);
    public List<Comment> list();
    public List<Comment> findByAid(Integer aid,Integer start,Integer count);
    public List<Comment> listByAid(Integer aid);
    public Integer countOfArticle(Integer aid);
    public Integer countOfComment(Integer aid);              //显示的总数
    List<Comment> lastComment();
    public List<Comment> listByUid(Integer aid);

}
