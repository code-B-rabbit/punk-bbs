package com.example.xhbblog.message;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class WebSocketMessage implements Serializable {
    private static final long serialVersionUID = 2715005087906344399L;
    Integer uid;
    String message;

    public WebSocketMessage() {
    }
}
