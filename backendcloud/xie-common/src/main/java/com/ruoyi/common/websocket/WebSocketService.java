package com.ruoyi.common.websocket;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import com.ruoyi.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @作者：xie
 * @时间：2022/9/18 22:21
 */



@Slf4j
@Component
@ServerEndpoint(value = "/websocketServer/{userName}",encoders = {ServerEncoder.class})
public class WebSocketService{

    //concurrent包的线程安全Set，用来存放每个客户端对应的Session对象。
    private static ConcurrentHashMap<String, WebSocketClient> webSocketMap = new ConcurrentHashMap<>();

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    /**与某个客户端的连接会话，需要通过它来给客户端发送数据*/
    private Session session;

    /**接收userName*/
    private String userName="";


    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userName") String userName) {
        if(!webSocketMap.containsKey(userName))
        {
            addOnlineCount(); // 在线数 +1
        }
        this.session = session;
        this.userName= userName;
        WebSocketClient client = new WebSocketClient();
        client.setSession(session);
        client.setUri(session.getRequestURI().toString());
        webSocketMap.put(userName, client);

        log.info(">>>>>>>>>>>用户连接:"+userName+",当前在线人数为:" + getOnlineCount());
        try {
            sendMessage("来自后台的反馈：连接成功");
        } catch (IOException e) {
            log.error("用户:"+userName+",网络异常!!!!!!");
        }
    }

    /**
     * 连接服务器成功后主动推送
     */
    public void sendMessage(String message) throws IOException {
        synchronized (session){
            this.session.getBasicRemote().sendText(message);
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if(webSocketMap.containsKey(userName)){
            if(webSocketMap.size()>0)
            {
                subOnlineCount();  //在线数 -1
            }
            webSocketMap.remove(userName);  //从set中删除
        }
        log.info("<<<<<<<<<<用户退出:"+userName+",当前在线人数为:" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到用户消息:"+userName+",报文:"+message);
        //可以群发消息
        //消息保存到数据库、redis
        if(StringUtils.isNotBlank(message)){

        }
    }

    /**
     * 连接发生异常时候触发的方法
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误:"+this.userName+",原因:"+error.getMessage());
        error.printStackTrace();
    }



    /**
     * 通过 websocket 发送消息到客户端
     *
     * @param userNames    用户名集合
     * @param title        消息标题
     * @param content      消息内容
     * @param sendUserName 发送人
     */
    public void sendMessageByWebSocket(List<String> userNames, String title, String content, String sendUserName) {
        log.info("sendMessageByWebSocket");
        List<String> names = new ArrayList<>();
        userNames.forEach(key -> {
            names.add(key);
        });
        // 发送消息
        JSONObject jsonObject = new JSONObject();
        jsonObject.putOpt("title", title);
        jsonObject.putOpt("content", content);
        jsonObject.putOpt("from", sendUserName);
        sendMessageBatch(names, jsonObject.toString());
    }

    /**
     * 批量向客户端发送消息
     * @return
     */
    public  static void sendMessageBatch(List<String> userNames, String message){
        if (CollUtil.isEmpty(userNames)){
            return;
        }
        userNames.forEach(userName ->{
            try {
                WebSocketClient webSocketClient = webSocketMap.get(userName);
                if (webSocketClient != null){
                    webSocketClient.getSession().getBasicRemote().sendText(message);
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        });
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketService.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketService.onlineCount--;
    }

    public static void setOnlineCount(int onlineCount) {
        WebSocketService.onlineCount = onlineCount;
    }


    public static ConcurrentHashMap<String, WebSocketClient> getWebSocketMap() {
        return webSocketMap;
    }

    public static void setWebSocketMap(ConcurrentHashMap<String, WebSocketClient> webSocketMap) {
        WebSocketService.webSocketMap = webSocketMap;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
