package com.example.xhbblog.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class ArticleWithBLOBs extends Article implements Serializable {
    private static final long serialVersionUID = 5824881753723147781L;

    private String content;

    private String info;
}