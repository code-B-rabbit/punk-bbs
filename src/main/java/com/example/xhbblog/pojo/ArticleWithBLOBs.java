package com.example.xhbblog.pojo;

public class ArticleWithBLOBs extends Article {
    private String content;

    private String info;            //博客介绍

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info == null ? null : info.trim();
    }
}