package com.example.xhbblog.mapper;

import com.example.xhbblog.pojo.User;
import com.example.xhbblog.pojo.UserExample;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    List<User> selectByExample(UserExample example);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    @Select("SELECT COUNT(*) FROM user")
    Integer count();

    @Select("select * from user where role='user' ")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "commentSize", column = "id", one = @One(select = "com.example.xhbblog.mapper.CommentMapper.countOfUser")),
            @Result(property = "articleSize", column = "id", one = @One(select = "com.example.xhbblog.mapper.ArticleMapper.countOfUid"))
    })
    List<User> selectUser();

    @Select("select count(*) from user where name=#{name}")
    int checkName(String name);

    @Select("select count(*) from user where email=#{email}")
    int checkEmail(String email);

    @Select("select name from user where id=#{uid} limit 1")       //查询姓名
    String findName(Integer uid);

    @Select("select email from user where id=#{uid} limit 1")       //查询姓名
    String findEmail(Integer uid);

    @Select("select * from user where name=#{name} limit 1")       //查询匿名用户的uid
    User  getUid(String name);

}