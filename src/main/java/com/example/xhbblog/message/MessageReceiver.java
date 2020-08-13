package com.example.xhbblog.message;

import com.example.xhbblog.websocket.WebSocketServer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serializable;

@Component
@Slf4j
public class MessageReceiver{

    @Autowired
    private WebSocketServer webSocketServer;

    //序列化对象（特别注意：发布的时候需要设置序列化；订阅方也需要设置序列化）
    public void getMessage(String object) throws IOException {
        Jackson2JsonRedisSerializer seria = new Jackson2JsonRedisSerializer(WebSocketMessage.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        seria.setObjectMapper(objectMapper);
        WebSocketMessage webSocketMessage = (WebSocketMessage)seria.deserialize(object.getBytes());
        log.info("收到消息{}",webSocketMessage);
        webSocketServer.sendMessage(webSocketMessage.getUid(), webSocketMessage.getMessage());
    }
}
