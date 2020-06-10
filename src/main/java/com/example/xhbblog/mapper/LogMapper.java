package com.example.xhbblog.mapper;

import com.example.xhbblog.pojo.Log;
import com.example.xhbblog.pojo.LogExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Log record);

    int insertSelective(Log record);

    List<Log> selectByExample(LogExample example);

    Log selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Log record);

    int updateByPrimaryKey(Log record);

    @Select("SELECT * FROM log WHERE date_sub(curdate(), interval 7 day) <= date(createTime)" +
            "ORDER BY createTime")   //查询出最近一周的数据
    List<Log> findLastLogs();
}