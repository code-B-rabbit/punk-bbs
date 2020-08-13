package com.example.xhbblog.service.impl;

import com.example.xhbblog.service.PictureService;
import com.example.xhbblog.mapper.PictureMapper;
import com.example.xhbblog.pojo.Picture;
import com.example.xhbblog.pojo.PictureExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(isolation= Isolation.READ_COMMITTED)
public class PictureServiceImpl implements PictureService {

    @Autowired
    private PictureMapper pictureMapper;

    @Override
    public void add(Picture picture) {
        pictureMapper.insert(picture);
    }

    @Override
    public void delete(Integer id) {
        pictureMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<Picture> list() {
        PictureExample example=new PictureExample();
        example.setOrderByClause("id desc");
        return pictureMapper.selectByExample(example);
    }

    @Override
    public Integer getId() {
        return pictureMapper.getId();
    }

    @Override
    public Integer count() {
        return pictureMapper.count();
    }
}
