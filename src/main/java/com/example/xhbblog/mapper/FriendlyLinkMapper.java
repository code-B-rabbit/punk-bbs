package com.example.xhbblog.mapper;

import com.example.xhbblog.pojo.FriendlyLink;
import com.example.xhbblog.pojo.FriendlyLinkExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface FriendlyLinkMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FriendlyLink record);

    int insertSelective(FriendlyLink record);

    List<FriendlyLink> selectByExample(FriendlyLinkExample example);

    FriendlyLink selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FriendlyLink record);

    int updateByPrimaryKey(FriendlyLink record);

    @Select("SELECT COUNT(*) FROM friendly_link")
    public Integer count();
}