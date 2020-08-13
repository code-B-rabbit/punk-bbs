package com.example.xhbblog.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Tag implements Serializable {
    private static final long serialVersionUID = -1459598898209192855L;

    private Integer id;

    private String name;

    private Integer numbersOfBlog;     //该标签的博客数量

    private Date createTime;

    private Date updateTime;

}