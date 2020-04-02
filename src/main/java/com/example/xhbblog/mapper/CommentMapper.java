package com.example.xhbblog.mapper;

import com.example.xhbblog.pojo.Comment;
import com.example.xhbblog.pojo.CommentExample;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Comment record);

    int insertSelective(Comment record);

    List<Comment> selectByExampleWithBLOBs(CommentExample example);

    List<Comment> selectByExample(CommentExample example);

    Comment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKeyWithBLOBs(Comment record);

    int updateByPrimaryKey(Comment record);

    @Select("select * from comment")
    @Results(
            {
                    @Result(property = "aid", column = "aid"),
                    @Result(property = "article", column = "aid", one = @One(select = "com.example.xhbblog.mapper.ArticleMapper.selectByPrimaryKey"))
            })
    List<Comment> list();

    @Select("select * from comment where aid=#{aid} order by id desc")         //按时间的倒序查询
    @Results(
            {
                    @Result(property = "aid", column = "aid"),
                    @Result(property = "article", column = "aid", one = @One(select = "com.example.xhbblog.mapper.ArticleMapper.selectByPrimaryKey"))
            })
    List<Comment> findByAid(Integer aid);

    @Select("select count(*) from comment where aid=#{aid}")
    Integer countOfArticle(Integer aid);               //计算一篇文章的评论个数

}