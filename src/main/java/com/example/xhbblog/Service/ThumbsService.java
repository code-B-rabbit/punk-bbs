package com.example.xhbblog.Service;

import com.example.xhbblog.pojo.Thumbs;

/**
 * 点赞业务接口
 */
public interface ThumbsService {
    public void insert(Thumbs thumbs);
    public void deleteThumb(Integer aid,String address);
    public void redisDataToMySQL();
}
