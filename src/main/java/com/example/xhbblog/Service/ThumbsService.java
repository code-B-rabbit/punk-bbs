package com.example.xhbblog.Service;

import com.example.xhbblog.pojo.Thumbs;

import java.util.List;

public interface ThumbsService {
    public void insert(Thumbs thumbs);
    public void deleteThumb(Integer aid,String address);
    public void deleteThumb(Integer aid);
}
