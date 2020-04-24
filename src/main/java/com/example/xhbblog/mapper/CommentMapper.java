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

    @Select("select visitor_name from comment where id=#{id}" )
    String findParentName(Integer id);       //获得每一个博客的父评论访问者姓名(若父节点为空则bu'cha)

    @Select("select * from comment where parentID= #{pid}")
    @Results(
            {
                    @Result(property = "id",column = "id"),
                    @Result(property = "aid", column = "aid"),
                    @Result(property = "parentID", column = "parentID"),
                    @Result(property = "article", column = "aid", one = @One(select = "com.example.xhbblog.mapper.ArticleMapper.selectByPrimaryKey")),
                    @Result(property = "parentVisitorName",column = "parentID",one = @One(select = "com.example.xhbblog.mapper.CommentMapper.findParentName")),
                    @Result(property = "childs",column = "id",many = @Many(select = "com.example.xhbblog.mapper.CommentMapper.findChilds"))
            })
    List<Comment> findChilds(Integer pid);

    @Select("select * from comment where aid=#{aid} order by id desc")         //按时间的倒序查询
    @Results(
            {
                    @Result(property = "id",column = "id"),
                    @Result(property = "aid", column = "aid"),
                    @Result(property = "article", column = "aid", one = @One(select = "com.example.xhbblog.mapper.ArticleMapper.selectByPrimaryKey")),
                    @Result(property = "childs",column = "id",many = @Many(select = "com.example.xhbblog.mapper.CommentMapper.findChilds"))
            })
    List<Comment> listByAid(Integer aid);           //给后台用

    @Select("select * from comment where aid=#{aid} and parentID is null order by id desc" +
            " limit #{start},#{count}")         //按时间的倒序查询
    @Results(
            {
                    @Result(property = "id",column = "id"),
                    @Result(property = "aid", column = "aid"),
                    @Result(property = "article", column = "aid", one = @One(select = "com.example.xhbblog.mapper.ArticleMapper.selectByPrimaryKey")),
                    @Result(property = "childs",column = "id",many = @Many(select = "com.example.xhbblog.mapper.CommentMapper.findChilds"))
            })
    List<Comment> findByAid(Integer aid,Integer start,Integer count);

    @Select("select count(*) from comment where aid=#{aid} and parentID is null")
    Integer countOfArticle(Integer aid);               //计算一篇文章的评论个数(不能包含回复)

    @Select("select count(*) from comment where aid=#{aid}")
    Integer countOfComment(Integer aid);              //显示的总数
}