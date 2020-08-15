package com.example.xhbblog.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@ToString
public class BannedInfo implements Serializable {

    private static final long serialVersionUID = -8237050211700800754L;

    private Integer id;

    @NotBlank
    private String reason;
}