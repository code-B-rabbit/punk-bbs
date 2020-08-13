package com.example.xhbblog.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class TimeLine implements Serializable {
    private static final long serialVersionUID = 3016128787458018145L;
    private Date time;         //哪一天
    private List<Article> articleList;
}
