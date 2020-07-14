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

        @Select("SELECT COUNT(*) FROM comment")
        public Integer count();


        @Select("select count(*) from comment where uid=#{uid}")
        public Integer countOfUser(Integer uid);

/**
 * 查询所有评论
 * @return
 */
        @Select("select * from comment ORDER BY id DESC")
        @Results(
        {
                @Result(property = "id",column = "id"),
                @Result(property = "uid",column = "uid"),
                @Result(property = "aid", column = "aid"),
                @Result(property = "parentID", column = "parentID"),
                @Result(property = "parentVisitorName",column = "parentID",one = @One(select = "com.example.xhbblog.mapper.CommentMapper.findParentName")),
                @Result(property = "visitor_name",column = "uid",one = @One(select = "com.example.xhbblog.mapper.UserMapper.findName")),
                @Result(property = "visitor_email",column = "uid",one = @One(select = "com.example.xhbblog.mapper.UserMapper.findEmail")),
                @Result(property = "article", column = "aid", one = @One(select = "com.example.xhbblog.mapper.ArticleMapper.selectByPrimaryKey")),
                @Result(property = "childs",column = "id",many = @Many(select = "com.example.xhbblog.mapper.CommentMapper.findChilds"))
        })
    List<Comment> list();

    @Select("SELECT name from user u where id IN (SELECT UID FROM COMMENT WHERE ID=#{id})" )
    String findParentName(Integer id);       //获得每一个评论的父评论访问者姓名(若父节点为空则bu'cha)

    @Select("select * from comment where aid=#{aid} order by id desc")         //按时间的倒序查询
    @Results({
                @Result(property = "id",column = "id"),
                @Result(property = "aid", column = "aid"),
                @Result(property = "uid", column = "uid"),
                @Result(property = "visitor_email", column = "uid",one = @One(select = "com.example.xhbblog.mapper.UserMapper.findEmail")),
                @Result(property = "visitor_name", column = "uid",one = @One(select = "com.example.xhbblog.mapper.UserMapper.findName")),
                @Result(property = "article", column = "aid", one = @One(select = "com.example.xhbblog.mapper.ArticleMapper.selectByPrimaryKey")),
                @Result(property = "childs",column = "id",many = @Many(select = "com.example.xhbblog.mapper.CommentMapper.findChilds"))
    })
    List<Comment> listByAid(Integer aid);           //给后台用

    @Select("select * from comment where uid=#{uid} order by id desc")         //按时间的倒序查询
    @Results(
        {
                @Result(property = "id",column = "id"),
                @Result(property = "aid", column = "aid"),
                @Result(property = "uid", column = "uid"),
                @Result(property = "visitor_email", column = "uid",one = @One(select = "com.example.xhbblog.mapper.UserMapper.findEmail")),
                @Result(property = "visitor_name", column = "uid",one = @One(select = "com.example.xhbblog.mapper.UserMapper.findName")),
                @Result(property = "article", column = "aid", one = @One(select = "com.example.xhbblog.mapper.ArticleMapper.selectByPrimaryKey")),
                @Result(property = "childs",column = "id",many = @Many(select = "com.example.xhbblog.mapper.CommentMapper.findChilds"))
        })
    List<Comment> listByUid(Integer uid);


    @Select("select * from comment where parentID=#{cid} order by id desc")         //按时间的倒序查询
    @Results(
            {
                    @Result(property = "id",column = "id"),
                    @Result(property = "aid", column = "aid"),
                    @Result(property = "uid", column = "uid"),
                    @Result(property = "visitor_email", column = "uid",one = @One(select = "com.example.xhbblog.mapper.UserMapper.findEmail")),
                    @Result(property = "visitor_name", column = "uid",one = @One(select = "com.example.xhbblog.mapper.UserMapper.findName")),
                    @Result(property = "article", column = "aid", one = @One(select = "com.example.xhbblog.mapper.ArticleMapper.selectByPrimaryKey")),
                    @Result(property = "childs",column = "id",many = @Many(select = "com.example.xhbblog.mapper.CommentMapper.findChilds"))
            })
    List<Comment> listByCid(Integer cid);

    @Select("select * from comment where parentID=#{cid} order by id desc")
    @Results(
        {
                @Result(property = "id",column = "id"),
                @Result(property = "aid", column = "aid"),
                @Result(property = "uid", column = "uid"),
                @Result(property = "parentID", column = "parentID"),
                @Result(property = "visitor_name", column = "uid",one = @One(select = "com.example.xhbblog.mapper.UserMapper.findName")),
                @Result(property = "article", column = "aid", one = @One(select = "com.example.xhbblog.mapper.ArticleMapper.selectByPrimaryKey")),
                @Result(property = "parentVisitorName",column = "parentID",one = @One(select = "com.example.xhbblog.mapper.CommentMapper.findParentName")),
                @Result(property = "childs",column = "id",many = @Many(select = "com.example.xhbblog.mapper.CommentMapper.findChilds"))
        })
    List<Comment> findChilds(@Param("cid") Integer cid);

    @Select("select * from comment where parentID=#{id} order by id desc")
    List<Comment> deleteDfs(Integer id);


    @Select("select * from comment where aid=#{aid} and parentID is null order by id desc" +
        " limit #{start},#{count}")         //按时间的倒序查询
    @Results(
        {
                @Result(property = "id",column = "id"),
                @Result(property = "aid", column = "aid"),
                @Result(property = "article", column = "aid", one = @One(select = "com.example.xhbblog.mapper.ArticleMapper.selectByPrimaryKey")),
                @Result(property = "visitor_name", column = "uid",one = @One(select = "com.example.xhbblog.mapper.UserMapper.findName")),
                @Result(property = "childs",column = "id",many = @Many(select = "com.example.xhbblog.mapper.CommentMapper.findChilds"))
        })
    List<Comment> findByAid(Integer aid,Integer start,Integer count);


/**
 * 父评论的所有子评论
 * @param cid
 * @return
 */
    @Select("SELECT COUNT(*) FROM comment where parentID=#{cid}")
    Integer countOfPar(Integer cid);

    @Select("select count(*) from comment where aid=#{aid} and parentID is null")
    Integer countOfArticle(Integer aid);               //计算一篇文章的评论个数(不能包含回复)

    @Select("select count(*) from comment where aid=#{aid}")
    Integer countOfComment(Integer aid);              //显示的总数

    @Select("SELECT * FROM comment ORDER BY ID DESC LIMIT 5")
    @Results(
        {
                @Result(property = "id",column = "id"),
                @Result(property = "aid", column = "aid"),
                @Result(property = "visitor_name", column = "uid",one = @One(select = "com.example.xhbblog.mapper.UserMapper.findName")),
        })
    List<Comment> lastComment();         //最新评论
}