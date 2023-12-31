package com.kob.backend.service.Imp.pk;

import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.service.pk.StartGameService;
import org.springframework.stereotype.Service;

/**
 * @作者：xie
 * @时间：2022/11/13 14:16
 */
@Service
public class StartGameServiceImpl implements StartGameService {
    @Override
    public String startGame(Integer aId,Integer aBotId, Integer bId,Integer bBotId) {
        //System.out.println("start game: " + aId + " " + bId);
        WebSocketServer.startGame(aId,aBotId,bId,bBotId);
        return "start game success";
    }
}
