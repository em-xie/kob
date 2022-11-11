<template>
    <PlayGround v-if="$store.state.pk.status === 'playing'"/>
    <MatchGround v-if="$store.state.pk.status === 'matching'" />
  </template>
  
  
  <script>
  import PlayGround from '../../components/PlayGround.vue'
  import MatchGround from '@/components/MatchGround.vue'
  import { onMounted,onUnmounted } from 'vue';
  import { useStore } from 'vuex';
  export default {
      components: {
          PlayGround,
          MatchGround
      },
      setup(){
        
        const store = useStore();
        //字符串中有${}表达式操作的话要用``，不能用引号
        const socketUrl = `ws://127.0.0.1:8089/websocket/${store.state.user.token}/`;

        let socket = null;
        onMounted(() => { 
            //调用updateOpponent函数 pk.js
            store.commit("updateOpponent",{
                username: "我的对手",
                photo: "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png"
            })

            //当当前页面打开时调用
            socket = new WebSocket(socketUrl); //js自带的WebSocket()
            socket.onopen = () => { //连接成功时调用的函数
                console.log("connected!");
                store.commit("updateSocket",socket);
            }

            socket.onmessage = msg => { //前端接收到信息时调用的函数
                console.log(msg.data)
                const data = JSON.parse(msg.data); //不同的框架数据定义的格式不一样
                console.log(data.gamemap)
                if (data.event === "start-matching") { //这个这个start-matching是respA或respB返回的
                    //匹配成功,更新对手信息
                    store.commit("updateOpponent",{
                        username: data.opponent_username,
                        photo: data.opponent_photo,
                    });
                    setTimeout(()=>{
                        store.commit("updateStatus", "playing");
                    },2000); //延时函数，单位是毫秒 
                    store.commit("updateGampMap", data.gamemap);
                    
                }
            }

            socket.onclose = () => { //关闭时调用的函数
                console.log("disconnected!");
            }
        });

        onUnmounted(() => { //当当前页面关闭时调用
            socket.close(); //卸载的时候断开连接
            store.commit("updateStatus", "matching");
        });
      }
  }
  </script>
  
  <style scoped> 
  </style>