package com.dqy.englishstudyapi.websocket;

import com.alibaba.fastjson.JSONObject;
import com.dqy.englishstudyapi.entity.TalkMessage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
@ServerEndpoint(value = "/websocket")
@Component
public class MyWebSocket {
//    @Autowired
//    private UserService userService   = new UserServiceImpl();
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
//    private static  CopyOnWriteArraySet<MyWebSocket> webSocketSet = new CopyOnWriteArraySet<MyWebSocket>();
    private  static  CopyOnWriteArraySet<Map<Integer,MyWebSocket>>  webSocketSet  = new CopyOnWriteArraySet<Map<Integer,MyWebSocket>>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    //连接的用户
     Map<String,Object> simpleUser= new HashMap<>();
    //当前封装session
    Map<Integer,MyWebSocket> this__=new HashMap<>();
    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {

        //getQueryString把url中？后面的所有的串儿都取出来
        String QueryString = session.getQueryString();
//        System.out.println(QueryString);
        try {
            QueryString = URLDecoder.decode(QueryString,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        System.out.println(QueryString);
        String[] param= QueryString.substring(QueryString.indexOf("=")+1).split("@@@");
        this.simpleUser.put("id",Integer.valueOf(param[0]));
        this.simpleUser.put("name",param[1]);
        this.simpleUser.put("headimg",param[2]);
        this.simpleUser.put("sex",param[3]);
        this.session = session;
        this__.put(Integer.valueOf(param[0]),this);
        //加入set中
        webSocketSet.add(this__);
        addOnlineCount();           //在线数加1
        System.out.println("有新连接加入（用户："+this.simpleUser.get("name")+"）！当前在线人数为" + getOnlineCount());
//        try {
//            sendMessage("8888");
//        } catch (IOException e) {
//            System.out.println("IO异常");
//        }

    }
 
    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this__);  //从set中删除
        subOnlineCount();           //在线数减1
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端的消息:" + message);

        //        //群发消息
//        for (MyWebSocket item : webSocketSet) {
//            try {
//                item.sendMessage(message);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }
    ///给特定的用户发消息
    private void sendMessageForUser(TalkMessage talkMessage) {
        for (Map<Integer, MyWebSocket> item : webSocketSet) {
            if ( item.containsKey(talkMessage.getTouserid())){
                JSONObject jo= (JSONObject) JSONObject.toJSON(talkMessage);
                try {
                    item.get(talkMessage.getTouserid()).session.getBasicRemote().sendText(jo.toJSONString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }
 
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
        //this.session.getAsyncRemote().sendText(message);
    }
 
//    public static void sendInfo(String message) throws IOException {
//        for (MyWebSocket item : webSocketSet) {
//            try {
//                item.sendMessage(message);
//            } catch (IOException e) {
//                continue;
//            }
//        }
//    }
 
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }
 
    public static synchronized void addOnlineCount() {
        MyWebSocket.onlineCount++;
    }
 
    public static synchronized void subOnlineCount() {
        MyWebSocket.onlineCount--;
    }
}