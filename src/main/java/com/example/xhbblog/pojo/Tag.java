package com.example.xhbblog.pojo;

import java.io.Serializable;

public class Tag implements Serializable {
    private Integer id;

    private String name;

    private Integer numbersOfBlog;     //该标签的博客数量

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", numbersOfBlog=" + numbersOfBlog +
                '}';
    }

    public Integer getNumbersOfBlog() {
        return numbersOfBlog;
    }

    public void setNumbersOfBlog(Integer numbersOfBlog) {
        this.numbersOfBlog = numbersOfBlog;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
}