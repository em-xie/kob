<template>
    <ContentField v-if="!userStore.pulling_info">
        <div class="row justify-content-md-center">
            <div class="col-3">
                <!--submit.prevent阻止掉默认行为  -->
                <form @submit.prevent="login">
                    <div class="mb-3">
                        <label for="username" class="form-label">用户名</label>
                        <input v-model="username" type="text" class="form-control" id="username" placeholder="请输入用户名">
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">密码</label>
                        <input v-model="password" type="password" class="form-control" id="password" placeholder="请输入密码">
                    </div>
                    <div class="error-message">{{ error_message }}</div>
                    <button type="submit" class="btn btn-primary">提交</button>
                </form>
                <div style="text-align: center; margin-top: 20px; cursor: pointer;" @click="acwing_login">
                    <img width="30" src="https://cdn.acwing.com/media/article/image/2022/09/06/1_32f001fd2d-acwing_logo.png" alt="">
                    <br>
                    AcWing一键登录
                </div>
            </div>
        </div>
    </ContentField>
</template>

<script>
import ContentField from '../../../components/ContentField.vue'
import {useUserStore} from '@/store/modules/user'
//import { storeToRefs } from 'pinia'
import {getToken} from '@/utils/auth'
import { ref } from 'vue';
import router from '../../../router/index'
import $ from 'jquery'
export default {
    components: {
        ContentField
    },
    setup(){
        const userStore = useUserStore()
        let username = ref('');
        let password = ref('');
        let error_message = ref('');

        const jwt_token = getToken();
        if(jwt_token){
            // commit 调用user里面的mutations函数  同步
            //dispatch action 异步

            userStore.getInfo().then(() => {
                    router.push({ name: 'home' });
                    userStore.updatePullingInfo(false);
                        }).catch(()=>{
                    userStore.updatePullingInfo(false);
                })
        }else {
            userStore.updatePullingInfo(false);
        }
        const acwing_login = () => {
            $.ajax({
                url: "http://127.0.0.1:3000/kob/user/account/acwing/web/apply_code/",
                type: "GET",
                success: resp => {
                    if(resp.result === "success") {
                        window.location.replace(resp.apply_code_url)
                    }
                }
            });
        }


        const login = () => {
            error_message.value = "";
            // dispatch 调用user.js
            const userInfo = {username: username.value,
                password: password.value,}
            userStore.login(userInfo).then(() => {
                        userStore.getInfo().then(() => {
                            //console.log(userStore.is_login)
                            router.push({ name: 'home' });
                        }).catch(()=>{

                        })
                        
             }).catch(() => {
                    error_message.value="用户名或密码错误";
            })

            
        }

        return {
            username, 
            password,
            error_message,
            login,
            acwing_login,
            userStore

        }
    }
}
</script>

<style scoped>
button {
    width: 100%;
}
div.error-message {
    color: red;
}
</style>
