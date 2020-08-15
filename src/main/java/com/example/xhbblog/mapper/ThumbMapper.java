package com.example.xhbblog.mapper;

import com.example.xhbblog.pojo.Thumb;
import com.example.xhbblog.pojo.ThumbExample;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ThumbMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Thumb record);

    int insertSelective(Thumb record);

    List<Thumb> selectByExample(ThumbExample example);

    Thumb selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Thumb record);

    int updateByPrimaryKey(Thumb record);

    @Delete("DELETE FROM thumb WHERE aid=#{aid}")
    public void deleteThumbByAid(Integer aid);          //删除文章时进行级联删除


    @Delete("DELETE FROM thumb WHERE address=#{address} AND aid=#{aid}")
    public void deleteThumb(Integer aid,String address);    //有则删除


    /**
     * 这里IGNORE是忽略错误
     * @param aid
     * @param address
     */
    @Insert("INSERT IGNORE INTO thumb(address,aid) " +
            "VALUES(#{address},#{aid}) ")
    public void insertThumbSelective(Integer aid,String address);

    /**
     * 获取某篇文章全部的点赞ip地址
     * @return
     */
    @Select("SELECT address FROM thumb WHERE aid=#{aid}")
    public List<String> ipOfAid(Integer aid);
}