package com.kob.backend.consumer.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @作者：xie
 * @时间：2022/11/12 9:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    private Integer id;
    private Integer botId;  // -1表示亲自出马，否则表示用AI打
    private String botCode;
    private Integer sx;
    private Integer sy;


    // 蛇每个球的方向
    private List<Integer> steps;

    private boolean check_tail_increasing(int step){ //校验当前回合，蛇的长度是否增加
        if(step <= 10){

            return  true;
        }
        return step % 3 == 1;
    }

    public List<Cell> getCells(){
        List<Cell> res = new ArrayList<>();
        int[] dx = {-1,0,1,0} , dy={0,1,0,-1};

        int x = sx,y = sy;
        int step = 0;
        res.add(new Cell(x,y));
        for(int d: steps){
            x += dx[d];
            y += dy[d];
            /**
             * 每一步移动都会把蛇头移动到下一个格子(注：蛇头有两个cell，详看前端Snake.js的next_step()与update_move()逻辑)，
             * 若当前长度增加，蛇头正好移到新的一个格子，剩下的蛇身长度不变，因此长度 + 1；若长度不增加，则删除蛇尾
             */
            res.add(new Cell(x,y));
            if(!check_tail_increasing( ++step)){
                /**
                 * 关键：
                 * 为什么此处删除0呢，首先存储蛇身、且判定是否增加、且画蛇的逻辑此时还是在前端，我们只是将
                 * 判断蛇是否撞到 墙和蛇身 移到后端。并且我们在后端保存的是是蛇头的x、y坐标和蛇身相对
                 * 于上一步操作的方向，但是在我们做了第一个操作后蛇尾才是蛇头，意思就是res逆序才是蛇
                 * 头到蛇尾的位置！
                 */
                res.remove(0);
            }
        }
        return  res;
    }

    public String getStepsString() {
        StringBuilder res = new StringBuilder();
        for(int d : steps) {
            res.append(d);
        }
        return res.toString();
    }
}
