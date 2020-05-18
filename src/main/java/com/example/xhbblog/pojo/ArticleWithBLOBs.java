package com.example.xhbblog.pojo;

import java.io.Serializable;

public class ArticleWithBLOBs extends Article implements Serializable {
    private String content;

    private String info;

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