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

    @Select("SELECT * FROM comment ORDER BY ID DESC LIMIT 5")
    List<Comment> lastComment();         //最新评论

    @Select("SELECT COUNT(*) FROM comment")
    public Integer count();


    @Select("select count(*) from comment where uid=#{uid}")
    public Integer countOfUser(Integer uid);

    /**
     * 查询所有评论
     * @return
     */
    @Select("select * from comment ORDER BY id DESC")
    List<Comment> list();

    @Select("SELECT name from user u where id = (SELECT uid FROM COMMENT WHERE ID=#{id})" )
    String findParentName(Integer id);       //获得每一个评论的父评论访问者姓名(若父节点为空则bu'cha)

    @Select("select * from comment where aid=#{aid} order by id desc")         //按时间的倒序查询
    List<Comment> listByAid(Integer aid);           //给后台用

    @Select("select * from comment where uid=#{uid} order by id desc")         //按时间的倒序查询
    List<Comment> listByUid(Integer uid);


    @Select("select * from comment where parentID=#{cid} order by id desc")         //按时间的倒序查询
    List<Comment> listByCid(Integer cid);


    @Select("select * from comment where aid=#{aid} and parentID is null order by id desc" +
            " limit #{start},#{count}")         //按时间的倒序查询
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
}