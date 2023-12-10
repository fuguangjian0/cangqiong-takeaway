package com.sky.task;

import com.sky.websocket.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author 付广建 2023/12/10 10:58
 */
//@Component
public class WebSocketTask {
    @Autowired
    private WebSocketServer webSocketServer;

    @Scheduled(cron = "0/5 * * * * ?")
    public void sendMessageToClient() throws IOException {
        webSocketServer.sendToAllClient("这是来自服务端的消息:" + DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now()));
    }

}
