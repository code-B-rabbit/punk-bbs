package com.example.xhbblog.Service;

import com.example.xhbblog.pojo.Comment;
import com.example.xhbblog.pojo.Tag;

import java.util.List;

public interface CommentService {
    public void add(Comment comment);
    public void delete(Integer id);
    public void update(Comment comment);
    public Comment get(Integer id);
    public List<Comment> list();
    public List<Comment> findByAid(Integer aid);
    public Integer countOfArticle(Integer aid);
}
