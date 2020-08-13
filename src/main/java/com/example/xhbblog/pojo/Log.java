package com.example.xhbblog.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Log implements Serializable {
    private static final long serialVersionUID = -3540387666405041638L;

    private Integer id;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    private Date createTime;

    private Long visit;

    private Date updateTime;
}