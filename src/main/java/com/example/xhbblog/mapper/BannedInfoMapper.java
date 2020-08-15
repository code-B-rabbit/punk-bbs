package com.example.xhbblog.mapper;

import com.example.xhbblog.pojo.BannedInfo;
import com.example.xhbblog.pojo.BannedInfoExample;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BannedInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BannedInfo record);

    int insertSelective(BannedInfo record);

    List<BannedInfo> selectByExample(BannedInfoExample example);

    BannedInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BannedInfo record);

    int updateByPrimaryKey(BannedInfo record);
}