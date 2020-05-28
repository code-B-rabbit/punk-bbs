package com.example.xhbblog.Service.impl;

import com.example.xhbblog.Service.ThumbsService;
import com.example.xhbblog.mapper.ThumbsMapper;
import com.example.xhbblog.pojo.Thumbs;
import com.example.xhbblog.pojo.ThumbsExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@EnableScheduling
public class ThumbsServiceImpl implements ThumbsService {

    @Autowired
    private ThumbsMapper thumbsMapper;

    @Override
    public void insert(Thumbs thumbs) {
        thumbsMapper.insert(thumbs);
    }


    @Override
    public void deleteThumb(Integer aid, String address) {
        thumbsMapper.deleteThumb(aid,address);
    }

    @Override
    public void deleteThumb(Integer aid) {
        thumbsMapper.deleteThumbByAid(aid);
    }


}
