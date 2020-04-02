package com.example.xhbblog.Service;

import com.example.xhbblog.mapper.CommentMapper;
import com.example.xhbblog.pojo.Comment;
import com.example.xhbblog.pojo.CommentExample;
import com.example.xhbblog.pojo.TagExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CommentServiceImpl implements CommentService{

    @Autowired
    private CommentMapper mapper;

    @Override
    public void add(Comment comment) {
        mapper.insert(comment);
    }

    @Override
    public void delete(Integer id) {
        mapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Comment comment) {
        mapper.updateByPrimaryKey(comment);
    }

    @Override
    public Comment get(Integer id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Comment> list() {
        return mapper.list();
    }

    @Override
    public List<Comment> findByAid(Integer aid) {
        return mapper.findByAid(aid);
    }

    @Override
    public Integer countOfArticle(Integer aid) {
        return mapper.countOfArticle(aid);
    }
}
