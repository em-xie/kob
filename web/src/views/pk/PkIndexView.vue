<template>
    <PlayGround v-if="status === 'playing'"/>
    <MatchGround v-if="status === 'matching'" />
    <ResultBoard v-if="loser !== 'none'"></ResultBoard>
    <div class="user-color" v-if="status === 'playing' && parseInt(id) === parseInt(a_id)">左下角</div>
    <div class="user-color" v-if="status === 'playing' && parseInt(id) === parseInt(b_id)">右上角</div>

  </template>
  
  
  <script>
  import PlayGround from '../../components/PlayGround.vue'
  import MatchGround from '@/components/MatchGround.vue'
  import ResultBoard from '@/components/ResultBoard.vue'
  import { onMounted,onUnmounted } from 'vue';
  import { pkStore } from '@/store/modules/pk'
  import {useUserStore} from '@/store/modules/user'
  import {recordStore} from '@/store/modules/record'
import { storeToRefs } from 'pinia'
  export default {
      components: {
          PlayGround,
          MatchGround,
          ResultBoard
      },
      setup(){
        
        const userStore = useUserStore()
        const store = pkStore();
        const rstore = recordStore();
        const { status, loser ,a_id,b_id} = storeToRefs(store)
       
        const { id } = storeToRefs(userStore)
        //console.log(id)
        store.updateLoser("none");
        rstore.updateIsRecord("false");
        //字符串中有${}表达式操作的话要用``，不能用引号
       // const socketUrl = `ws://localhost:3000/kob/websocket/${userStore.token}/`;
        const socketUrl = `wss://app3943.acapp.acwing.com.cn/kob/websocket/${userStore.token}/`;

        let socket = null;
        onMounted(() => { 
            store.updateOpponent({
                username: "我的对手",
                photo: "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png"
            })
            //调用updateOpponent函数 pk.js
            // store.commit("updateOpponent",{
            //     username: "我的对手",
            //     photo: "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png"
            // })

            //当当前页面打开时调用
            socket = new WebSocket(socketUrl); //js自带的WebSocket()
            socket.onopen = () => { //连接成功时调用的函数
                //console.log("connected!");

                store.updateSocket(socket);
            }

            socket.onmessage = msg => { //前端接收到信息时调用的函数
                //console.log(msg.data)
                const data = JSON.parse(msg.data); //不同的框架数据定义的格式不一样
                //console.log(data.gamemap)
                if (data.event === "start-matching") { //这个这个start-matching是respA或respB返回的
                    //匹配成功,更新对手信息
                    store.updateOpponent({
                        username: data.opponent_username,
                        photo: data.opponent_photo,
                    })
                    // store.commit("updateOpponent",{
                    //     username: data.opponent_username,
                    //     photo: data.opponent_photo,
                    // });
                    setTimeout(()=>{
                        store.updateStatus("playing");
                    },2000); //延时函数，单位是毫秒 
                    store.updateGame(data.game);
                }else if(data.event ==="move"){
                    const game = store.gameObject;
//console.log(data.a_direction)
                    const [snake0, snake1] = game.Snakes;
                    snake0.set_direction(data.a_direction);
                    snake1.set_direction(data.b_direction);
                }else if(data.event ==="result"){
                    const game = store.gameObject;
                    //console.log(game)
                    const [snake0, snake1] = game.Snakes;
                   // console.log(data.loser);
                    if(data.loser ==="all" || data.loser==="A"){
                        snake0.status = "die";
                    }
                    if(data.loser ==="all" || data.loser==="B"){
                        snake1.status = "die";
                    }
                    store.updateLoser(data.loser);
                }
            }

            socket.onclose = () => { //关闭时调用的函数
                //console.log("disconnected!");
            }
        });

        onUnmounted(() => { //当当前页面关闭时调用
            socket.close(); //卸载的时候断开连接
            store.updateStatus("matching");
        });

        return {
            userStore,
            pkStore,
            rstore,
            status,
            loser,
            a_id,
            b_id,
            id

        }
      }
  }
  </script>
  
  <style scoped> 
  div.user-color {
    text-align: center;
    color: white;
    font-size: 30px;
    font-weight: 600;
}

  </style>