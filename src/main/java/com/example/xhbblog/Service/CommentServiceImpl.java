package com.example.xhbblog.Service;

import com.example.xhbblog.mapper.CommentMapper;
import com.example.xhbblog.pojo.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    public List<Comment> findByAid(Integer aid,Integer start,Integer count) {
        List<Comment> comments=mapper.findByAid(aid,start,count);
        return getComments(comments);
    }

    @Override
    public List<Comment> listByAid(Integer aid) {
        return mapper.listByAid(aid);       //给后台查看评论使用的接口
    }

    List<Comment> getComments(List<Comment> comments)
    {
        ArrayList<Comment> anw = new ArrayList<>();
        for(int i=0;i<comments.size();i++)
        {
            Comment comment=comments.get(i);
            comment.setChilds(getChildComments(comment));  //全都变为"直接评论方的子节点"
            anw.add(comment);
        }
        return anw;
    }

    List<Comment> getChildComments(Comment comment)
    {
        List<Comment> anw=new ArrayList<>();
        for(int i=0;i<comment.getChilds().size();i++)
        {
            Comment comment1=comment.getChilds().get(i);
            anw.add(comment1);
            if(comment1.getChilds()!=null&&comment1.getChilds().size()!=0)
            {
                anw.addAll(getChildComments(comment1));
            }
        }
        return anw;
    }

    @Override
    public Integer countOfArticle(Integer aid) {
        return mapper.countOfArticle(aid);
    }

    @Override
    public Integer countOfComment(Integer aid) {
        return mapper.countOfComment(aid);
    }
}
