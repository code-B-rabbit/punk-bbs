package com.example.xhbblog.service;

import com.example.xhbblog.pojo.Picture;

import java.util.List;

/**
 * 博客图片业务接口
 */
public interface PictureService {
    public void add(Picture picture);
    public void delete(Integer id);
    public List<Picture> list();
    public Integer getId();
    public Integer count();
}
