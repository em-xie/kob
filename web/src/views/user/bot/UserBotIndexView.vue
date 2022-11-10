<template>
    <ContentField>对战</ContentField>
</template>

<script>
import ContentField from '../../../components/ContentField'
import $ from 'jquery'
import { useStore } from 'vuex';
export default {
    components: {
        ContentField
    },
    setup() {
        const store = useStore();

        // $.ajax({
        //     url: "http://127.0.0.1:8089/user/bot/add/",
        //     type: "post",
        //     data: {
        //         title: "Bot的标题",
        //         description : "Bot的描述",
        //         content: "Bot的代码",
        //     },
        //     headers: {
        //         Authorization: "Bearer " + store.state.user.token,
        //     },
        //     success(resp) {
        //         console.log(resp);
        //     },
        //     error(resp) {
        //         console.log(resp);
        //     }
        // })

        // $.ajax({
        //     url: "http://127.0.0.1:8089/user/bot/remove/",
        //     type: "POST",
        //     data: {
        //         bot_id: 3, //可以修改为自己的bot_id
        //     },
        //     headers: {
        //         Authorization: "Bearer " + store.state.user.token,
        //     },
        //     success(resp) {
        //         console.log(resp);
        //     },
        //     error(resp) {
        //         console.log(resp);
        //     }
        // })

        // $.ajax({
        //     url: "http://127.0.0.1:8089/user/bot/update/",
        //     type: "POST",
        //     data: {
        //         bot_id: 2,
        //         title: "我是Bot_2的标题",
        //         description : "我是Bot_2的描述",
        //         content: "我是Bot_2的代码",
        //     },
        //     headers: {
        //         Authorization: "Bearer " + store.state.user.token,
        //     },
        //     success(resp) {
        //         console.log(resp);
        //     },
        //     error(resp) {
        //         console.log(resp);
        //     }
        // })

        $.ajax({
            url: "http://127.0.0.1:8089/user/bot/getlist/",
            type: "get",
            headers: {
                Authorization: "Bearer " + store.state.user.token,
            },
            success(resp) {
                console.log(resp);
            },
            error(resp) {
                console.log(resp);
            }
        })
    }
}

</script>


<style scoped> 
</style>