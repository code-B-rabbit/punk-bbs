package com.example.xhbblog.Service;

import com.example.xhbblog.pojo.Thumbs;

public interface ThumbsService {
    public void insert(Thumbs thumbs);
    public void deleteThumb(Integer aid,String address);
    public Integer countOf(Integer aid);
    public Boolean isThumb(Integer aid,String address);
}
