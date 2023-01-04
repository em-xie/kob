
<template>
    <div class="matchGround">
        <div class="row">
            <div class="col-4">
                <div class="user-photo">
                    <img :src="photo" alt="">
                </div>
                <div class="user-name">
                    {{username }}
                </div>
            </div>

            <div class="col-4">
                <div class="user-select-bot">
                    <select class="form-select" aria-label="Default select example" v-model="select_bot">
                        <option value="-1" selected>亲自出马</option>
                        <option v-for="bot in bots" :key="bot.id" :value="bot.id">{{ bot.title }}</option>
                    </select>
                </div>
            </div>



            <div class="col-4">

                <div class="user-photo">
                    <img :src="opponent_photo" alt="">
                </div>
                <div class="user-name">
                    {{ opponent_username }}
                </div>
            </div>
            <div class="col-12" style="text-align:center ;padding-top: 15vh;">
                <button @click="click_match_btn" type="button" class="btn btn-warning btn-lg">{{ match_btn_info }}</button>
            </div>
        </div>
    </div>
</template>



<script>

import {ref} from 'vue'
import {getlist} from '@/api/bot/bot'
import { pkStore } from '@/store/modules/pk'
import {useUserStore} from '@/store/modules/user'
import {recordStore} from '@/store/modules/record'
import { storeToRefs } from 'pinia'
export default {
    setup() {
        const userStore = useUserStore()
        const pkstore = pkStore();
        const rstore = recordStore();
        const { opponent_photo,opponent_username} = storeToRefs(pkstore)
        const { photo,username } = storeToRefs(userStore)
        let bots = ref([]);
        let select_bot = ref("-1");
        let match_btn_info = ref("开始匹配");
        const click_match_btn = () => {
            if(match_btn_info.value==="开始匹配"){
                match_btn_info.value="取消";
                pkstore.socket.send(JSON.stringify({
                    event: "start-matching",
                    bot_id: select_bot.value,
                }));
            }else{
                match_btn_info.value = "开始匹配";
                pkstore.socket.send(JSON.stringify({
                    event: "stop-matching",
                }));
            }
        };
        const refresh_bots = () => {
            return new Promise((resolve, reject) => {
                getlist().then(res => {
                bots.value = res;
                resolve(res)
            }).catch(error => {
                reject(error)
            })
        })
            
        };
        refresh_bots();



        return {
            match_btn_info,
            click_match_btn,
            bots,
            select_bot,
            pkstore,
            userStore,
            rstore,
            opponent_photo,
            opponent_username,
            username,
            photo
        }
    }

}
</script>

<style scoped>
div.matchGround {
    /* 浏览器的宽高 */
    width: 60vw;
    height: 70vh;
    background-color: rgba(50,50,50,50);
    /* 居中 */
    margin: 40px auto;
}
div.user-photo{
    text-align:  center;
    padding-top: 10vh;
}
div.user-photo >img {
    border-radius: 50%;
    width: 20vh;
}

div.user-name{
    text-align: center;
    font-size: 24px;
    font-weight: 600;
    color:white;
    padding-top:2vh;
}


div.user-select-bot {
    padding-top: 20vh;
}
div.user-select-bot > select {
    width: 60%;
    margin: 0 auto;
}
</style>