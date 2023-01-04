package com.ruoyi.common.websocket;

import lombok.Data;

import javax.websocket.Session;

/**
 * @作者：xie
 * @时间：2022/9/19 20:37
 */

@Data
public class WebSocketClient {

    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    //连接的uri
    private String uri;

}
