package com.example.xhbblog.Service;

import com.example.xhbblog.pojo.Log;

import java.util.List;

/**
 * 访客业务接口
 */
public interface LogService {
    public void writeDate();  //写入数据库
    public void checkAndAdd(String ipAddr);  //将用户ip写入到缓存
    public List<Log> findLastLogs();
}
