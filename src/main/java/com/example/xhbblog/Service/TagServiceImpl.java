package com.example.xhbblog.Service;


import com.example.xhbblog.mapper.TagMapper;
import com.example.xhbblog.pojo.Tag;
import com.example.xhbblog.pojo.TagExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Override
    public void add(Tag tag) {
        tagMapper.insertSelective(tag);
    }

    @Override
    public void delete(Integer id) {
        tagMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Tag tag) {
        tagMapper.updateByPrimaryKey(tag);
    }

    @Override
    public Tag get(Integer id) {
        return tagMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Tag> list() {List<Tag> tags=tagMapper.list();
        tags.sort(new Comparator<Tag>() {
            @Override
            public int compare(Tag o1, Tag o2) {
                return o2.getNumbersOfBlog()-o1.getNumbersOfBlog();
            }
        });
        return tags;            //使得标签按照文章数多少进行排序
    }
}
