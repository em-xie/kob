<template>
        <ContentField>
        <div class="row justify-content-md-center">
            <div class="col-3">
                <!--submit.prevent阻止掉默认行为  -->
                <form @submit.prevent="UserRegister">
                    <div class="mb-3">
                        <label for="username" class="form-label">用户名</label>
                        <input v-model="username" type="text" class="form-control" id="username" placeholder="请输入用户名">
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">密码</label>
                        <input v-model="password" type="password" class="form-control" id="password" placeholder="请输入密码">
                    </div>
                    <div class="mb-3">
                        <label for="confirmedPassword" class="form-label">确认密码</label>
                        <input v-model="confirmedPassword" type="password" class="form-control" id="confirmedPassword" placeholder="请输入密码">
                    </div>
                    <div class="error-message">{{ error_message }}</div>
                    <button type="submit" class="btn btn-primary">注册</button>
                </form>
            </div>
        </div>
    </ContentField>
</template>

<script>
import ContentField from '../../../components/ContentField'
import { ref } from 'vue';
import router from '../../../router/index'
import {register} from '@/api/login'
export default {
    components: {
        ContentField
    },
    setup(){
        let username = ref('');
        let password = ref('');
        let confirmedPassword = ref('');
        let error_message = ref('');

        const UserRegister = () => {
            const data = {
                    username: username.value,
                    password: password.value,
                    confirmedPassword: confirmedPassword.value,
                }
          
            return new Promise((resolve, reject) => {
            register(data).then(res => {
                 // 成功直接返回登录界面
                 if (res.error_message === "success") {
                        router.push({name: "user_account_login"});
                    } else {
                        error_message.value = res.error_message;
                    }
              resolve()
            }).catch(error => {
              reject(error)
            })
          })
    }
        
        
        return {
            username,
            password,
            confirmedPassword,
            error_message,
            UserRegister,
        }
    }
}

</script>


<style scoped> 

button {
    width: 100%;
}

</style>