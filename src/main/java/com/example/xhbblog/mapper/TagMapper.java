package com.example.xhbblog.mapper;

import com.example.xhbblog.pojo.Tag;
import com.example.xhbblog.pojo.TagExample;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TagMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Tag record);

    int insertSelective(Tag record);

    List<Tag> selectByExample(TagExample example);

    Tag selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Tag record);

    int updateByPrimaryKey(Tag record);

    @Select("select * from tag")
    @Results({
            @Result(property = "id",column = "id"),
            @Result(property = "numbersOfBlog",column = "id",one = @One(select = "com.example.xhbblog.mapper.ArticleMapper.countOfTag")) //查询该标签下的博客数
    })
    List<Tag> list();
}