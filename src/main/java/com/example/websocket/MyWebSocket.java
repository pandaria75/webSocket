package com.example.websocket;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * websocket实现类
 */
@ServerEndpoint("/websocket")
@Component
public class MyWebSocket {
//    用来存放每个客户端对应的WebSocket对象
    private static CopyOnWriteArraySet<MyWebSocket> webSockets = new CopyOnWriteArraySet<>();

//    与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    @OnOpen
    public void onOpen(Session session){
        this.session = session;
        webSockets.add(this);
        System.out.println("有新连接加入，当前在线人数为"+webSockets.size());
        this.session.getAsyncRemote().sendText("恭喜您连上websocket-->当前人数为："+webSockets.size());
    }

    @OnClose
    public void onClose(){
        webSockets.remove(this);
        System.out.println("有一连接关闭！当前人数为"+webSockets.size());
    }

    @OnMessage
    public void onMessage(String message, Session session){
        System.out.println("来自客户端的消息:" + message);
        broadcast(message);
    }

    @OnError
    public void onError(Session session, Throwable error){
        System.out.println("发生错误");
        error.printStackTrace();
    }

    public void broadcast(String message){
        for (MyWebSocket item:webSockets) {
            item.session.getAsyncRemote().sendText(message);
        }
    }
}
