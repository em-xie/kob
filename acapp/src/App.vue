<template>

      <div class="game-body">
          <MenuView v-if="$store.state.router.router_name === 'menu'" />
          <PkIndexViewVue v-else-if="$store.state.router.router_name === 'pk'" />
          <RecordIndexViewVue v-else-if="$store.state.router.router_name === 'record'" />
          <RecordContentViewVue v-else-if="$store.state.router.router_name === 'record_content'" />
          <RankListViewVue v-else-if="$store.state.router.router_name === 'ranklist'" />
          <UserBotIndexViewVue v-else-if="$store.state.router.router_name === 'user_bot'" />
       </div>   

  
</template>


<script>
import { useStore } from 'vuex';
import MenuView from './views/MenuView.vue';
import PkIndexViewVue from './views/pk/PkIndexView.vue';
import RecordIndexViewVue from './views/record/RecordIndexView.vue';
import RecordContentViewVue from './views/record/RecordContentView.vue';
import RankListViewVue from './views/ranklist/RanklistIndexView.vue';
import UserBotIndexViewVue from './views/user/bot/UserBotIndexView.vue';



export default {
  components: {
        MenuView,
        PkIndexViewVue,
        RecordIndexViewVue,
        RecordContentViewVue,
        RankListViewVue,
        UserBotIndexViewVue,
    },
  //eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzNDIyYTQ4NGEwZjA0NzMyYmNlZGIwM2Q0NGQ3ZGUwNyIsInN1YiI6IjEiLCJpc3MiOiJzZyIsImlhdCI6MTY2ODU5OTk3NiwiZXhwIjoxNjY5ODA5NTc2fQ.59ScUPcvM4BYay6fPYXD9MJxGyWIs6YVLoc44hbTWOI
  setup(){
        const store = useStore();
        const jwt_token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzNDIyYTQ4NGEwZjA0NzMyYmNlZGIwM2Q0NGQ3ZGUwNyIsInN1YiI6IjEiLCJpc3MiOiJzZyIsImlhdCI6MTY2ODU5OTk3NiwiZXhwIjoxNjY5ODA5NTc2fQ.59ScUPcvM4BYay6fPYXD9MJxGyWIs6YVLoc44hbTWOI";
        if(jwt_token){
            // commit 调用user里面的mutations函数  同步
            //dispatch action 异步
            store.commit("updateToken",jwt_token);
            store.dispatch("getinfo",{
                success() {
                    //router.push({ name: 'home' });
                    store.commit("updatePullingInfo", false);
                },
                error() {
                    store.commit("updatePullingInfo", false);
                }
            })
        }else {
            store.commit("updatePullingInfo", false);
        }
  }
}
</script>

<style scoped>
.div{
    margin:0;
}
div.game-body {
    width: 100%;
    height: 100%;
    background-image: url("@/assets/images/background.png");
    background-size: cover;
}

/* div.window {
    width: 100vw;
    height: 100vh;
} */
</style>
