package com.example.xhbblog.Service;

import com.example.xhbblog.pojo.Picture;

import java.util.List;

public interface PictureService {
    public void add(Picture picture);
    public void delete(Integer id);
    public List<Picture> list();
    public Integer getId();
}
