<template>
    <!--
    <ContentField v-if="!userStore.pulling_info">
        <div class="row justify-content-md-center">
            <div class="col-3">
               
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
            -->
                <!-- <div style="text-align: center; margin-top: 20px; cursor: pointer;" @click="acwing_login">
                    <img width="30" src="https://cdn.acwing.com/media/article/image/2022/09/06/1_32f001fd2d-acwing_logo.png" alt="">
                    <br>
                    AcWing一键登录
                </div> -->
            <!-- </div>
        </div>
    </ContentField> -->

    <main class="mt-0 main-content main-content-bg" >
    <section>
      <div class="page-header min-vh-75">
        <div class="container">
            <div class="row mt-lg-n1 mt-md-n11 mt-n10 justify-content-center">
      <div class="mx-auto col-xl-4 col-lg-5 col-md-7">
        <div class="card z-index-0">
          <div class="pt-4 text-center card-header">
                  <h3 class="font-weight-bolder text-success text-gradient">
                    Welcome back
                  </h3>
                  <p class="mb-0">Enter your Username and password to sign in</p>
                </div>

                
        <div class="card-body">
           <form @submit.prevent="login" >
             <div class="mb-3">
               <input
                 id="username"
                 class="form-control"
                 type="text"
                 placeholder="Name"
                 aria-label="username"
                 v-model="username"
                 
               />
             </div>
             <div class="mb-3">
               <input
                 class="form-control"
                 id="password"
                 type="password"
                 placeholder="Password"
                 aria-label="Password"
                 v-model="password"
               />
             </div>
             

              <MessageCard :title="error.error_message" class="error-message" >
     
              </MessageCard>
             <div class="text-center">
               
               <soft-button
                 type="submit"
                 color="dark"
                 full-width
                 variant="gradient"
                 class="my-4 mb-2"
                 >Sign up</soft-button
               >
             </div>
           </form> 
         </div>
                <div class="px-1 pt-0 text-center card-footer px-lg-2">
                  <p class="mx-auto mb-4 text-sm">
                    Don't have an account?
                    <router-link
                      :to="{ name: 'user_account_register' }"
                      class="text-success text-gradient font-weight-bold"
                      >Sign up</router-link
                    >
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  </main>

  

</template>


<script>
// import ContentField from '../../../components/ContentField.vue'
import {useUserStore} from '@/store/modules/user'
//import { storeToRefs } from 'pinia'
import {setToken,getToken} from '@/utils/auth'
import { ref ,reactive} from 'vue';
import MessageCard from "@/components/MessageCard.vue";
import SoftButton from "@/components/SoftButton.vue";
import router from '../../../router/index'
import $ from 'jquery'
export default {
    components: {
        // ContentField
        MessageCard,
        SoftButton,
    },
    setup(){
        const userStore = useUserStore()
        let username = ref('');
        let password = ref('');
       
        const error = reactive({
        
        error_message: "",
    })
        const jwt_token = getToken();
        
        //console.log(jwt_token)
        if(jwt_token){
            // commit 调用user里面的mutations函数  同步
            //dispatch action 异步
            setToken(jwt_token);
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
            error.error_message = "";
            // dispatch 调用user.js
            const userInfo = {username: username.value,
                password: password.value,}
            userStore.login(userInfo).then(() => {
                        userStore.getInfo().then(() => {
                            //console.log(userStore.is_login)
                            router.push({ name: 'home' });
                        }).catch(()=>{
                            error.error_message="用户名或密码错误"
                        })
                        
             }).catch(() => {
                    
            })

            
        }

        return {
            username, 
            password,
            error,
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
