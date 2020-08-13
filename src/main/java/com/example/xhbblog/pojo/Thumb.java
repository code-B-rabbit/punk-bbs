package com.example.xhbblog.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Thumb implements Serializable {

    private static final long serialVersionUID = 3690719127445252442L;

    private Integer id;

    private String address;

    private Integer aid;
}