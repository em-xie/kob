<template>
    <div>

    </div>
</template>

<script>
import router from '@/router/index';
import { useRoute } from 'vue-router';
import {useUserStore} from '@/store/modules/user'
import {setToken} from '@/utils/auth'
import $ from 'jquery';

export default {
    setup() {
        const myRoute = useRoute();
        const userStore = useUserStore()
        $.ajax({
            url: "https://app3943.acapp.acwing.com.cn/api/user/account/acwing/web/receive_code/", 
            type: "GET",
            data: {
                code: myRoute.query.code,
                state: myRoute.query.state
            },
            success: resp => {
                if(resp.result === "success") {
                    localStorage.setItem("jwt_token", resp.jwt_token);
                    setToken(resp.jwt_token);
                    router.push({ name: "home" });
                    userStore.updatePullingInfo(false);
                } else {
                    router.push({ name: "user_account_login" });
                }
            }
        });

        return {
           
            userStore
        }
    }
}
</script>

<style scoped>

</style>

