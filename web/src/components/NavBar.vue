<template>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
  <div class="container">
    <!--router-link实现点击、图标不刷新  -->
    <router-link class="navbar-brand" :to="{name: 'home'}">King Of Bots</router-link>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarText" aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarText">
      <ul class="navbar-nav me-auto mb-2 mb-lg-0">
        <li class="nav-item">
          <router-link :class="route_name == 'pk_index' ? 'nav-link active' : 'nav-link'" :to="{name: 'pk_index'}">对战</router-link>
        </li>
        <li class="nav-item">
          <router-link :class="route_name == 'record_index' ? 'nav-link active' : 'nav-link'" :to="{name: 'record_index'}">对局列表</router-link>
        </li>
        <li class="nav-item">
          <router-link :class="route_name == 'ranklist_index' ? 'nav-link active' : 'nav-link'" :to="{name: 'ranklist_index'}">排行榜</router-link>
        </li>
        <li class="nav-item">
          <router-link :class="route_name == 'extern_index' ? 'nav-link active' : 'nav-link'" :to="{name: 'extern_index'}">扩展</router-link>
        </li>
      </ul>
      <ul class="navbar-nav" v-if="is_login">
        <li class="nav-item dropdown">
          <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
            {{ username }}
          </a>
          <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
            <li>

                <router-link class="dropdown-item" :to="{name: 'user_bot_index'}">我的Bot</router-link>
            </li>
            <li><hr class="dropdown-divider"></li>
            <li><a class="dropdown-item" href="#" @click="logout">退出</a></li>
          </ul>
        </li>
      </ul>
      <!-- v-else-if="!$store.state.user.pulling_info" -->
      <ul  class="navbar-nav" v-else-if="!pulling_info">
        <li class="nav-item">
          <router-link class="nav-link" :to="{name: 'user_account_login' }" role="button">
            登录
          </router-link>
        </li>
        <li class="nav-item">
          <router-link class="nav-link" :to="{name: 'user_account_register'}" role="button">
            注册
          </router-link>
        </li>
      </ul>


      <ul class="navbar-nav ">
        <div class="space">
        </div>
      </ul>
    </div>
  </div>
</nav>

</template>

<script>
import { useRoute } from 'vue-router'
import { computed } from 'vue'
import {useUserStore} from '@/store/modules/user'
import { storeToRefs } from 'pinia'
export default {
    setup() {
        const userStore = useUserStore()
        const { is_login, pulling_info ,username } = storeToRefs(userStore)
        const { logOut } = userStore
        const route = useRoute();
        let route_name = computed(() => route.name)
        // const jwt_token = getToken();
        // if(jwt_token){
        //     // commit 调用user里面的mutations函数  同步
        //     //dispatch action 异步

        //     userStore.getInfo().then(() => {
        //             router.push({ name: 'home' });
        //             userStore.updatePullingInfo(false);
        //                 }).catch(()=>{
        //             userStore.updatePullingInfo(false);
        //         })
        // }else {
        //     userStore.updatePullingInfo(false);
        // }

        const logout = () => {
            logOut();
        }

        return {
            route_name,
            logout,
            logOut,
            is_login,
            pulling_info,
            username


        }
    }
}
</script>



<style scoped>
/* .navbar-nav{
  margin-left:40px ;
} */
.space{
  margin-right: 50px;
}
</style>