package com.example.xhbblog.service;

import com.example.xhbblog.pojo.Thumb;

/**
 * 点赞业务接口
 */
public interface ThumbsService {
    public void insert(Thumb thumbs);
    public void deleteThumb(Integer aid,String address);
    public void redisDataToMySQL();
}
