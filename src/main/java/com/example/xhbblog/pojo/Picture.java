package com.example.xhbblog.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Picture implements Serializable {
    private static final long serialVersionUID = 5985966843968079008L;

    private Integer id;

    private String filekey;

    private String url;
}