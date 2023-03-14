<template>
   
   <div class="page-header min-vh-75">
    <div class="container">
    <div class="row mt-lg-n1 mt-md-n11 mt-n10 justify-content-center">
      <div class="mx-auto col-xl-4 col-lg-5 col-md-7">
        <div class="card z-index-0">
          <div class="pt-4 text-center card-header">
            <h5>Register with</h5>
    </div>
          <div class="card-body">
            <form @submit.prevent="UserRegister" :v-model="registers">
              <div class="mb-3">
                <input
                  id="username"
                  class="form-control"
                  type="text"
                  placeholder="Name"
                  aria-label="username"
                  v-model="registers.username"
                  
                />
              </div>
              <div class="mb-3">
                <input
                  class="form-control"
                  id="password"
                  type="password"
                  placeholder="Password"
                  aria-label="Password"
                  v-model="registers.password"
                />
              </div>
              <div class="mb-3">
                <input
                  id="confirmedPassword"
                  class="form-control"
                  type="password"
                  placeholder="confirmedPassword"
                  aria-label="Password"
                  v-model="registers.confirmedPassword"
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
              <p class="text-sm mt-3 mb-0">
                Already have an account?
                <router-link
                  :to="{ name: 'user_account_login' }"
                  class="text-dark font-weight-bolder"
                >
                  Sign in
                </router-link>
              </p>
            </form>

          
            
          </div>
          
        </div>
        
      </div>
    </div>
  </div>

</div>

</template>

<script>
//import ContentField from '../../../components/ContentField'
import { ref,reactive } from 'vue';
import router from '../../../router/index'
import {register} from '@/api/login'


import MessageCard from "@/components/MessageCard.vue";
import SoftButton from "@/components/SoftButton.vue";
export default {
    components: {
        //ContentField,
        MessageCard,
        SoftButton,
        //SoftCheckbox,
        
    },
    setup(){
        // let username = ref('');
        // let password = ref('');
        // let confirmedPassword = ref('');
        
        const error = reactive({
        
        error_message: "",
    })
        
        const registers = ref({
            username: "",
            password: "",
            confirmedPassword: "",  
        })
    
        const UserRegister = () => {
       

 
            
            return new Promise((resolve, reject) => {

            register(registers.value).then(res => {
                 // 成功直接返回登录界面
                 if (res.error_message === "success") {
                        router.push({name: "user_account_login"});
                    } else {
                        error.error_message = res.error_message;
                   
                    }
              resolve()
            }).catch(error => {
              reject(error)
            })
          })
    }
        
        
        return {
            // username,
            // password,
            // confirmedPassword,
            
            registers,
            UserRegister,
            error
        }
    }
}

</script>


<style scoped> 

button {
    width: 100%;
}

.error-message{
    color: red;
}



</style>