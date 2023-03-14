package com.kob.job.consumer;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;

import com.kob.job.domain.CallJob;
import com.kob.job.mapper.CallJobMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/websocket/call/{token}")  // 注意不要以'/'结尾
public class CallWebSocketServer {

    private CallJob callJob;
    public static CallJobMapper callJobMapper;

private Integer userId;
    public final static ConcurrentHashMap<Integer, CallWebSocketServer> users = new ConcurrentHashMap<>();
//    private final static CopyOnWriteArraySet<User> matchpool = new CopyOnWriteArraySet<>();
    private Session session = null;

    @Autowired
    public void setCallJobMapper(CallJobMapper callJobMapper) {
        CallWebSocketServer.callJobMapper = callJobMapper;
    }







    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        // 建立连接
        this.session = session;

        //Integer userId = JwtAuthentication.getUserId(token);
        //token = StpUtil.getTokenValue();
        if(!token.isEmpty()) {
            String loginIdByToken1 = (String)StpUtil.getLoginIdByToken(token);

            Integer loginIdByToken = Integer.valueOf(loginIdByToken1);

            Integer userId = loginIdByToken;

            this.userId = userId;
        }


        if (userId != null) {
            users.put(userId, this);
            //System.out.println("connected！");
            //users.get(userId).sendMessage("success");
        } else {
            this.session.close();
        }
    }

    @OnClose
    public void onClose() {
        // 关闭链接
        //System.out.println("disconnected！");
//        if (this.user != null) {
//            users.remove(this.user.getId());
////            matchpool.remove(this.user);
//        }
    }










    @OnMessage
    public void onMessage(String message, Session session) {
        // 从Client接收消息
        //System.out.println("receive message！" + message);
//        JSONObject data = JSONObject.parseObject(message);
//        String event = data.getString("event");
//        if ("start-matching".equals(event)) {
//            startMatching(data.getInteger("bot_id"));
//        } else if ("stop-matching".equals(event)) {
//            stopMatching();
//        }else  if("move".equals(event)){
//            move(data.getInteger("direction"));
//        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    public void sendMessage(String message) {
        synchronized(this.session) {
            try {
                this.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
