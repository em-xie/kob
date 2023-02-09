<template>
  <NavBar></NavBar>
  <router-view></router-view>


  <!--信息提示栏-->
<div class="position-fixed top-50 start-50 translate-middle p-3" style="z-index: 11">
    <div id="info-toast" class="toast align-items-center" role="alert" aria-live="assertive" aria-atomic="true">
        <div class="d-flex">
            <div class="toast-body">
                <!--成功信息-->
                <span id="toast-info-success" class="text-success"></span>
            </div>
            <button type="button" class="btn-close me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
        </div>
    </div>
</div>
</template>
<script>
import NavBar from './components/NavBar.vue'
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap"
import {useUserStore} from '@/store/modules/user'
import {showStore} from '@/store/modules/show'
import { onMounted,onUnmounted } from 'vue';
import $ from 'jquery'
import bootstrap from 'bootstrap/dist/js/bootstrap'
export default {
  components: {
    NavBar
  },

  setup(){
    const userStore = useUserStore()
    const store = showStore();
    let socket = null;
   
 //字符串中有${}表达式操作的话要用``，不能用引号
    const socketUrl = `ws://localhost:3000/kob/websocket/call/${userStore.token}/`;
    onMounted(() => { 

            //当当前页面打开时调用
            socket = new WebSocket(socketUrl); //js自带的WebSocket()
            socket.onopen = () => { //连接成功时调用的函数
                //console.log("connected!");
                store.updateSocket(socket);
               
            }

            socket.onmessage = msg => { //前端接收到信息时调用的函数
                //console.log(msg.data)
                const data = msg.data; //不同的框架数据定义的格式不一样
                //console.log(data)
               
                if(data === "success"){
                        $("#toast-info-success").text("预约时间到了");
                        // --- 显示提示栏
                        var toastBar = document.getElementById('info-toast');
                        var toast = new bootstrap.Toast(toastBar);
                        toast.show();
                 }
             
  
            }

            socket.onclose = () => { //关闭时调用的函数
                //console.log("disconnected!");
            }
        });

        onUnmounted(() => { //当当前页面关闭时调用
            socket.close(); //卸载的时候断开连接
            // store.updateStatus("matching");
        });


        return {
            userStore,
            
           

        }
  }
  
}
</script>

<style>
body {
  background-image: url("@/assets/images/Eminem.jpg") ;
  background-size: cover;
  
  
}
</style>
