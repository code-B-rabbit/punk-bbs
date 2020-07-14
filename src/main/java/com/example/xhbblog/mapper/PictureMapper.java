package com.example.xhbblog.mapper;

import com.example.xhbblog.pojo.Picture;
import com.example.xhbblog.pojo.PictureExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PictureMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Picture record);

    int insertSelective(Picture record);

    List<Picture> selectByExample(PictureExample example);

    Picture selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Picture record);

    int updateByPrimaryKey(Picture record);

    @Select("select auto_increment from information_schema.`TABLES` where table_name='picture' and TABLE_SCHEMA='xhb_blog'")
    int getId();

    @Select("SELECT COUNT(*) FROM picture")
    public Integer count();
}