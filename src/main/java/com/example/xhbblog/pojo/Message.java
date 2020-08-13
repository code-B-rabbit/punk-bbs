package com.example.xhbblog.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Message implements Serializable {
    private static final long serialVersionUID = 222827663542645867L;

    private Integer id;

    private Date createTime;

    private String content;

    private Date updateTime;
}