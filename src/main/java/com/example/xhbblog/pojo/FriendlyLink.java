package com.example.xhbblog.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class FriendlyLink implements Serializable {
    private static final long serialVersionUID = 7511131561369700376L;

    private Integer id;

    private String name;

    private String link;

    private Boolean allowed=false;         //默认为负

    private String email;

    private Date createTime;

    private Date updateTime;
}