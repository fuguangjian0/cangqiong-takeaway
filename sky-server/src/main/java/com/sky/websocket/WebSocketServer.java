package com.sky.websocket;

import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 付广建 2023/12/10 10:51
 */
@Component
@ServerEndpoint("/ws/{sid}")
public class WebSocketServer {
    // 存放会话对象
    private static Map<String, Session> sessionMap = new HashMap<>();

    // 连接建立成功调用的方法
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid){
        System.out.println("客户端: " + sid + "建立连接");
        sessionMap.put(sid, session);
    }

    /**
     * 收到客户端消息调用的方法
     * @param message
     * @param sid
     */
    @OnMessage
    public void onMessage(String message, @PathParam("sid") String sid){
        System.out.println("收到来自客户端: " + sid + "的消息:" + message);
    }

    /**
     * 连接关闭调用的方法
     * @param sid
     */
    @OnClose
    public void onClose(@PathParam("sid") String sid){
        System.out.println("连接断开: " + sid);
        sessionMap.remove(sid);
    }

    /**
     * 群发
     * @param message
     * @throws IOException
     */
    public void sendToAllClient(String message) {
        Collection<Session> sessions = sessionMap.values();
        for (Session session : sessions) {
            try {
                // 服务器向客户端发送消息
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}
